package com.am.telegram.groupstat.user.scennarios;

import com.am.telegram.groupstat.AssistantService;
import com.am.telegram.groupstat.user.Assistant;
import com.am.telegram.groupstat.user.operations.Operations;
import com.am.telegram.groupstat.user.report.ReportService;
import com.pengrad.telegrambot.TelegramBot;

public class ScenarioFactory {

    private final TelegramBot bot;
    private final AssistantService assistantService;
    private final ReportService reportService;

    public ScenarioFactory(TelegramBot bot, AssistantService assistantService, ReportService reportService) {
        this.bot = bot;
        this.assistantService = assistantService;
        this.reportService = reportService;
    }


    public Scenario selectScenario(Assistant assistant, Operations operations) {

        switch (operations) {
            case EMPTY -> {

            }
            case GET_CURRENT_REPORT -> {
                return new GenerateReportScenario(bot, reportService);
            }

            case SUBSCRIBE -> {
                return new SubscribeScenario(assistant, bot, assistantService);
            }

            case UNSUBSCRIBE -> {
                return new UnsubscribeScenario(assistant, bot, assistantService);
            }

            case ADD_USER -> {
                return new AddUserScenario(assistant, bot, assistantService);
            }

            case REMOVE_USER -> {
            }

            case ADD_NEW_GROUP -> {
            }

            case REMOVE_GROUP -> {
            }

            case ADD_ADMIN -> {
                return new AddAdminScenario(assistant, bot, assistantService);
            }

            case LIST_ADMINS -> {
                return new ListAdminsScenario(assistant, bot, assistantService);
            }

            case LIST_USERS -> {
                return new ListUsersScenario(assistant, bot, assistantService);
            }
        }
        throw new RuntimeException();
    }

}
