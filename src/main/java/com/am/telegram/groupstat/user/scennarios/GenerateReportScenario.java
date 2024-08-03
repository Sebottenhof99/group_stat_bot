package com.am.telegram.groupstat.user.scennarios;

import com.am.telegram.groupstat.user.report.ReportService;
import com.am.telegram.groupstat.user.report.concurrency.ReportSubscriber;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;

public class GenerateReportScenario implements Scenario {

    private final TelegramBot bot;
    private final ReportService reportService;

    public GenerateReportScenario(TelegramBot bot, ReportService reportService) {
        this.bot = bot;
        this.reportService = reportService;
    }

    @Override
    public void execute(long chatId) {
        try {
            System.out.println("try to get report");
            reportService.subscribe(new ReportSubscriber(chatId, bot));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        bot.execute(new SendMessage(chatId, "Report generation is in progress. It can take several minutes." +
                "As soon as report has been generated you will receive a copy of it"));

    }
}