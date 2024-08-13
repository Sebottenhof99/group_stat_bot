package com.am.telegram.groupstat;

import com.am.telegram.groupstat.logic.report.ReportService;
import com.pengrad.telegrambot.TelegramBot;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GroupStatApplication {

  public static void main(String[] args) {
    SpringApplication.run(GroupStatApplication.class, args);
  }

  @Bean
  public ApplicationRunner commandLineRunner(
      TelegramBot bot, StatUpdateListener statUpdateListener, ReportService reportService) {
    return new BotRunner(bot, statUpdateListener, reportService);
  }
}
