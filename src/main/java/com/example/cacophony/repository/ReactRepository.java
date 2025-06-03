package com.example.cacophony.repository;

import java.util.UUID;

import org.springframework.data.repository.ListCrudRepository;

import com.example.cacophony.data.model.React;

public interface ReactRepository extends ListCrudRepository<React, UUID> {
}
