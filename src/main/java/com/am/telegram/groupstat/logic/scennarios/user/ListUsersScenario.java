package com.am.telegram.groupstat.logic.scennarios.user;

import com.am.telegram.groupstat.logic.assistant.Assistant;
import com.am.telegram.groupstat.logic.assistant.specialization.UserListAssistant;
import com.am.telegram.groupstat.logic.scennarios.Scenario;
import com.am.telegram.groupstat.logic.user.UserService;

public class ListUsersScenario implements Scenario {

  private final Assistant assistant;

  private final UserService userService;

  public ListUsersScenario(Assistant assistant, UserService userService) {
    this.assistant = assistant;
    this.userService = userService;
  }

  @Override
  public void execute(long chatId) {
    new UserListAssistant(assistant, userService).listUsers();
  }
}
