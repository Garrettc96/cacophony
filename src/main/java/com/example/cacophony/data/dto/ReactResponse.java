package com.example.cacophony.data.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ReactResponse {
    UUID id;
    String name;
    String s3Path;
    UUID createdBy;
    OffsetDateTime createdAt;
    OffsetDateTime lastUpdatedAt;
}
