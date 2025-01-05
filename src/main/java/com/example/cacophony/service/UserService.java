package com.example.cacophony.service;

import com.example.cacophony.data.model.User;

import java.util.List;

public interface UserService {
    public User createUser(User user);
    public User getUserFromId(String id);
    public User getUserFromName(String name);
    public List<User> listUsers();
}
