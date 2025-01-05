package com.example.cacophony.repository;

import com.example.cacophony.data.model.Message;
import com.example.cacophony.data.model.User;
import org.springframework.data.repository.ListCrudRepository;

import java.util.UUID;

public interface MessageRepository  extends ListCrudRepository<Message, UUID> {}
