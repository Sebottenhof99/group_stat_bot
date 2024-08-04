package com.am.telegram.groupstat.user.assistant;

import com.am.telegram.groupstat.user.user.UserService;

public class UserListAssistant {

  private final Assistant assistant;
  private final UserService userService;

  public UserListAssistant(Assistant assistant, UserService userService) {
    this.assistant = assistant;
    this.userService = userService;
  }

  public void listAdmins() {
    if (assistant.isAdmin()) {
      assistant.provideMessageToUser("Admins: \n" + userService.adminNames());
    }
  }

  public void listUsers() {
    if (assistant.isAdmin()) {
      assistant.provideMessageToUser("Regular users: \n" + userService.regularUserNames());
    }
  }
}
