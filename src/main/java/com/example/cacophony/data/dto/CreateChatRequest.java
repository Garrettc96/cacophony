package com.example.cacophony.data.dto;

import com.example.cacophony.data.model.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CreateChatRequest {
    @Size(min = 100)
    String name;
    @NotNull
    String description;
    List<@Size(min = 1, max=4, message = "Member is invalid length")String> members;
}
