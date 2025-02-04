package com.example.cacophony;

import com.example.cacophony.repository.ChatRepository;
import com.example.cacophony.repository.ConversationRepository;
import com.example.cacophony.repository.MessageRepository;
import com.example.cacophony.repository.UserRepository;
import com.example.cacophony.service.*;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

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
    public MessageService messageService(MessageRepository messageRepository) {
        return new MessageServiceImpl(messageRepository);
    }

    @Bean
    public Hibernate5Module hibernateModule() {
        return new Hibernate5Module();
    }
}
