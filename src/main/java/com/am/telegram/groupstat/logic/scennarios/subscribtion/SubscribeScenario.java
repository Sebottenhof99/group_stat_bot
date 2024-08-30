package com.am.telegram.groupstat.logic.scennarios.subscribtion;

import com.am.telegram.groupstat.logic.assistant.Assistant;
import com.am.telegram.groupstat.logic.assistant.specialization.SubscriptionAssistant;
import com.am.telegram.groupstat.logic.scennarios.Scenario;
import com.am.telegram.groupstat.logic.user.UserService;

public class SubscribeScenario implements Scenario {
  private final Assistant assistant;
  private final UserService userService;

  public SubscribeScenario(Assistant assistant, UserService userService) {
    this.assistant = assistant;
    this.userService = userService;
  }

  @Override
  public void execute(long chatId) {
    new SubscriptionAssistant(assistant).subscribe();
    assistant.getUserDTO().setChatId(chatId);
    userService.save(assistant.getUserDTO());
  }
}
