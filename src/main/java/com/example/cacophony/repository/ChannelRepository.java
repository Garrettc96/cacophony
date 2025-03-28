package com.example.cacophony.repository;

import java.util.UUID;

import org.springframework.data.repository.ListCrudRepository;

import com.example.cacophony.data.model.Channel;

public interface ChannelRepository extends ListCrudRepository<Channel, UUID> {

}
