package org.example.api.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.example.db.dto.CompanyDto;
import org.example.db.model.Company;
import org.example.db.repo.CompanyRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepo repo;
    @Autowired
    private UserFeignClient client;
    @Autowired
    private ModelMapper mapper;

    @Transactional
    public Long createCompany(CompanyDto companyDto) {
        if (companyDto.getUserId() != null) {
            Boolean exist = client.existsById(companyDto.getUserId());
            if (!exist) {
                throw new EntityNotFoundException("Компания создана. Директор с id = %s не существует".formatted(companyDto.getUserId()));
            }
        }
        return repo.save(mapper.map(companyDto, Company.class)).getId();
    }

    @Transactional
    public Boolean existsById(Long id) {
        return repo.existsById(id);
    }

    @Transactional
    public CompanyDto getById(Long id) {
        return mapper.map(repo.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Компания с id: " + id + " - не существует"))
                , CompanyDto.class);
    }

    @Transactional
    public List<CompanyDto> getAllCompanies() {
        List<Company> rows = repo.findAll();
        if (rows == null){
            rows = new ArrayList<>();
        }
        return rows.stream().map(company -> mapper.map(company, CompanyDto.class)).toList();
    }
}
