package com.am.telegram.groupstat.user.scennarios;

import com.am.telegram.groupstat.AssistantService;
import com.am.telegram.groupstat.user.Assistant;
import com.am.telegram.groupstat.user.UserManagementPanel;
import com.am.telegram.groupstat.user.operations.Operations;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;

public class AddAdminScenario implements Scenario {

  private final Assistant assistant;
  private final TelegramBot bot;
  private final AssistantService assistantService;
  private final UserManagementPanel userManagementPanel = new UserManagementPanel();

  public AddAdminScenario(Assistant assistant, TelegramBot bot, AssistantService assistantService) {
    this.assistant = assistant;
    this.bot = bot;
    this.assistantService = assistantService;
  }

  @Override
  public void execute(long chatId) {
    if (!assistant.isAdmin()) {
      bot.execute(new SendMessage(chatId, "You have no permissions for this operation"));
      return;
    }

    if (assistant.lastGivenAnswer() == null || assistant.lastGivenAnswer().isEmpty()) {
      assistant.memorizeLastActiveOperation(Operations.ADD_ADMIN);
      bot.execute(new SendMessage(chatId, "Please, provide username"));
    } else {
      // TODO if user already exists
      // TODO check before persist
      assistantService.save(userManagementPanel.createAdmin(assistant.lastGivenAnswer()));
      bot.execute(
          new SendMessage(
                  chatId, "Admin with nickname " + assistant.lastGivenAnswer() + " has been added")
              .replyMarkup(assistant.availableOperations()));
      assistant.memorizeLastActiveOperation(Operations.EMPTY);
      assistant.memorizeLastGivenAnswer(null);
    }
    assistantService.save(assistant);
  }
}
