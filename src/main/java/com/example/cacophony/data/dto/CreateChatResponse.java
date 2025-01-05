package com.example.cacophony.data.dto;

import com.example.cacophony.data.model.User;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CreateChatResponse {
    String id;
    String name;
    String description;
    List<String> members;
}
