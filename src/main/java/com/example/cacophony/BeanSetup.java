package com.example.cacophony;

import com.example.cacophony.mapper.ModelMapper;
import com.example.cacophony.repository.ChannelJooqRepository;
import com.example.cacophony.repository.ChannelRepository;
import com.example.cacophony.repository.ChatJooqRepository;
import com.example.cacophony.repository.ChatRepository;
import com.example.cacophony.repository.ConversationJooqRepository;
import com.example.cacophony.repository.ConversationRepository;
import com.example.cacophony.repository.MessageJooqRepository;
import com.example.cacophony.repository.MessageRepository;
import com.example.cacophony.repository.ReactJooqRepository;
import com.example.cacophony.repository.ReactRepository;
import com.example.cacophony.repository.UserJooqRepository;
import com.example.cacophony.repository.UserRepository;
import com.example.cacophony.security.ResourceAccessFilter;
import com.example.cacophony.service.*;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.codegen.GenerationTool;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.unit.DataSize;

@Configuration
@PropertySource("classpath:application.properties")
@Profile("prod")
public class BeanSetup {

    @Bean
    public Connection jooqConnection(@Value("${spring.datasource.url}") String url,
            @Value("${spring.datasource.username}") String userName,
            @Value("${spring.datasource.password}") String password) throws SQLException {
        return DriverManager.getConnection(url, userName, password);
    }

    @Bean
    public DSLContext dslContext(Connection conn, @Value("${spring.datasource.username}") String userName,
            @Value("${spring.datasource.password}") String password, @Value("${spring.datasource.url}") String url)
            throws Exception {
        return DSL.using(conn, SQLDialect.POSTGRES);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10); // Password encoding
    }

    @Bean
    public UserJooqRepository userJooqRepository(DSLContext dsl) {
        return new UserJooqRepository(dsl);
    }

    @Bean
    public ChatJooqRepository chatJooqRepository(DSLContext dsl) {
        return new ChatJooqRepository(dsl);
    }

    @Bean
    public ChannelJooqRepository channelJooqRepository(DSLContext dsl) {
        return new ChannelJooqRepository(dsl);
    }

    @Bean
    public ConversationJooqRepository conversationJooqRepository(DSLContext dsl) {
        return new ConversationJooqRepository(dsl);
    }

    @Bean
    public ReactJooqRepository reactJooqRepository(DSLContext dsl) {
        return new ReactJooqRepository(dsl);
    }

    @Bean
    public UserService userService(UserJooqRepository userRepository, PasswordEncoder passwordEncoder) {
        return new UserServiceImpl(userRepository, passwordEncoder);
    }

    @Bean
    public ChatService chatService(ChatJooqRepository chatRepository, ConversationService conversationService,
            UserService userService) {
        return new ChatServiceImpl(chatRepository, conversationService, userService);
    }

    @Bean
    public ChannelService channelService(ChannelJooqRepository channelRepository,
            ConversationService conversationService, UserService userService) {
        return new ChannelServiceImpl(channelRepository, conversationService, userService);
    }

    @Bean
    public ConversationService conversationService(ConversationJooqRepository conversationRepository) {
        return new ConversationServiceImpl(conversationRepository);
    }

    @Bean
    public MessageService messageService(MessageJooqRepository messageRepository,
            ConversationService conversationService, S3Presigner s3Presigner,
            @Value("${cacophony.s3.imageBucket}") String imageBucket) {
        return new MessageServiceImpl(messageRepository, conversationService, s3Presigner, imageBucket);
    }

    @Bean
    public ReactService reactService(ReactJooqRepository reactRepository) {
        return new ReactServiceImpl(reactRepository);
    }

    @Bean
    public Hibernate5Module hibernateModule() {
        return new Hibernate5Module();
    }

    @Bean
    public ResourceAccessFilter resourceAccessFilter(MessageService messageService,
            ConversationService conversationService, UserService userService, ChatService chatService,
            ChannelService channelService) {
        return new ResourceAccessFilter(messageService, conversationService, userService, chatService, channelService);
    }

    @Bean
    public ModelMapper modelMapper(UserService userService, PasswordEncoder passwordEncoder) {
        return new ModelMapper(userService, passwordEncoder);
    }

    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder().region(Region.US_EAST_1).build();
    }
}
