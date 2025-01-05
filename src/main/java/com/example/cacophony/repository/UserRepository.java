package com.example.cacophony.repository;

import com.example.cacophony.data.model.User;
import org.springframework.data.repository.ListCrudRepository;

import java.util.UUID;

public interface UserRepository extends ListCrudRepository<User, UUID> {
    User findByUsername(String username);

}
