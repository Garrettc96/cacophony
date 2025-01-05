package com.example.cacophony.data.dto;

import lombok.Data;

@Data
public class CreateUserRequest extends AbstractRequest{
    private String username;
    private String password;
    private String email;
}