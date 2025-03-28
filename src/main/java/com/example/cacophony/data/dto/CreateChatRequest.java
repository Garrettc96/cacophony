package com.example.cacophony.data.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateChatRequest {
    @Size(min = 5, message = "Min name length is 5")
    String name;
    @NotNull(message = "Description can't be null")
    String description;
    List<UUID> members;
}
