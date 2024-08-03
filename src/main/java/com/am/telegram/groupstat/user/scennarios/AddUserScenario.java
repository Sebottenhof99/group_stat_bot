package com.am.telegram.groupstat.user.scennarios;

import com.am.telegram.groupstat.AssistantService;
import com.am.telegram.groupstat.user.Assistant;
import com.am.telegram.groupstat.user.UserManagementPanel;
import com.am.telegram.groupstat.user.operations.Operations;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;

public class AddUserScenario implements Scenario {

  private final Assistant assistant;
  private final TelegramBot bot;
  private final AssistantService assistantService;
  private final UserManagementPanel userManagementPanel = new UserManagementPanel();

  public AddUserScenario(
      Assistant assistant, TelegramBot telegram, AssistantService assistantService) {
    this.assistant = assistant;
    this.bot = telegram;
    this.assistantService = assistantService;
  }

  @Override
  public void execute(long chatId) {

    if (!assistant.isAdmin()) {
      bot.execute(new SendMessage(chatId, "You have no permissions for this operation"));
      return;
    }

    if (assistant.lastGivenAnswer() == null || assistant.lastGivenAnswer().isEmpty()) {
      assistant.memorizeLastActiveOperation(Operations.ADD_USER);
      bot.execute(new SendMessage(chatId, "Please, provide username"));
    } else {
      // TODO if user already exists
      // TODO check before persist
      assistantService.save(userManagementPanel.createUser(assistant.lastGivenAnswer()));
      bot.execute(
          new SendMessage(
                  chatId, "User with nickname " + assistant.lastGivenAnswer() + " has been added")
              .replyMarkup(assistant.availableOperations()));
      assistant.memorizeLastActiveOperation(Operations.EMPTY);
      assistant.memorizeLastGivenAnswer(null);
    }
    assistantService.save(assistant);
  }
}
