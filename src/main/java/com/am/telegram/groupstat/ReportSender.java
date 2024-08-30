package com.am.telegram.groupstat;

import com.am.telegram.groupstat.logic.report.ReportService;
import com.am.telegram.groupstat.logic.report.concurrency.ReportSubscriber;
import com.am.telegram.groupstat.logic.user.UserDTO;
import com.am.telegram.groupstat.logic.user.UserService;
import com.pengrad.telegrambot.TelegramBot;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

public class ReportSender {

  private static final Logger log = LoggerFactory.getLogger(ReportSender.class);
  private final ReportService reportService;
  private final UserService userService;
  private final TelegramBot bot;

  public ReportSender(ReportService reportService, UserService userService, TelegramBot bot) {
    this.reportService = reportService;
    this.userService = userService;
    this.bot = bot;
  }

  @Scheduled(cron = "0 10 28-31 * *")
  public void sendReport() {
    if (!isLastDayOfMonth()) {
      log.info("Today is not the last day of the month");
      return;
    }

    List<UserDTO> subscribers = userService.getSubscriber();
    log.info("Generate report for {} subscribers due to the last day of the month",
        subscribers.size());
    subscribers.forEach(
        subscriber -> {
          try {
            reportService.subscribe(new ReportSubscriber(subscriber.getChatId(), bot));
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
          }
        });
  }

  private boolean isLastDayOfMonth() {
    LocalDate localDate = LocalDate.now();
    YearMonth yearMonth = YearMonth.from(localDate);
    return localDate.getDayOfMonth() == yearMonth.atEndOfMonth().getDayOfMonth();
  }
}
