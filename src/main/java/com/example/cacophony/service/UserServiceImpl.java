package com.example.cacophony.service;

import com.example.cacophony.data.model.User;
import com.example.cacophony.exception.DuplicateEntityException;
import com.example.cacophony.repository.UserRepository;
import org.postgresql.util.PSQLException;
import org.springframework.dao.DataIntegrityViolationException;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUserFromId(UUID id) {
        return this.userRepository.findById(id).orElse(null);
    }

    @Override
    public User getUserFromName(String userName) {
        return this.userRepository.findByUsername(userName);
    }

    @Override
    public User createUser(User user){
        try {
            return this.userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            // Unique constraint is violated
            throw new DuplicateEntityException("User already exists", ex);
        }
    }

    @Override
    public List<User> listUsers() {
        return this.userRepository.findAll();
    }
}
