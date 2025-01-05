package com.example.cacophony.data.dto;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.springframework.http.client.ClientHttpResponse;

@Data
public abstract class AbstractRequest {
    public AbstractRequest() {}
}
