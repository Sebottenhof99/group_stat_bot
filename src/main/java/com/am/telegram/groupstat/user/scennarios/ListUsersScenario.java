package com.am.telegram.groupstat.user.scennarios;

import com.am.telegram.groupstat.AssistantService;
import com.am.telegram.groupstat.user.Assistant;
import com.am.telegram.groupstat.user.Assistants;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;

public class ListUsersScenario implements Scenario {

    private final Assistant assistant;
    private final TelegramBot bot;
    private final AssistantService assistantService;

    public ListUsersScenario(Assistant assistant, TelegramBot bot, AssistantService assistantService) {
        this.assistant = assistant;
        this.bot = bot;
        this.assistantService = assistantService;
    }

    @Override
    public void execute(long chatId) {
        if (assistant.isAdmin()) {
            Assistants assistants = assistantService.users();
            bot.execute(new SendMessage(chatId, "Regular users: \n" + assistants.toStringList()));
        }
    }
}
