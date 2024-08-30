package com.am.telegram.groupstat.logic.report;

import com.am.telegram.groupstat.logic.group.GroupRepository;
import com.am.telegram.groupstat.logic.report.statistic.StatisticRepository;
import com.am.telegram.groupstat.logic.report.statistic.StatisticService;
import com.pengrad.telegrambot.TelegramBot;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReportConfig {

  @Bean(destroyMethod = "shutdown")
  public ScheduledExecutorService scheduledExecutorService() {
    return Executors.newSingleThreadScheduledExecutor();
  }

  @Bean
  public StatisticRepository statisticRepository(
      TelegramBot bot, ScheduledExecutorService scheduledExecutorService) {
    return new StatisticRepository(bot, scheduledExecutorService);
  }

  @Bean
  public StatisticService statisticService(
      GroupRepository groupRepository, StatisticRepository statisticRepository, DataSource ds) {
    return new StatisticService(groupRepository, statisticRepository, ds);
  }

  @Bean
  public ReportService reportService(StatisticService statisticServices) {
    return new ReportService(statisticServices);
  }
}
