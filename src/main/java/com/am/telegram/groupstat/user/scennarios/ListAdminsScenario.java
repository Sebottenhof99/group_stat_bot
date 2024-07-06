package com.am.telegram.groupstat.user.scennarios;

import com.am.telegram.groupstat.AssistantService;
import com.am.telegram.groupstat.user.Assistants;
import com.am.telegram.groupstat.user.Assistant;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;

public class ListAdminsScenario implements Scenario {

    private final Assistant assistant;
    private final TelegramBot bot;
    private final AssistantService assistantService;


    public ListAdminsScenario(Assistant assistant, TelegramBot bot, AssistantService assistantService) {
        this.assistant = assistant;
        this.bot = bot;
        this.assistantService = assistantService;
    }

    @Override
    public void execute(long chatId) {
        if (assistant.isAdmin()) {
            Assistants assistants = assistantService.admins();
            bot.execute(new SendMessage(chatId, "Admins: \n" + assistants.toStringList()));
        }
    }
}
