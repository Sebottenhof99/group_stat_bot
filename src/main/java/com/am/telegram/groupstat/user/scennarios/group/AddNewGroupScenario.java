package com.am.telegram.groupstat.user.scennarios.group;

import com.am.telegram.groupstat.user.assistant.Assistant;
import com.am.telegram.groupstat.user.assistant.AssistantService;
import com.am.telegram.groupstat.user.assistant.GroupAssistant;
import com.am.telegram.groupstat.user.group.GroupDTO;
import com.am.telegram.groupstat.user.group.GroupManagementService;
import com.am.telegram.groupstat.user.scennarios.Scenario;
import com.pengrad.telegrambot.TelegramBot;
import java.util.Optional;

public class AddNewGroupScenario implements Scenario {

  private final Assistant assistant;
  private final AssistantService assistantService;
  private final GroupManagementService groupManagementService;
  private final TelegramBot bot;

  public AddNewGroupScenario(
      Assistant assistant,
      AssistantService assistantService,
      GroupManagementService groupManagementService,
      TelegramBot bot) {
    this.assistant = assistant;
    this.assistantService = assistantService;
    this.groupManagementService = groupManagementService;
    this.bot = bot;
  }

  @Override
  public void execute(long chatId) {
    Optional<GroupDTO> groupDTO = new GroupAssistant(assistant, bot).addNewGroup();
    groupDTO.ifPresent(groupManagementService::save);
    assistantService.save(assistant);
  }
}
