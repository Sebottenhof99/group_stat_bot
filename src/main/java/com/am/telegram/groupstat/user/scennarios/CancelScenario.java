package com.am.telegram.groupstat.user.scennarios;

import com.am.telegram.groupstat.user.assistant.Assistant;
import com.am.telegram.groupstat.user.assistant.AssistantService;
import com.am.telegram.groupstat.user.operations.Operations;

public class CancelScenario implements Scenario {

  private final Assistant assistant;
  private final AssistantService assistantService;

  public CancelScenario(Assistant assistant, AssistantService assistantService) {
    this.assistant = assistant;
    this.assistantService = assistantService;
  }

  @Override
  public void execute(long chatId) {
    assistant.memorizeLastActiveOperation(Operations.EMPTY);
    assistant.memorizeLastGivenAnswer(null);
    assistant.provideMessageToUser("Previous operation was cancelled.");
    assistantService.save(assistant);
  }
}
