package org.example.api.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.example.db.dto.CompanyDto;
import org.example.db.model.Company;
import org.example.db.repo.CompanyRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepo repo;
    @Autowired
    private UserFeignClient client;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Value("${spring.kafka.producer.topic.company-deleted}")
    private String companyDeletedTopic;

    @Transactional
    public CompanyDto createCompany(CompanyDto companyDto) {
        if (companyDto.getUserId() != null) {
            Boolean exist = client.existsById(companyDto.getUserId());
            if (!exist) {
                throw new EntityNotFoundException("Компания создана. Директор с id = %s не существует".formatted(companyDto.getUserId()));
            }
        }
        return mapper.map(repo.save(mapper.map(companyDto, Company.class)), CompanyDto.class);
    }

    public Boolean existsById(Long id) {
        if (repo.existsById(id)){
            Company company = repo.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Компания с id: " + id + " - не существует"));
            return !company.isDeleted();
        }
        return false;
    }

    public CompanyDto getById(Long id) {
        Company company = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Компания с id: " + id + " - не существует"));
        if (!company.isDeleted())
            return mapper.map(company, CompanyDto.class);
        else
            throw new EntityNotFoundException("Компания с id: " + id + " - не существует");
    }

    public List<CompanyDto> getAllCompanies() {
        List<Company> rows = repo.findAll();
        if (rows == null){
            rows = new ArrayList<>();
        }
        return rows.stream().filter(company -> !company.isDeleted()).map(company -> mapper.map(company, CompanyDto.class)).toList();
    }

    public ResponseEntity<String> deleteCompany(Long id)  {
        Company Company = repo.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Компания не найдена"));
        Company.setDeleted(true);
        repo.save(Company);
        kafkaTemplate.send(companyDeletedTopic, id.toString());

        return ResponseEntity.ok("Компания удалена");
    }

    @KafkaListener(topics = "${spring.kafka.consumer.topic.company-deleted-user}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleCompanyDeleted(String id) {
        Optional<Company> Company = repo.findById(Long.parseLong(id));
        if(Company.isPresent())
            repo.delete(Company.get());
        else
            throw new EntityNotFoundException("Компания не найдена");
    }
}
