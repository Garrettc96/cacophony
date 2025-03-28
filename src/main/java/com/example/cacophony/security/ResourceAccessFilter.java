package com.example.cacophony.security;

import com.example.cacophony.data.dto.CreateMessageRequest;
import com.example.cacophony.data.dto.ResourceAuthorizationBody;
import com.example.cacophony.data.model.ModelType;
import com.example.cacophony.data.model.User;
import com.example.cacophony.exception.NotFoundException;
import com.example.cacophony.service.ChannelService;
import com.example.cacophony.service.ChatService;
import com.example.cacophony.service.ConversationService;
import com.example.cacophony.service.MessageService;
import com.example.cacophony.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.nio.channels.Channels;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.example.cacophony.util.json.MAPPER;

/*
  Examples:
  GET getChat/{id}
  GET getMessages/{id}
  POST createMessage (chat in body)
 */

record AuthTypeAndId(
    String id,
    ModelType resourceType
){}

@Slf4j
public class ResourceAccessFilter extends OncePerRequestFilter {

  final MessageService messageService;
  final ConversationService conversationService;
  final ChatService chatService;
  final ChannelService channelService;
  final UserService userService;

  public ResourceAccessFilter(MessageService messageService, ConversationService conversationService, UserService userService,
      ChatService chatService, ChannelService channelService) {
    this.messageService = messageService;
    this.conversationService = conversationService;
    this.userService = userService;
    this.chatService = chatService;
    this.channelService = channelService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if ( authentication == null || authentication.getPrincipal() == null) {
      filterChain.doFilter(request, response);
      return;
    }
    String fullRequestPathWithQuery = request.getRequestURL().toString();
    String queryString = request.getQueryString();
    if (queryString != null) {
      fullRequestPathWithQuery += "?" + queryString;
    }

    String requestPath = request.getRequestURL().toString();
    UserInfoDetails userDetails = ( (UserInfoDetails)authentication.getPrincipal());
    ResourceAccessType resourceAccessType = parseRequestToResourceAccessType(request, requestPath);
    AuthTypeAndId authTypeAndId = switch (resourceAccessType) {
      case ResourceAccessType.ResourceAccessPathId t ->
          parseAuthTypeAndIdFromRequest(fullRequestPathWithQuery);
      case ResourceAccessType.ResourceAccessBodyId t -> parseAuthTypeAndIdFromBody(request, t);
      case ResourceAccessType.ResourceAccessGeneral t -> null;
    };
    if (canUserAccessAuthTypeAndId(userDetails, authTypeAndId)) {
      filterChain.doFilter(request, response);
    } else {
      throw new AuthorizationDeniedException("User does not have permission to access requested resource.");
    }

      // Take request and convert to an Id and a type

      // Based on the resource type, and if it is a get or a post,
      // match on resource type
      // case "chat", "message" "User" etc
      // then call function on appropriate service to then check if user has access to that specific resource
      // If its a create, do they always have permission?
      //   - No they don't, you can't create a message in a chat you don't have access to.
  }
  private boolean canUserAccessAuthTypeAndId(UserInfoDetails userDetails, AuthTypeAndId authTypeAndId) {
    // This is only true if ResourceAccessType is ResourceAccessGeneral which means any user can access
    if (authTypeAndId == null) {
      return true;
    }
    return switch(authTypeAndId.resourceType()) {
      case USER -> userDetails.userId.equals(UUID.fromString(authTypeAndId.id()));
      case MESSAGE -> this.messageService.canUserAccessMessage(userDetails.userId, authTypeAndId.id());
      case CONVERSATION -> this.conversationService.isUserInConversation(
        UUID.fromString(authTypeAndId.id()), userDetails.userId);
    };
  }

  private String findGuid(String str) {
    Pattern pattern = Pattern.compile("[a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12}");
    Matcher matcher = pattern.matcher(str);
    if (matcher.find()) {
        return matcher.group(0);
    }
    return null;
  }

  private AuthTypeAndId parseAuthTypeAndIdFromRequest(String requestPath) {
    // GUID regex
    String id = findGuid(requestPath);
    List<String> splitPath = Arrays.stream(requestPath.split("/")).toList();
    String authType = splitPath.get(splitPath.size()-2);
    return new AuthTypeAndId(
        id,
        ModelType.fromString(authType)
    );
  }

