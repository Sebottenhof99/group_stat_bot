package com.am.telegram.groupstat.logic.group;

public class GroupException extends RuntimeException {
  public GroupException(String message, Exception e) {
    super(message, e);
  }
}
