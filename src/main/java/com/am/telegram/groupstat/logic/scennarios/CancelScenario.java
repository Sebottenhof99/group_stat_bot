package com.am.telegram.groupstat.logic.scennarios;

import com.am.telegram.groupstat.logic.Operations;
import com.am.telegram.groupstat.logic.assistant.Assistant;
import com.am.telegram.groupstat.logic.assistant.AssistantService;

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
