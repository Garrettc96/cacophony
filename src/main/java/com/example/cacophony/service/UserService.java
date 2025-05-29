package com.example.cacophony.service;

import com.example.cacophony.data.model.User;
import com.example.cacophony.jooq.tables.records.CUserRecord;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.UUID;

public interface UserService extends UserDetailsService {
    public CUserRecord createUser(CUserRecord user);

    public CUserRecord getUserFromId(UUID id);

    public UserDetails getUserDetailsFromUser(CUserRecord user);

    public CUserRecord getUserFromName(String name);

    public List<CUserRecord> listUsers();

    public List<CUserRecord> validateUsers(List<UUID> users);
}
