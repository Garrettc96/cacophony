package com.example.cacophony.data.dto;

import java.time.OffsetDateTime;

public record ImageUploadResponse(String conversationId, String url, String s3Path, OffsetDateTime experationTime) {
}
