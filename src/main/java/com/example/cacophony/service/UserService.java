package com.example.cacophony.service;

import com.example.cacophony.data.model.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    public User createUser(User user);
    public User getUserFromId(UUID id);
    public User getUserFromName(String name);
    public List<User> listUsers();
}
