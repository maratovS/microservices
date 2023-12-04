package org.example.api.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "user-service",
        path = "/user-service/api/user",
        url = "http://localhost:8088"
)
public interface UserFeignClient {
    @GetMapping("/exists-by-id/{id}")
    Boolean existsById (@PathVariable Long id);
}