  private AuthTypeAndId parseAuthTypeAndIdFromBody(HttpServletRequest request, ResourceAccessType.ResourceAccessBodyId resourceAccessBodyType) {
    List<String> splitPath = Arrays.stream(request.getRequestURI().split("/")).toList();
    String authType = splitPath.get(splitPath.size() - 2);
    switch (resourceAccessBodyType) {
      case ResourceAccessType.ResourceAccessBodyId t -> {
        try {
          String lines = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
          for (Class subClass: ResourceAuthorizationBody.class.getPermittedSubclasses()) {
            try {
              ResourceAuthorizationBody body =
                  (ResourceAuthorizationBody) MAPPER.readValue(lines, subClass);
              
              return new AuthTypeAndId(body.getAuthId(), body.getModelType());
            } catch (Exception ex) {
              log.info("Unable to parse authBody to {} with ex {}", subClass, ex.toString());
            }
          }
        } catch (IOException ex) {
          log.error("Unable to read body of request", ex);
          throw new RuntimeException("Unable to read body of request", ex);
        }
      }
    }
    return new AuthTypeAndId("", ModelType.fromString(authType));
  }

  private ResourceAccessType parseRequestToResourceAccessType(HttpServletRequest request, String requestPath) {
    String resourcePath = Arrays.stream(requestPath.split("cacophony")).toList().getLast();
    String requestMethod = request.getMethod().toLowerCase();
    String GET = "get";
    String POST = "post";
    if (resourcePath.startsWith("/chats")) {
      // GET /chats/{id}
      if (requestMethod.equals(POST) && resourcePath.matches("\\/chats\\/[a-z-]*")) {
        return new ResourceAccessType.ResourceAccessPathId();
        // POST /chats
      } else if (requestMethod.equals(POST) && resourcePath.matches("/chats")) {
        return new ResourceAccessType.ResourceAccessGeneral();
      } else {
        throw new NotFoundException("Resource URL not found when authorizing chats request");
      }
    } else if (resourcePath.startsWith("/channels")) {
      if (requestMethod.equals(POST) && resourcePath.matches("\\/channels\\/[a-z-]*")) {
        return new ResourceAccessType.ResourceAccessPathId();
        // POST /chats
      } else if (requestMethod.equals(POST) && resourcePath.matches("/channels")) {
        return new ResourceAccessType.ResourceAccessGeneral();
      } else {
        throw new NotFoundException("Resource URL not found when authorizing channels request");
      }
    } else if (resourcePath.startsWith("/conversations")) {
      if (requestMethod.equals(GET) && resourcePath.matches("\\/conversations\\/[a-z-]*")) {
        return new ResourceAccessType.ResourceAccessPathId();
      // POST /messages
      } else if (requestMethod.equals(POST) && resourcePath.matches("\\/conversations")) {
        return new ResourceAccessType.ResourceAccessBodyId();
      } else {
        throw new NotFoundException("Resource URL not found when authorizing messages request");
      }

    } else if (resourcePath.startsWith("/messages")) {
      // GET /messages/{id}
      if (requestMethod.equals(GET) && resourcePath.matches("\\/messages\\/[a-z-]*")) {
        return new ResourceAccessType.ResourceAccessPathId();
      // POST /messages
      } else if (requestMethod.equals(POST) && resourcePath.matches("\\/messages")) {
        return new ResourceAccessType.ResourceAccessBodyId();
      } else {
        throw new NotFoundException("Resource URL not found when authorizing messages request");
      }
    } else if (resourcePath.startsWith("/users")) {
      // GET /users/{id}
      if (requestMethod.equals(GET) && resourcePath.matches("\\/users\\/[a-z-]*")) {
        return new ResourceAccessType.ResourceAccessPathId();
      } else if (requestMethod.equals(POST) && resourcePath.matches("\\/users")) {
        return new ResourceAccessType.ResourceAccessGeneral();
      } else {
        throw new NotFoundException("Resource URL not found when authorizing users request");
      }
    } else {
      throw new NotFoundException("Invalid resource type found in URL while authorizing");
    }
  }
}
