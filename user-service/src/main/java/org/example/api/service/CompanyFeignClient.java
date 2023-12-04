package org.example.api.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "company-service",
        path = "/company-service/api/company",
        url = "localhost:8088"
)
public interface CompanyFeignClient {
    @GetMapping("/exists-by-id/{companyId}")
    Boolean existsById (@PathVariable Long companyId);
}
