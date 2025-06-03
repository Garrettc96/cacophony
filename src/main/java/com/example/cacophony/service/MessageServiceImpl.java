package com.example.cacophony.service;

import com.example.cacophony.data.dto.ImageUploadDetails;
import com.example.cacophony.exception.NotFoundException;
import com.example.cacophony.jooq.tables.records.MessageRecord;
import com.example.cacophony.repository.MessageJooqRepository;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import org.springframework.transaction.annotation.Transactional;

import static com.example.cacophony.util.Constants.IMAGE_UPLOAD_EXPIRATION_MINUTES;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Slf4j
public class MessageServiceImpl implements MessageService {

    MessageJooqRepository messageRepository;
    ConversationService conversationService;
    S3Presigner presigner;
    String imageBucketName;

    public MessageServiceImpl(MessageJooqRepository messageRepository, ConversationService conversationService,
            S3Presigner s3Presigner, String imageBucketName) {
        this.messageRepository = messageRepository;
        this.conversationService = conversationService;
        presigner = s3Presigner;
        this.imageBucketName = imageBucketName;

    }

    @Override
    public MessageRecord createMessage(MessageRecord message) {
        return this.messageRepository.createMessage(message);
    }

    @Override
    public MessageRecord getMessage(String id) {
        return messageRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new NotFoundException("Message not found"));
    }

    @Override
    public List<MessageRecord> getMessagesInConversationBetweenTimes(UUID conversationId, OffsetDateTime startTime,
            OffsetDateTime endTime) {
        return this.messageRepository.findByConversationIdAndCreatedAtBetween(conversationId, startTime, endTime);
    }

    @Override
    public List<MessageRecord> searchMessages(UUID conversationId, String searchString) {
        return this.messageRepository.searchMessages(conversationId, searchString);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canUserAccessMessage(UUID userId, String conversationId) {
        return this.conversationService.isUserInConversation(UUID.fromString(conversationId), userId);
    }

    @Override
    public ImageUploadDetails generateImageUploadUrl(String conversationId) {
        String keyName = String.format("%s/%s", conversationId, generateKeyName());
        String s3Path = String.format("s3://%s/%s", this.imageBucketName, keyName);
        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(IMAGE_UPLOAD_EXPIRATION_MINUTES)) // The URL expires in 10
                                                                                        // minutes.
                .putObjectRequest(por -> por.bucket(this.imageBucketName).key(keyName)).build();

        PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(presignRequest);
        String myURL = presignedRequest.url().toString();
        log.info("Presigned URL to upload a file to: [{}]", myURL);
        log.info("HTTP method: [{}]", presignedRequest.httpRequest().method());

        return new ImageUploadDetails(presignedRequest.url().toExternalForm(), s3Path);
    }

    private String generateKeyName() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        long epoch = Instant.EPOCH.toEpochMilli();
        return String.format("%s_%d", saltStr, epoch);
    }
}
