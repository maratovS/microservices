package org.example.api;

import org.example.api.service.UserService;
import org.example.db.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private  UserService userService;

    @PostMapping("/create")
    public Long createUser(@RequestBody UserDto userDto) {
        return userService.createUser(userDto);
    }

    @GetMapping("/exists-by-id/{id}")
    public Boolean existsById(@PathVariable Long id) {
        return userService.existsById(id);
    }

    @GetMapping("/get-all")
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @PatchMapping("/change-enabled/{id}")
    public String changeEnabled(@PathVariable Long id) {
        return userService.changeEnabled(id);
    }

    @PatchMapping("/change-user/{id}")
    public String changeUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        return userService.changeUser(id, userDto);
    }

}
