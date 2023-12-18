package org.example.db.repo;

import org.example.db.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Long> {
    List<User> findAllByCompanyId(Long id);
}
