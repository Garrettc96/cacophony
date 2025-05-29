package com.example.cacophony.service;

import com.example.cacophony.jooq.tables.records.ChatRecord;
import com.example.cacophony.jooq.tables.records.ConversationRecord;
import com.example.cacophony.data.model.Chat;
import com.example.cacophony.data.model.ChatWithMembers;
import com.example.cacophony.data.model.Conversation;
import com.example.cacophony.data.model.ConversationType;
import com.example.cacophony.data.model.User;
import com.example.cacophony.exception.NotFoundException;
import com.example.cacophony.repository.ChatJooqRepository;
import com.example.cacophony.repository.ChatRepository;
import com.example.cacophony.repository.ConversationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ChatServiceImpl implements ChatService {
    private final ChatJooqRepository chatRepository;
    private final ConversationService conversationService;
    private final UserService userService;

    public ChatServiceImpl(ChatJooqRepository chatRepository, ConversationService conversationService,
            UserService userService) {
        this.chatRepository = chatRepository;
        this.conversationService = conversationService;
        this.userService = userService;
    }

    @Override
    public ChatRecord createChat(final ChatRecord chat, List<UUID> members) {
        ConversationRecord conversation = this.conversationService.createConversation(chatToConversation(chat),
                members);
        ChatRecord chatWithConversation = new ChatRecord(conversation.getId(), chat.getName(), chat.getDescription(),
                chat.getCreatedAt(), chat.getUpdatedAt());
        return chatRepository.createChat(chatWithConversation);
    }

    @Override
    public ChatRecord getChat(String chatId) {
        ChatWithMembers result = chatRepository.getChatWithMembers(UUID.fromString(chatId));
        if (result == null) {
            throw new NotFoundException("Chat not found");
        }
        return result.chat();
    }

    @Cacheable(value = "chats", key = "#chatId", unless = "#result == null")
    @Override
    public ChatWithMembers getChatWithMembers(String chatId) {
        ChatWithMembers result = chatRepository.getChatWithMembers(UUID.fromString(chatId));
        if (result == null) {
            throw new NotFoundException("Chat not found");
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canUserAccessChat(UUID userId, String chatId) {
        return this.conversationService.isUserInConversation(UUID.fromString(chatId), userId);
    }

    private ConversationRecord chatToConversation(ChatRecord chat) {
        return new ConversationRecord(null, ConversationType.CHAT.name(), null, null);
    }
}
