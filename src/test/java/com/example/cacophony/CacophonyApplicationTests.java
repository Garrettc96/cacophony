package com.example.cacophony;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.net.URI;
import java.net.URL;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.hamcrest.CoreMatchers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import software.amazon.awssdk.http.SdkHttpMethod;
import software.amazon.awssdk.http.SdkHttpRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import org.testcontainers.junit.jupiter.Container;

import com.example.cacophony.data.dto.CreateChannelRequest;
import com.example.cacophony.data.dto.ChannelResponse;
import com.example.cacophony.data.dto.ChannelVisibilityEnum;
import com.example.cacophony.data.dto.CreateChatRequest;
import com.example.cacophony.data.dto.ChatResponse;
import com.example.cacophony.data.dto.CreateMessageRequest;
import com.example.cacophony.data.dto.CreateUserRequest;
import com.example.cacophony.data.dto.GenerateTokenResponse;
import com.example.cacophony.data.dto.MessageResponse;
import com.example.cacophony.data.model.AuthRequest;
import com.example.cacophony.service.UserService;
import com.example.cacophony.TestConstants;
import static com.example.cacophony.TestConstants.TEST_USER;
import static com.example.cacophony.TestConstants.TEST_EMAIL;
import static com.example.cacophony.TestConstants.TEST_PASSWORD;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@TestPropertySource("classpath:application-test.properties")
@ActiveProfiles("test")
@Import(TestBeanSetup.class)
class CacophonyApplicationTests {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private S3Presigner s3PresignerMock;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity())
                .defaultRequest(get("/").contextPath("/cacophony")) // Use contextPath instead of servletPath
                .alwaysDo(MockMvcResultHandlers.print()).build();
        cleanupDatabase();
    }

    void cleanupDatabase() {
        // First ensure the schema exists
        jdbcTemplate.execute("CREATE SCHEMA IF NOT EXISTS cacophony");

        // Get tables from the cacophony schema instead of public
        List<String> tableNames = jdbcTemplate.queryForList(
                "SELECT tablename FROM pg_catalog.pg_tables WHERE schemaname = 'cacophony';", String.class);
        List<String> staticTables = List.of("user_role", "conversation_type", "channel_visibility");
        // Truncate all tables
        tableNames.forEach(tableName -> {
            try {
                if (!staticTables.contains(tableName)) {
                    jdbcTemplate.execute("TRUNCATE TABLE cacophony." + tableName + " CASCADE");
                }

            } catch (Exception ex) {
                System.out.println("Error truncating table " + tableName + ": " + ex.getMessage());
            }
        });
    }

    @Test
    void contextLoads() {
    }

    @Test
    void testCreateUser() throws Exception {
        // Create a mock user creation request
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("testuser");
        request.setPassword("password123");
        request.setEmail("testuser@example.com");

        // Perform the API call to create a new user
        mockMvc.perform(post("/cacophony/users").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request))).andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("testuser"))
                .andExpect(jsonPath("$.email").value("testuser@example.com"));
    }

    @Test
    void testGenerateToken() throws Exception {
        // First create a user that we can authenticate
        CreateUserRequest createRequest = new CreateUserRequest();
        createRequest.setUsername("authuser");
        createRequest.setPassword("password123");
        createRequest.setEmail("authuser@example.com");

        // Create the user first
        mockMvc.perform(post("/cacophony/users").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest))).andExpect(status().isOk());

        // Create auth request
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("authuser");
        authRequest.setPassword("password123");

        // Test token generation
        mockMvc.perform(post("/cacophony/users/generateToken").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest))).andExpect(status().isOk())
                .andExpect(content().string(CoreMatchers.not(""))); // Verify we get a non-empty token back
    }

    @Test
    void testCreateChat() throws Exception {
        String token = generateToken();

        // Create chat request
        CreateChatRequest request = new CreateChatRequest();
        request.setName("Test Chat");
        request.setDescription("A test chat room");
        request.setMembers(List.of(userService.getUserFromName(TEST_USER).getId()));

        // Test chat creation
        mockMvc.perform(post("/cacophony/chats").header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Test Chat"))
                .andExpect(jsonPath("$.description").value("A test chat room"))
                .andExpect(jsonPath("$.members").isArray())
                .andExpect(jsonPath("$.members[0]").value(userService.getUserFromName(TEST_USER).getId().toString()))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void testCreateChannel() throws Exception {
        String token = generateToken();

        // Create channel request
        CreateChannelRequest request = new CreateChannelRequest();
        request.setName("test channel name");
        request.setDescription("random test description");
        request.setMembers(List.of(userService.getUserFromName(TEST_USER).getId()));
        request.setVisibility(ChannelVisibilityEnum.PUBLIC);
        // Test channel creation
        mockMvc.perform(post("/cacophony/channels").header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.name").value("test channel name"))
                .andExpect(jsonPath("$.description").value("random test description"))
                .andExpect(jsonPath("$.members").isArray())
                .andExpect(jsonPath("$.members[0]").value(userService.getUserFromName(TEST_USER).getId().toString()))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void testSendMessageToChat() throws Exception {
        String token = generateToken();

        // First create a chat to send message to
        CreateChatRequest chatRequest = new CreateChatRequest();
        chatRequest.setName("Test Chat");
        chatRequest.setDescription("A test chat room");
        chatRequest.setMembers(List.of(userService.getUserFromName(TEST_USER).getId()));

        // Create the chat and get its ID
        String chatResponse = mockMvc
                .perform(post("/cacophony/chats").header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(chatRequest)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        ChatResponse createChannel = objectMapper.readValue(chatResponse, new TypeReference<ChatResponse>() {
        });

        sendMessage(createChannel.getId().toString(), token);
        sendMessage(createChannel.getId().toString(), token);
        sendMessage(createChannel.getId().toString(), token);

        String searchResponse = mockMvc
                .perform(get("/cacophony/conversations/search").header("Authorization", "Bearer " + token)
                        .queryParam("conversationId", createChannel.getId().toString()).queryParam("startEpoch", "0")
                        .queryParam("endEpoch", String.valueOf(OffsetDateTime.now().toInstant().toEpochMilli())))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        List<MessageResponse> messages = objectMapper.readValue(searchResponse,
                new TypeReference<List<MessageResponse>>() {
                });
        assertNotNull(messages);
        assertTrue(messages.size() == 3);
    }

    @Test
    void testSendMessageToChannel() throws Exception {
        String token = generateToken();

        // First create a chat to send message to
        CreateChannelRequest channelRequest = new CreateChannelRequest();
        channelRequest.setName("Test channel");
        channelRequest.setDescription("A test chat room");
        channelRequest.setMembers(List.of(userService.getUserFromName(TEST_USER).getId()));
        channelRequest.setVisibility(ChannelVisibilityEnum.PUBLIC);
        // Create the chat and get its ID
        String channelResponse = mockMvc
                .perform(post("/cacophony/channels").header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(channelRequest)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        ChannelResponse createChannel = objectMapper.readValue(channelResponse, new TypeReference<ChannelResponse>() {
        });

        sendMessage(createChannel.getId().toString(), token);
        sendMessage(createChannel.getId().toString(), token);
        sendMessage(createChannel.getId().toString(), token);

        String searchResponse = mockMvc
                .perform(get("/cacophony/conversations/search").header("Authorization", "Bearer " + token)
                        .queryParam("conversationId", createChannel.getId().toString()).queryParam("startEpoch", "0")
                        .queryParam("endEpoch", String.valueOf(OffsetDateTime.now().toInstant().toEpochMilli())))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        List<MessageResponse> messages = objectMapper.readValue(searchResponse,
                new TypeReference<List<MessageResponse>>() {
                });
        assertNotNull(messages);
        assertTrue(messages.size() == 3);
    }

    @Test
    void testConversationSearchTimeRange() throws Exception {
        String token = generateToken();
        CreateChatRequest chatRequest = new CreateChatRequest();
        chatRequest.setName("Test Chat");
        chatRequest.setDescription("A test chat room");
        chatRequest.setMembers(List.of(userService.getUserFromName(TEST_USER).getId()));

        // Create the chat and get its ID
        String chatResponse = mockMvc
                .perform(post("/cacophony/chats").header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(chatRequest)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        String chatId = objectMapper.readTree(chatResponse).get("id").asText();

        sendMessage(chatId, token);
        Thread.sleep(1000);
        Instant startSearchInterval = Instant.now();
        sendMessage(chatId, token);

        Instant endSearchInterval = Instant.now();
        Thread.sleep(1000);
        sendMessage(chatId, token);

        String searchResponse = mockMvc
                .perform(get("/cacophony/conversations/search").header("Authorization", "Bearer " + token)
                        .queryParam("conversationId", chatId)
                        .queryParam("startEpoch", String.valueOf(startSearchInterval.toEpochMilli()))
                        .queryParam("endEpoch", String.valueOf(endSearchInterval.toEpochMilli())))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        List<MessageResponse> messages = objectMapper.readValue(searchResponse,
                new TypeReference<List<MessageResponse>>() {
                });
        assertNotNull(messages);
        assertTrue(messages.size() == 1);
    }

    @Test
    void testMessageImageUpload() throws Exception {
        String token = generateToken();

        PresignedPutObjectRequest response = mock(PresignedPutObjectRequest.class);
        URL urlMock = mock(URL.class);
        when(urlMock.toString()).thenReturn("localhost");
        when(urlMock.toExternalForm()).thenReturn("localhost");
        when(response.url()).thenReturn(urlMock);
        SdkHttpRequest mockSdkHttpRequest = mock(SdkHttpRequest.class);
        when(mockSdkHttpRequest.method()).thenReturn(SdkHttpMethod.GET);
        when(response.httpRequest()).thenReturn(mockSdkHttpRequest);
        when(s3PresignerMock.presignPutObject(any(PutObjectPresignRequest.class))).thenReturn(response);
        // First create a chat to upload image to
        CreateChatRequest chatRequest = new CreateChatRequest();
        chatRequest.setName("Test Chat");
        chatRequest.setDescription("A test chat room");
        chatRequest.setMembers(List.of(userService.getUserFromName(TEST_USER).getId()));

        // Create the chat and get its ID
        String chatResponse = mockMvc
                .perform(post("/cacophony/chats").header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(chatRequest)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        ChatResponse createChat = objectMapper.readValue(chatResponse, new TypeReference<ChatResponse>() {
        });

        // Test image upload URL generation
        mockMvc.perform(get("/cacophony/messages/" + createChat.getId() + "/uploadImage").header("Authorization",
                "Bearer " + token)).andExpect(status().isOk())
                .andExpect(jsonPath("$.conversationId").value(createChat.getId().toString()))
                .andExpect(jsonPath("$.s3Path").exists()).andExpect(jsonPath("$.url").exists());
    }

    private String generateToken() throws Exception {
        // First create a user that we can authenticate
        CreateUserRequest createRequest = new CreateUserRequest();
        createRequest.setUsername(TEST_USER);
        createRequest.setPassword(TEST_PASSWORD);
        createRequest.setEmail(TEST_EMAIL);

        // Create the user first
        mockMvc.perform(post("/cacophony/users").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest))).andExpect(status().isOk());

        // Create auth request - Use the same credentials as the created user
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername(TEST_USER); // Changed from "authuser"
        authRequest.setPassword(TEST_PASSWORD); // Changed from "password123"

        // Test token generation
        String tokenResponse = mockMvc
                .perform(post("/cacophony/users/generateToken").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(tokenResponse, new TypeReference<GenerateTokenResponse>() {
        }).getToken();
    }

    private String getRandomString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

    private void sendMessage(String conversationId, String token) throws Exception {
        CreateMessageRequest createMessageRequest = CreateMessageRequest.builder()
                .conversationId(UUID.fromString(conversationId)).message(getRandomString()).build();

        // Test sending a message
        mockMvc.perform(post("/cacophony/messages").header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(createMessageRequest)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    }

}
