package com.am.telegram.groupstat.logic.scennarios;

import com.am.telegram.groupstat.logic.assistant.Assistant;
import com.am.telegram.groupstat.logic.assistant.AssistantService;
import com.am.telegram.groupstat.logic.group.GroupService;
import com.am.telegram.groupstat.logic.operations.Operations;
import com.am.telegram.groupstat.logic.report.ReportService;
import com.am.telegram.groupstat.logic.scennarios.group.AddNewGroupScenario;
import com.am.telegram.groupstat.logic.scennarios.group.ListGroupsScenario;
import com.am.telegram.groupstat.logic.scennarios.report.GenerateReportScenario;
import com.am.telegram.groupstat.logic.scennarios.subscribtion.SubscribeScenario;
import com.am.telegram.groupstat.logic.scennarios.subscribtion.UnsubscribeScenario;
import com.am.telegram.groupstat.logic.scennarios.user.*;
import com.am.telegram.groupstat.logic.user.UserService;
import com.pengrad.telegrambot.TelegramBot;

public class ScenarioFactory {

  private final TelegramBot bot;
  private final AssistantService assistantService;
  private final ReportService reportService;
  private final UserService userService;
  private final GroupService groupService;

  public ScenarioFactory(
      TelegramBot bot,
      AssistantService assistantService,
      UserService userService,
      ReportService reportService,
      GroupService groupService) {
    this.bot = bot;
    this.assistantService = assistantService;
    this.reportService = reportService;
    this.userService = userService;
    this.groupService = groupService;
  }

  public Scenario selectScenario(Assistant assistant, Operations operations) {

    switch (operations) {
      case EMPTY -> {}
      case GET_CURRENT_REPORT -> {
        return new GenerateReportScenario(assistant, bot, reportService);
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
        return new ListGroupsScenario(assistant, groupService);
      }

      case ADD_NEW_GROUP -> {
        return new AddNewGroupScenario(assistant, assistantService, groupService, bot);
      }

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
