package com.am.telegram.groupstat.user.scennarios;

import com.am.telegram.groupstat.AssistantService;
import com.am.telegram.groupstat.user.Assistant;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;

public class SubscribeScenario implements Scenario {
  private final Assistant assistant;
  private final TelegramBot bot;
  private final AssistantService assistantService;

  public SubscribeScenario(
      Assistant assistant, TelegramBot bot, AssistantService assistantService) {
    this.assistant = assistant;
    this.bot = bot;
    this.assistantService = assistantService;
  }

  @Override
  public void execute(long chatId) {
    if (assistant.isAdmin() || assistant.hasReadAccess()) {
      assistant.subscribeUser();
      assistantService.save(assistant);
      bot.execute(
          new SendMessage(chatId, "You are subscribed now to a monthly report of group dynamics")
              .replyMarkup(assistant.availableOperations()));
    }
  }
}
