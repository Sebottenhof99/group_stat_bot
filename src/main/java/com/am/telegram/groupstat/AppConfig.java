package com.am.telegram.groupstat;

import com.am.telegram.groupstat.user.assistant.AssistantRepository;
import com.am.telegram.groupstat.user.assistant.AssistantService;
import com.am.telegram.groupstat.user.group.GroupManagementRepository;
import com.am.telegram.groupstat.user.group.GroupManagementService;
import com.am.telegram.groupstat.user.report.ReportConfig;
import com.am.telegram.groupstat.user.report.ReportService;
import com.am.telegram.groupstat.user.scennarios.ScenarioFactory;
import com.am.telegram.groupstat.user.user.UserRepository;
import com.am.telegram.groupstat.user.user.UserService;
import com.pengrad.telegrambot.TelegramBot;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ReportConfig.class)
public class AppConfig {

  @Bean
  public UserRepository userRepository() {
    return new UserRepository();
  }

  @Bean
  public UserService userService(DataSource ds, UserRepository userRepository) {
    return new UserService(ds, userRepository);
  }

  @Bean
  public AssistantRepository assistantRepository() {
    return new AssistantRepository();
  }

  @Bean
  public AssistantService assistantService(
      DataSource ds, UserRepository userRepository, AssistantRepository assistantRepository) {
    return new AssistantService(ds, userRepository, assistantRepository);
  }

  @Bean
  public GroupManagementRepository groupManagementRepository() {
    return new GroupManagementRepository();
  }

  @Bean
  public GroupManagementService groupManagementService(
      DataSource ds, GroupManagementRepository groupManagementRepository) {
    return new GroupManagementService(ds, groupManagementRepository);
  }

  @Bean
  public ScenarioFactory scenarioFactory(
      TelegramBot bot,
      AssistantService assistantService,
      UserService userService,
      ReportService reportService,
      GroupManagementService groupManagementService) {
    return new ScenarioFactory(
        bot, assistantService, userService, reportService, groupManagementService);
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
