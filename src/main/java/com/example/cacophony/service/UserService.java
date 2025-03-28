package com.example.cacophony.service;

import com.example.cacophony.data.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.UUID;

public interface UserService extends UserDetailsService{
    public User createUser(User user);
    public User getUserFromId(UUID id);
    public UserDetails getUserDetailsFromUser(User user);
    public User getUserFromName(String name);
    public List<User> listUsers();
    public List<User> validateUsers(List<User> users);
}
