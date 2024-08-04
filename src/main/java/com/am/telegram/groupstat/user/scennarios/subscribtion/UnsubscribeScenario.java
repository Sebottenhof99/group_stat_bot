package com.am.telegram.groupstat.user.scennarios.subscribtion;

import com.am.telegram.groupstat.user.assistant.Assistant;
import com.am.telegram.groupstat.user.assistant.SubscriptionAssistant;
import com.am.telegram.groupstat.user.scennarios.Scenario;
import com.am.telegram.groupstat.user.user.UserService;

public class UnsubscribeScenario implements Scenario {
  private final Assistant assistant;
  private final UserService userService;

  public UnsubscribeScenario(Assistant assistant, UserService userService) {
    this.assistant = assistant;
    this.userService = userService;
  }

  @Override
  public void execute(long chatId) {
    new SubscriptionAssistant(assistant).unsubscribe();
    userService.save(assistant.getUserDTO());
  }
}
