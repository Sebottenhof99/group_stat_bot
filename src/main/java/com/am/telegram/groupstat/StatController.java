package com.am.telegram.groupstat;

import static com.am.telegram.groupstat.logic.Operations.CANCEL;

import com.am.telegram.groupstat.logic.Operations;
import com.am.telegram.groupstat.logic.assistant.Assistant;
import com.am.telegram.groupstat.logic.assistant.AssistantService;
import com.am.telegram.groupstat.logic.scennarios.ScenarioFactory;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatController implements UpdatesListener {

  private static final Logger log = LoggerFactory.getLogger(StatController.class);
  private final TelegramBot bot;
  private final AssistantService assistantService;
  private final ScenarioFactory scenarioFactory;
  private final ExecutorService executor;

  public StatController(
      TelegramBot bot, AssistantService assistantService, ScenarioFactory scenarioFactory) {
    this.bot = bot;
    this.assistantService = assistantService;
    this.scenarioFactory = scenarioFactory;
    this.executor = Executors.newFixedThreadPool(6);
  }

  @Override
  public int process(List<Update> updates) {
    for (Update update : updates) {
      executor.submit(() -> processUpdate(update));
    }
    return CONFIRMED_UPDATES_ALL;
  }

  private void processUpdate(Update update) {
    log.info("Incoming update from chat {}", update.message().chat().id());
    Optional<Assistant> assistant =
        assistantService.assistantOf(update.message().chat().username());

    if (assistant.isEmpty()) {
      bot.execute(new SendMessage(update.message().chat().id(), "No permissions!"));
      return;
    }

    if (update.message() == null || update.message().text() == null) {
      bot.execute(
          new SendMessage(update.message().chat().id(), "Unsupported communication medium"));
      return;
    }

    if ("/start".equals(update.message().text())) {
      bot.execute(
          new SendMessage(update.message().chat().id(), "Please selection an option")
              .replyMarkup(assistant.get().availableOperations()));
    } else {

      if (CANCEL.name().equals(update.message().text())) {
        assistant.get().memorizeLastActiveOperation(CANCEL);
      }

      try {
        assistant.get().memorizeLastGivenAnswer(update.message().text());
        Operations operations =
            assistant.get().lastActiveOperation() == Operations.EMPTY
                ? Operations.valueOf(update.message().text())
                : assistant.get().lastActiveOperation();
        scenarioFactory
            .selectScenario(assistant.get(), operations)
            .execute(update.message().chat().id());
        bot.execute(
            new SendMessage(update.message().chat().id(), assistant.get().responseToUser())
                .replyMarkup(assistant.get().availableOperations()));
      } catch (Exception e) {
        log.error("Unexpected error: ", e);
        bot.execute(
            new SendMessage(
                update.message().chat().id(),
                "Unsupported operation: Please choice from given operations!"));
      }
    }
  }
}
