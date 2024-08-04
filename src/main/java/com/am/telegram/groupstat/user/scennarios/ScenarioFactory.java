package com.am.telegram.groupstat.user.scennarios;

import com.am.telegram.groupstat.user.assistant.Assistant;
import com.am.telegram.groupstat.user.assistant.AssistantService;
import com.am.telegram.groupstat.user.group.GroupManagementService;
import com.am.telegram.groupstat.user.operations.Operations;
import com.am.telegram.groupstat.user.report.ReportService;
import com.am.telegram.groupstat.user.scennarios.group.ListGroupsScenario;
import com.am.telegram.groupstat.user.scennarios.report.GenerateReportScenario;
import com.am.telegram.groupstat.user.scennarios.subscribtion.SubscribeScenario;
import com.am.telegram.groupstat.user.scennarios.subscribtion.UnsubscribeScenario;
import com.am.telegram.groupstat.user.scennarios.user.*;
import com.am.telegram.groupstat.user.user.UserService;
import com.pengrad.telegrambot.TelegramBot;

public class ScenarioFactory {

  private final TelegramBot bot;
  private final AssistantService assistantService;
  private final ReportService reportService;
  private final UserService userService;
  private final GroupManagementService groupManagementService;

  public ScenarioFactory(
      TelegramBot bot,
      AssistantService assistantService,
      UserService userService,
      ReportService reportService,
      GroupManagementService groupManagementService) {
    this.bot = bot;
    this.assistantService = assistantService;
    this.reportService = reportService;
    this.userService = userService;
    this.groupManagementService = groupManagementService;
  }

  public Scenario selectScenario(Assistant assistant, Operations operations) {

    switch (operations) {
      case EMPTY -> {}
      case GET_CURRENT_REPORT -> {
        return new GenerateReportScenario(bot, reportService);
      }

      case SUBSCRIBE -> {
        return new SubscribeScenario(assistant, userService);
      }

      case UNSUBSCRIBE -> {
        return new UnsubscribeScenario(assistant, userService);
      }

      case ADD_USER -> {
        return new AddUserScenario(assistant, assistantService, userService);
      }

      case ADD_ADMIN -> {
        return new AddAdminScenario(assistant, assistantService, userService);
      }

      case REMOVE_USER -> {
        return new RemoveUserScenario(assistant, assistantService, userService);
      }

      case LIST_GROUPS -> {
        return new ListGroupsScenario(assistant, groupManagementService);
      }

      case ADD_NEW_GROUP -> {}

      case PAUSE_GROUP -> {}

      case RESUME_GROUP -> {}

      case LIST_ADMINS -> {
        return new ListAdminsScenario(assistant, userService);
      }

      case LIST_USERS -> {
        return new ListUsersScenario(assistant, userService);
      }
      case CANCEL -> {
        return new CancelScenario(assistant, assistantService);
      }
    }
    throw new RuntimeException();
  }
}
