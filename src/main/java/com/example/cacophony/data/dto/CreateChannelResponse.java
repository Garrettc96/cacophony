package com.example.cacophony.data.dto;

import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateChannelResponse {
    UUID id;
    String name;
    String description;
    List<UUID> members;
}
