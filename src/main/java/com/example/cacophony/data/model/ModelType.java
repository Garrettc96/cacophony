package com.example.cacophony.data.model;

import com.example.cacophony.exception.ModelParseException;
import com.example.cacophony.exception.NotFoundException;

public enum ModelType {
  CONVERSATION,
  USER,
  MESSAGE;

  public static ModelType fromString(String modelTypeString) {
    return switch(modelTypeString.toLowerCase()) {
      case "chats": yield ModelType.CONVERSATION;
      case "conversations": yield ModelType.CONVERSATION;
      case "messages": yield ModelType.MESSAGE;
      case "users": yield ModelType.USER;
      default: throw new ModelParseException(String.format("Unable to detect model from modelTypeString: %s", modelTypeString));
    };
  }
}
