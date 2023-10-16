package org.example.api;

import org.example.db.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(
        name = "user-service",
        path = "/api/user",
        url = "localhost:8089"
)
public class UserServiceFeignClient {
    @GetMapping("/users/all")
    List<User> getAllUsers(){
        return null;
    }
}
