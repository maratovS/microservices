package org.example.api;

import org.example.api.service.CompanyService;
import org.example.db.dto.CompanyDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/company")
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    @PostMapping("/create")
    public Long createCompany(@RequestBody CompanyDto companyDto) {
        return companyService.createCompany(companyDto);
    }

    @GetMapping("/exists-by-id/{companyId}")
    Boolean existsById (@PathVariable Long companyId) {
        return companyService.existsById(companyId);
    }

    @GetMapping("/get-all")
    List<CompanyDto> getAllCompanies () {
        return companyService.getAllCompanies();
    }
}
