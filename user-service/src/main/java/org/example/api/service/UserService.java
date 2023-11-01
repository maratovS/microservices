package org.example.api.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.example.db.dto.UserDto;
import org.example.db.model.User;
import org.example.db.repo.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepo repo;

    @Autowired
    private CompanyFeignClient userServiceFeignClients;

    @Autowired
    private ModelMapper mapper;

    @Transactional
    public Long createUser(UserDto userDto) {
        boolean exist = userServiceFeignClients.existsById(userDto.getCompanyId());
        if(!exist) {
            throw new EntityNotFoundException("Компания с id = %s не существует".formatted(userDto.getCompanyId()));
        }
        User toSave = mapper.map(userDto, User.class);
        return repo.save(toSave).getId();
    }

    @Transactional
    public Boolean existsById(Long id) {
        User User = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id: " + id + " - не существует"));

        if (!User.isEnabled())
            throw new EntityNotFoundException("Пользователь с id: " + id + " - не активен");

        return true;
    }

    @Transactional
    public List<UserDto> getAllUsers() {
        List<User> Users = repo.findAll();
        List<UserDto> userDtos = new ArrayList<>();
        for (User entity : Users) {
            userDtos.add(mapper.map(entity, UserDto.class));
        }
        return userDtos;
    }

    @Transactional
    public String changeEnabled(Long id) {
        User User = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id: " + id + " - не существует"));

        UserDto userDto = mapper.map(User, UserDto.class);
        if (userDto.isEnabled()) {
            userDto.setEnabled(false);
            repo.save(mapper.map(userDto, org.example.db.model.User.class));
            return "disabled";
        } else {
            userDto.setEnabled(true);
            repo.save(mapper.map(userDto, org.example.db.model.User.class));
            return "enabled";
        }
    }

    @Transactional
    public String changeUser(Long id, UserDto changeUserDto) {
        User User = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id: " + id + " - не существует"));

        UserDto userDto = mapper.map(User, UserDto.class);
        if (!changeUserDto.getName().isEmpty()) {
            userDto.setName(changeUserDto.getName());
        }
        if (!changeUserDto.getEmail().isEmpty()) {
            userDto.setEmail(changeUserDto.getEmail());
        }
        if (changeUserDto.getCompanyId() != null) {
            if(!userServiceFeignClients.existsById(changeUserDto.getCompanyId()))
                throw new EntityNotFoundException("Компания с id = %s не существует".formatted(userDto.getCompanyId()));
            userDto.setCompanyId(changeUserDto.getCompanyId());
        }
        repo.save(mapper.map(userDto, org.example.db.model.User.class));
        return "updated";
    }
}
