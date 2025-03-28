package com.example.cacophony.data.dto;

import com.example.cacophony.data.model.User;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class CreateChatResponse {
    UUID id;
    String name;
    String description;
    List<UUID> members;

}
