package com.am.telegram.groupstat.logic.assistant;

import com.am.telegram.groupstat.logic.group.GroupService;

public class GroupListAssistant {

  private final Assistant assistant;
  private final GroupService groupService;

  public GroupListAssistant(Assistant assistant, GroupService groupService) {
    this.assistant = assistant;
    this.groupService = groupService;
  }

  public void listGroups() {
    if (assistant.isAdmin() || assistant.hasReadAccess()) {
      assistant.provideMessageToUser("Groups: \n" + groupService.groups());
    }
  }
}
