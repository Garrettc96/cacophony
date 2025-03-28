package com.example.cacophony.service;

import com.example.cacophony.data.UserRole;
import com.example.cacophony.data.model.User;
import com.example.cacophony.exception.DuplicateEntityException;
import com.example.cacophony.exception.NotFoundException;
import com.example.cacophony.repository.UserRepository;
import com.example.cacophony.security.UserInfoDetails;

import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User getUserFromId(UUID id) {
        return this.userRepository.findById(id).orElse(null);
    }

    @Override
    public User getUserFromName(String userName) {
        return this.userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Username %s not found", userName)));
    }

    @Override
    public User createUser(User user) {
        try {
            return this.userRepository.save(User.withPassword(User.withRoles(user, List.of(UserRole.USER_ROLE)),
                    this.passwordEncoder.encode(user.getPassword())));
        } catch (DataIntegrityViolationException ex) {
            // Unique constraint is violated
            throw new DuplicateEntityException("User already exists", ex, User.class);
        }
    }

    @Override
    public UserDetails getUserDetailsFromUser(User user) {
        return new UserInfoDetails(user);
    }

    @Override
    public List<User> listUsers() {
        return this.userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.getUserDetailsFromUser(this.getUserFromName(username));
    }

    @Override
    public List<User> validateUsers(List<User> users) {
        return users.stream().map((User user) -> this.getUserFromId(user.getId())).map((User user) -> {
            if (user == null) {
                log.error("User {} not found", user);
                throw new NotFoundException("User not found");
            }
            return user;
        }).toList();
    }
}
