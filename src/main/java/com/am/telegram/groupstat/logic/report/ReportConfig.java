package com.am.telegram.groupstat.logic.report;

import com.am.telegram.groupstat.logic.group.GroupRepository;
import com.am.telegram.groupstat.logic.report.statistic.StatisticRepository;
import com.am.telegram.groupstat.logic.report.statistic.StatisticService;
import com.pengrad.telegrambot.TelegramBot;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReportConfig {

  @Bean
  public StatisticRepository statisticRepository(TelegramBot bot) {
    return new StatisticRepository(bot);
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
