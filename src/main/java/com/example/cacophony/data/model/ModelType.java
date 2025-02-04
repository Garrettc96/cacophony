package com.example.cacophony.data.model;

import com.example.cacophony.exception.ModelParseException;
import com.example.cacophony.exception.NotFoundException;

public enum ModelType {
  CONVERSATION,
  USER,
  MESSAGE;

  public static ModelType fromString(String modelTypeString) {
    return switch(modelTypeString.toLowerCase()) {
      case "chat": yield ModelType.CONVERSATION;
      case "message": yield ModelType.MESSAGE;
      case "user": yield ModelType.USER;
      default: throw new ModelParseException(String.format("Unable to detect model from modelTypeString: %s", modelTypeString));
    };
  }
}
