package org.example.api.service;

import jakarta.transaction.Transactional;
import org.example.db.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepo repo;


}
