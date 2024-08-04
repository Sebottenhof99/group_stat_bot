package com.am.telegram.groupstat.user.assistant;

import com.am.telegram.groupstat.user.user.UserService;

public class ListAssistant {

  private final Assistant assistant;
  ;
  private final UserService userService;

  public ListAssistant(Assistant assistant, UserService userService) {
    this.assistant = assistant;
    this.userService = userService;
  }

  public void listAdmins(long chatId) {
    if (assistant.isAdmin()) {
      assistant.provideMessageToUser("Admins: \n" + userService.adminNames());
    }
  }

  public void listUsers(long chatId) {
    if (assistant.isAdmin()) {
      assistant.provideMessageToUser("Regular users: \n" + userService.regularUserNames());
    }
  }
}
