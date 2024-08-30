package com.am.telegram.groupstat.logic.scennarios.group;

import com.am.telegram.groupstat.logic.assistant.Assistant;
import com.am.telegram.groupstat.logic.assistant.AssistantService;
import com.am.telegram.groupstat.logic.assistant.GroupAssistant;
import com.am.telegram.groupstat.logic.group.GroupDTO;
import com.am.telegram.groupstat.logic.group.GroupService;
import com.am.telegram.groupstat.logic.scennarios.Scenario;
import com.pengrad.telegrambot.TelegramBot;
import java.util.Optional;

public class AddNewGroupScenario implements Scenario {

  private final Assistant assistant;
  private final AssistantService assistantService;
  private final GroupService groupService;
  private final TelegramBot bot;

  public AddNewGroupScenario(
      Assistant assistant,
      AssistantService assistantService,
      GroupService groupService,
      TelegramBot bot) {
    this.assistant = assistant;
    this.assistantService = assistantService;
    this.groupService = groupService;
    this.bot = bot;
  }

  @Override
  public void execute(long chatId) {
    Optional<GroupDTO> groupDTO = new GroupAssistant(assistant, bot).addNewGroup();
    groupDTO.ifPresent(groupService::save);
    assistantService.save(assistant);
  }
}
