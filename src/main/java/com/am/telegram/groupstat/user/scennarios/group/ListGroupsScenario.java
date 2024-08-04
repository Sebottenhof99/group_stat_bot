package com.am.telegram.groupstat.user.scennarios.group;

import com.am.telegram.groupstat.user.assistant.Assistant;
import com.am.telegram.groupstat.user.assistant.GroupListAssistant;
import com.am.telegram.groupstat.user.group.GroupManagementService;
import com.am.telegram.groupstat.user.scennarios.Scenario;

public class ListGroupsScenario implements Scenario {

  private final Assistant assistant;
  private final GroupManagementService groupManagementService;

  public ListGroupsScenario(Assistant assistant, GroupManagementService groupManagementService) {
    this.assistant = assistant;
    this.groupManagementService = groupManagementService;
  }

  @Override
  public void execute(long chatId) {
    new GroupListAssistant(assistant, groupManagementService).listGroups();
  }
}
