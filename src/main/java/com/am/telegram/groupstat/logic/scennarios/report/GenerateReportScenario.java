package com.am.telegram.groupstat.logic.scennarios.report;

import com.am.telegram.groupstat.logic.assistant.Assistant;
import com.am.telegram.groupstat.logic.report.ReportService;
import com.am.telegram.groupstat.logic.report.concurrency.ReportSubscriber;
import com.am.telegram.groupstat.logic.scennarios.Scenario;
import com.pengrad.telegrambot.TelegramBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenerateReportScenario implements Scenario {

  private static final Logger log = LoggerFactory.getLogger(GenerateReportScenario.class);

  private final Assistant assistant;
  private final TelegramBot bot;
  private final ReportService reportService;

  public GenerateReportScenario(Assistant assistant, TelegramBot bot, ReportService reportService) {
    this.assistant = assistant;
    this.bot = bot;
    this.reportService = reportService;
  }

  @Override
  public void execute(long chatId) {
    try {
      log.info("Trying to get report");
      reportService.subscribe(new ReportSubscriber(chatId, bot));
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    assistant.provideMessageToUser(
        "Report generation is in progress. It can take several minutes."
            + "As soon as report has been generated you will receive a copy of it");
  }
}
