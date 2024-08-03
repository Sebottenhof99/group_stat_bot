package com.am.telegram.groupstat;

import com.am.telegram.groupstat.user.AssistantRepository;
import com.am.telegram.groupstat.user.report.ReportConfig;
import com.am.telegram.groupstat.user.report.ReportService;
import com.am.telegram.groupstat.user.scennarios.ScenarioFactory;
import com.pengrad.telegrambot.TelegramBot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ReportConfig.class)
public class AppConfig {

  @Bean
  public AssistantRepository userRepository() {
    return new AssistantRepository();
  }

  @Bean
  public AssistantService userService(AssistantRepository assistantRepository) {
    return new AssistantService(assistantRepository);
  }

  @Bean
  public ScenarioFactory scenarioFactory(
      TelegramBot bot, AssistantService assistantService, ReportService reportService) {
    return new ScenarioFactory(bot, assistantService, reportService);
  }

  @Bean(destroyMethod = "shutdown")
  public TelegramBot telegramBot() {
    return new TelegramBot("7222624015:AAGzaO-4U6dVAh4fat3FOmkx7RQmEhZg4AQ");
  }

  @Bean
  public StatUpdateListener statUpdateListener(
      TelegramBot bot, AssistantService assistantService, ScenarioFactory scenarioFactory) {
    return new StatUpdateListener(bot, assistantService, scenarioFactory);
  }
}
