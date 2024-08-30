package com.am.telegram.groupstat;

import com.am.telegram.groupstat.logic.assistant.AssistantRepository;
import com.am.telegram.groupstat.logic.assistant.AssistantService;
import com.am.telegram.groupstat.logic.group.GroupRepository;
import com.am.telegram.groupstat.logic.group.GroupService;
import com.am.telegram.groupstat.logic.report.ReportConfig;
import com.am.telegram.groupstat.logic.report.ReportService;
import com.am.telegram.groupstat.logic.scennarios.ScenarioFactory;
import com.am.telegram.groupstat.logic.user.UserRepository;
import com.am.telegram.groupstat.logic.user.UserService;
import com.pengrad.telegrambot.TelegramBot;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@Import(ReportConfig.class)
@EnableScheduling
public class AppConfig {

  @Bean
  public GroupRepository groupRepository() {
    return new GroupRepository();
  }

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
  public GroupRepository groupManagementRepository() {
    return new GroupRepository();
  }

  @Bean
  public GroupService groupManagementService(DataSource ds, GroupRepository groupRepository) {
    return new GroupService(ds, groupRepository);
  }

  @Bean
  public ScenarioFactory scenarioFactory(
      TelegramBot bot,
      AssistantService assistantService,
      UserService userService,
      ReportService reportService,
      GroupService groupService) {
    return new ScenarioFactory(bot, assistantService, userService, reportService, groupService);
  }

  @Bean(destroyMethod = "shutdown")
  public TelegramBot telegramBot(Environment env) {
    return new TelegramBot(env.getRequiredProperty("telegram.bot.token"));
  }

  @Bean
  public StatController statUpdateListener(
      TelegramBot bot, AssistantService assistantService, ScenarioFactory scenarioFactory) {
    return new StatController(bot, assistantService, scenarioFactory);
  }

  @Bean
  public ReportSender reportSender(
      ReportService reportService, UserService userService, TelegramBot bot) {
    return new ReportSender(reportService, userService, bot);
  }
}
