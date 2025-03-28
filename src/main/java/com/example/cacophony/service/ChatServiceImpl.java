package com.example.cacophony.service;

import com.example.cacophony.data.model.Chat;
import com.example.cacophony.data.model.Conversation;
import com.example.cacophony.data.model.User;
import com.example.cacophony.exception.NotFoundException;
import com.example.cacophony.repository.ChatRepository;
import com.example.cacophony.repository.ConversationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;
    private final ConversationRepository conversationRepository;
    private final UserService userService;
    public ChatServiceImpl(ChatRepository chatRepository, ConversationRepository conversationRepository, UserService userService) {
        this.chatRepository = chatRepository;
        this.conversationRepository = conversationRepository;
        this.userService = userService;
    }

    public Chat createChat(final Chat chat) {
        List<User> chatMembers = this.userService.validateUsers(chat.getConversation().getMembers());
        Conversation conversation = this.conversationRepository.save(Conversation.builder()
                .members(chatMembers)
                .type(chat.getConversation().getType())
                .build()
        );
        return chatRepository.save(Chat.of(chat, conversation));
    }

    public Chat getChat(String chatId) {
        return chatRepository.findById(UUID.fromString(chatId)).orElseThrow(() -> new NotFoundException("Chat not found"));
    }

    public List<Chat> getChatsByTimestamp(OffsetDateTime startTime, OffsetDateTime endTime) {
        return this.chatRepository.findByCreatedAtBetween(startTime, endTime);
    }

    @Override
    @Transactional(readOnly = true)
        public boolean canUserAccessChat(UUID userId, String chatId) {
        return getChat(chatId)
            .getConversation()
            .getMembers()
            .contains(User.fromId(userId));
        }
}
