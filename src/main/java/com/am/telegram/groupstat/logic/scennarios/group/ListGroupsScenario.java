package com.am.telegram.groupstat.logic.scennarios.group;

import com.am.telegram.groupstat.logic.assistant.Assistant;
import com.am.telegram.groupstat.logic.assistant.GroupListAssistant;
import com.am.telegram.groupstat.logic.group.GroupService;
import com.am.telegram.groupstat.logic.scennarios.Scenario;

public class ListGroupsScenario implements Scenario {

  private final Assistant assistant;
  private final GroupService groupService;

  public ListGroupsScenario(Assistant assistant, GroupService groupService) {
    this.assistant = assistant;
    this.groupService = groupService;
  }

  @Override
  public void execute(long chatId) {
    new GroupListAssistant(assistant, groupService).listGroups();
  }
}
