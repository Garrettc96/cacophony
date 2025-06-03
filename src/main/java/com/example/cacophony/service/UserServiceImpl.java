package com.example.cacophony.service;

import com.example.cacophony.data.UserRole;
import com.example.cacophony.data.model.User;
import com.example.cacophony.exception.DuplicateEntityException;
import com.example.cacophony.exception.NotFoundException;
import com.example.cacophony.jooq.tables.records.CUserRecord;
import com.example.cacophony.repository.UserJooqRepository;
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

    private final UserJooqRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserJooqRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public CUserRecord getUserFromId(UUID id) {
        return this.userRepository.findUserById(id).orElse(null);
    }

    @Override
    public CUserRecord getUserFromName(String userName) {
        return this.userRepository.findUserByName(userName)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Username %s not found", userName)));
    }

    @Override
    public CUserRecord createUser(CUserRecord user) {
        try {
            return this.userRepository.createUser(user);
        } catch (DataIntegrityViolationException ex) {
            // Unique constraint is violated
            throw new DuplicateEntityException("User already exists", ex, User.class);
        }
    }

    @Override
    public UserDetails getUserDetailsFromUser(CUserRecord user) {
        return new UserInfoDetails(user);
    }

    @Override
    public List<CUserRecord> listUsers() {
        return this.userRepository.listUsers();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.getUserDetailsFromUser(this.getUserFromName(username));
    }

    @Override
    public List<CUserRecord> validateUsers(List<UUID> userIdList) {
        return userIdList.stream().map((UUID userId) -> this.getUserFromId(userId)).map((CUserRecord user) -> {
            if (user == null) {
                log.error("User {} not found", user);
                throw new NotFoundException("User not found");
            }
            return user;
        }).toList();
    }
}
