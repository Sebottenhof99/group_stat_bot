package com.am.telegram.groupstat.logic.assistant;

public class AssistantException extends RuntimeException {
  public AssistantException(String message, Exception exception) {
    super(message, exception);
  }
}
