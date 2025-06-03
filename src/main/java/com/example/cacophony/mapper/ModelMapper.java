package com.example.cacophony.mapper;

import com.example.cacophony.data.dto.*;
import com.example.cacophony.data.model.*;
import com.example.cacophony.data.UserRole;
import com.example.cacophony.jooq.tables.records.CUserRecord;
import com.example.cacophony.jooq.tables.records.ChannelRecord;
import com.example.cacophony.jooq.tables.records.ChatRecord;
import com.example.cacophony.jooq.tables.records.MessageRecord;
import com.example.cacophony.jooq.tables.records.ReactRecord;
import com.example.cacophony.service.UserService;
import com.example.cacophony.util.Identity;
import static com.example.cacophony.util.Constants.IMAGE_UPLOAD_EXPIRATION_MINUTES;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;

public class ModelMapper {

    UserService userService;
    PasswordEncoder passwordEncoder;

    public ModelMapper(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public CUserRecord requestToUser(CreateUserRequest createUserRequest) {
        return new CUserRecord(null, createUserRequest.getUsername(), createUserRequest.getEmail(),
                this.passwordEncoder.encode(createUserRequest.getPassword()), null, null, UserRole.USER_ROLE.name());
    }

    public CreateUserResponse userToResponse(CUserRecord user) {
        return CreateUserResponse.builder().id(user.getId()).email(user.getEmail()).userName(user.getUsername())
                .build();
    }

    public ChatRecord requestToChat(CreateChatRequest createChatRequest) {
        return new ChatRecord(null, createChatRequest.getName(), createChatRequest.getDescription(), null, null);
    }

    public ChatResponse chatToResponse(ChatRecord chat, List<UUID> members) {
        return ChatResponse.builder().id(chat.getId()).name(chat.getName()).description(chat.getDescription())
                .members(members).build();
    }

    public MessageRecord createMessageRequestToMessage(CreateMessageRequest createMessageRequest) {
        return new MessageRecord(null, UUID.fromString(Identity.getUserId()), createMessageRequest.getConversationId(),
                createMessageRequest.getMessage(), createMessageRequest.getS3Path().orElse(null), null, null, null);
    }

    public MessageResponse messageToResponse(MessageRecord message) {
        return MessageResponse.builder().id(message.getId()).userId(message.getUserId()).message(message.getText())
                .conversationId(message.getConversationId()).s3Path(message.getS3Path())
                .createdAt(message.getCreatedAt()).updatedAt(message.getUpdatedAt()).build();
    }

    public ChannelRecord requestToChannel(CreateChannelRequest request) {
        return new ChannelRecord(null, request.getName(), request.getDescription(), request.getVisibility().toString(),
                null, null);
    }

    public ChannelResponse channelToCreateResponse(ChannelRecord channel, List<UUID> members) {
        return ChannelResponse.builder().id(channel.getId()).name(channel.getName())
                .description(channel.getDescription()).members(members).build();
    }

    public List<MessageResponse> listOfMessagesToRespones(List<MessageRecord> messageList) {
        return messageList.stream().map(this::messageToResponse).toList();
    }

    public ImageUploadResponse createImpageUploadResponse(String conversationId, String s3Path, String url) {
        return new ImageUploadResponse(conversationId, url, s3Path,
                OffsetDateTime.now(ZoneOffset.UTC).plusMinutes(IMAGE_UPLOAD_EXPIRATION_MINUTES));
    }

    public ReactRecord createReactRequestToReact(CreateReactRequest createReactBody) {
        return new ReactRecord(null, UUID.fromString(Identity.getUserId()), createReactBody.name,
                createReactBody.s3Path, null, null);
    }

    public ReactResponse reactToReactResponse(ReactRecord react) {
        return ReactResponse.builder().id(react.getId()).name(react.getName()).s3Path(react.getS3Path())
                .createdAt(react.getCreatedAt()).createdBy(react.getUserId()).lastUpdatedAt(react.getUpdatedAt())
                .build();
    }
}
