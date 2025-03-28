package com.example.cacophony;

import com.example.cacophony.mapper.ModelMapper;
import com.example.cacophony.repository.ChannelRepository;
import com.example.cacophony.repository.ChatRepository;
import com.example.cacophony.repository.ConversationRepository;
import com.example.cacophony.repository.MessageRepository;
import com.example.cacophony.repository.UserRepository;
import com.example.cacophony.security.ResourceAccessFilter;
import com.example.cacophony.service.*;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

import java.nio.channels.Channels;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableJpaAuditing
@PropertySource("classpath:application.properties")
public class BeanSetup {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Password encoding
    }

    @Bean
    public UserService userService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return new UserServiceImpl(userRepository, passwordEncoder);
    }

    @Bean
    public ChatService chatService(ChatRepository chatRepository, ConversationRepository conversationRepository, UserService userService) {
        return new ChatServiceImpl(chatRepository, conversationRepository, userService);
    }

    @Bean
    public ChannelService channelService(ChannelRepository channelRepository, ConversationRepository conversationRepository, UserService userService) {
        return new ChannelServiceImpl(channelRepository, conversationRepository, userService);
    }

    @Bean
    public ConversationService conversationService(ConversationRepository conversationRepository) {
        return new ConversationServiceImpl(conversationRepository);
    }

    @Bean
    public MessageService messageService(MessageRepository messageRepository, ConversationService conversationService) {
        return new MessageServiceImpl(messageRepository, conversationService);
    }

    @Bean
    public Hibernate5Module hibernateModule() {
        return new Hibernate5Module();
    }

    @Bean
    public ResourceAccessFilter resourceAccessFilter(MessageService messageService, ConversationService conversationService, UserService userService,
            ChatService chatService, ChannelService channelService) {
        return new ResourceAccessFilter(messageService, conversationService, userService, chatService, channelService);
    }
    
    @Bean
    public ModelMapper modelMapper(UserService userService) {
        return new ModelMapper(userService);
    }
}
