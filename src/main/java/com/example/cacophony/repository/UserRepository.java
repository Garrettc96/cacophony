package com.example.cacophony.repository;

import com.example.cacophony.data.model.User;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends ListCrudRepository<User, UUID> {
    Optional<User> findByUsername(String username);

}
