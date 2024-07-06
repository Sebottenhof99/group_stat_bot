package com.am.telegram.groupstat;

import com.am.telegram.groupstat.user.Assistant;

import com.am.telegram.groupstat.user.operations.Operations;
import com.am.telegram.groupstat.user.scennarios.ScenarioFactory;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class StatUpdateListener implements UpdatesListener {

    private static final Logger log = LoggerFactory.getLogger(StatUpdateListener.class);
    private final TelegramBot bot;
    private final AssistantService assistantService;
    private final ScenarioFactory scenarioFactory;

    public StatUpdateListener(TelegramBot bot, AssistantService assistantService, ScenarioFactory scenarioFactory) {
        this.bot = bot;
        this.assistantService = assistantService;
        this.scenarioFactory = scenarioFactory;
    }

    @Override
    public int process(List<Update> updates) {

        for (Update update : updates) {

            Optional<Assistant> assistant = assistantService.assistantOf(update.message().chat().username());
            if (assistant.isEmpty()) {
                bot.execute(new SendMessage(update.message().chat().id(), "No permissions!"));
                break;
            }
            if (update.message()== null || update.message().text() == null){
                bot.execute(new SendMessage(update.message().chat().id(), "Unsupported communication medium"));
                break;
            }

            if ("/start".equals(update.message().text())) {
                bot.execute(new SendMessage(update.message().chat().id(), "Please selection an option").replyMarkup(assistant.get().availableOperations()));
            } else {
                try {
                    assistant.get().memorizeLastGivenAnswer(update.message().text());
                    Operations operations = assistant.get().lastActiveOperation() == Operations.EMPTY ?
                            Operations.valueOf(update.message().text()) : assistant.get().lastActiveOperation();
                    scenarioFactory.selectScenario(assistant.get(), operations).execute(update.message().chat().id());

                } catch (Exception e) {
                    log.error("Unexpected error: ", e);
                    bot.execute(new SendMessage(update.message().chat().id(), "Unsupported operation: Please choice from given operations!"));
                }
            }
        }
        return CONFIRMED_UPDATES_ALL;
    }
}
