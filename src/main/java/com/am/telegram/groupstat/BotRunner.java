package com.am.telegram.groupstat;

import com.am.telegram.groupstat.logic.report.ReportService;
import com.pengrad.telegrambot.TelegramBot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

public class BotRunner implements ApplicationRunner {

  private static Logger log = LogManager.getLogger(BotRunner.class);

  private final TelegramBot bot;
  private final StatUpdateListener updateListener;
  private final ReportService reportService;

  public BotRunner(
      TelegramBot bot, StatUpdateListener updateListener, ReportService reportService) {
    this.bot = bot;
    this.updateListener = updateListener;
    this.reportService = reportService;
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    bot.setUpdatesListener(updateListener);
  }
}
