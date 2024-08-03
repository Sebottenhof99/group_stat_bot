package com.am.telegram.groupstat.user;

public class UserManagementPanel {

  public AssistantDTO createUser(String userName) {
    AssistantDTO assistantDTO = new AssistantDTO();
    assistantDTO.setUserName(userName);
    assistantDTO.setAdmin(false);
    assistantDTO.setSubscribed(false);
    assistantDTO.setHasReadAccess(true);
    assistantDTO.setLastActiveOperation("EMPTY");
    return assistantDTO;
  }

  public AssistantDTO createAdmin(String username) {
    AssistantDTO user = createUser(username);
    user.setAdmin(true);
    return user;
  }
}
