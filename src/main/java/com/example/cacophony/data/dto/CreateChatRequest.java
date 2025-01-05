package com.example.cacophony.data.dto;

import com.example.cacophony.data.model.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CreateChatRequest {
    @Size(min = 5, message = "Min name length is 5")
    String name;
    @NotNull(message = "Description can't be null")
    String description;
    List<UUID> members;
}
