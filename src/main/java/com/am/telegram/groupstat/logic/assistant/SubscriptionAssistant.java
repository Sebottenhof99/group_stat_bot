package com.am.telegram.groupstat.logic.assistant;

import com.am.telegram.groupstat.logic.user.UserDTO;

public class SubscriptionAssistant {

  private final Assistant assistant;

  public SubscriptionAssistant(Assistant assistant) {
    this.assistant = assistant;
  }

  public void subscribe() {
    if (assistant.isAdmin() || assistant.hasReadAccess()) {
      UserDTO userDTO = assistant.getUserDTO();
      userDTO.setSubscribed(true);
      assistant.provideMessageToUser(
          "You are subscribed now to a monthly report of group dynamics");
    }
  }

  public void unsubscribe() {
    if (assistant.isAdmin() || assistant.hasReadAccess()) {
      UserDTO userDTO = assistant.getUserDTO();
      userDTO.setSubscribed(false);
      assistant.provideMessageToUser("No reports anymore");
    }
  }
}
