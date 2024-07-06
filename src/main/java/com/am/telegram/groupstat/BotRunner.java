package com.am.telegram.groupstat;

import com.am.telegram.groupstat.user.report.ReportService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.GetChatMemberCount;
import com.pengrad.telegrambot.response.GetChatMemberCountResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

public class BotRunner implements ApplicationRunner {

    private static Logger log = LogManager.getLogger(BotRunner.class);

    private final TelegramBot bot;
    private final StatUpdateListener updateListener;
    private final ReportService reportService;

    public BotRunner(TelegramBot bot, StatUpdateListener updateListener, ReportService reportService) {
        this.bot = bot;
        this.updateListener = updateListener;
        this.reportService = reportService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
           // bot.setUpdatesListener(updateListener);
        reportService.createCurrentReport();
   //    GetChatMemberCountResponse execute = bot.execute(new GetChatMemberCount("@urokimeditacii_norilsk"));
     //  System.out.println(execute.count());
    }
}
