package com.example.cacophony.security;

import com.example.cacophony.data.dto.CreateMessageRequest;
import com.example.cacophony.data.dto.ResourceAuthorizationBody;
import com.example.cacophony.data.model.ModelType;
import com.example.cacophony.data.model.User;
import com.example.cacophony.exception.NotFoundException;
import com.example.cacophony.service.ChatService;
import com.example.cacophony.service.MessageService;
import com.example.cacophony.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
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
  final ChatService chatService;
  final UserService userService;

  public ResourceAccessFilter(MessageService messageService, ChatService chatService, UserService userService) {
    this.messageService = messageService;
    this.chatService = chatService;
    this.userService = userService;
  }
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    if (request.getUserPrincipal() == null) {
      filterChain.doFilter(request, response);
      return;
    }
    HttpServletRequest requestCache = new ContentCachingRequestWrapper(request);
    String requestPath = request.getRequestURI();
    String userId = "";
    ResourceAccessType resourceAccessType = parseRequestToResourceAccessType(request, requestPath);
    AuthTypeAndId authTypeAndId = switch (resourceAccessType) {
      case ResourceAccessType.ResourceAccessPathId t ->
          parseAuthTypeAndIdFromRequest(requestPath);
      case ResourceAccessType.ResourceAccessBodyId t -> parseAuthTypeAndIdFromBody(requestCache, t);
      case ResourceAccessType.ResourceAccessGeneral t -> null;
    };
    if (canUserAccessAuthTypeAndId(userId, authTypeAndId)) {
      return;
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
  private boolean canUserAccessAuthTypeAndId(String userId, AuthTypeAndId authTypeAndId) {
    // This is only true if ResourceAccessType is ResourceAccessGeneral which means any user can access
    if (authTypeAndId == null) {
      return true;
    }
    return switch(authTypeAndId.resourceType()) {
      case USER -> userId.equals(authTypeAndId.id());
      case MESSAGE -> this.messageService.getMessage(authTypeAndId.id())
          .getConversation()
          .getMembers()
          .contains(
              User.fromId(UUID.fromString(userId)));
      case CONVERSATION -> this.chatService
          .getChat(authTypeAndId.id())
          .getConversation()
          .getMembers()
          .contains(User.fromId(UUID.fromString(userId)));
    };
  }

  private AuthTypeAndId parseAuthTypeAndIdFromRequest(String requestPath) {
    List<String> splitPath = Arrays.stream(requestPath.split("/")).toList();
    String id = splitPath.getLast();
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
