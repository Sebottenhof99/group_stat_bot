package com.am.telegram.groupstat.logic.user;

public class UserException extends RuntimeException {
  public UserException(String message, Exception e) {
    super(message, e);
  }
}
