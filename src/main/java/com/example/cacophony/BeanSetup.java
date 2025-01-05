package com.example.cacophony;

import com.example.cacophony.repository.ChatRepository;
import com.example.cacophony.repository.ConversationRepository;
import com.example.cacophony.repository.MessageRepository;
import com.example.cacophony.repository.UserRepository;
import com.example.cacophony.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@EnableJpaAuditing
public class BeanSetup {

    @Bean
    public UserService userService(UserRepository userRepository) {
        return new UserServiceImpl(userRepository);
    }

    @Bean
    public ChatService chatService(ChatRepository chatRepository, ConversationRepository conversationRepository, UserService userService) {
        return new ChatServiceImpl(chatRepository, conversationRepository, userService);
    }

    @Bean
    public MessageService messageService(MessageRepository messageRepository) {
        return new MessageServiceImpl(messageRepository);
    }
}
