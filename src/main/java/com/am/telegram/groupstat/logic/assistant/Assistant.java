package com.am.telegram.groupstat.logic.assistant;

import static com.am.telegram.groupstat.logic.Operations.*;

import com.am.telegram.groupstat.logic.Operations;
import com.am.telegram.groupstat.logic.user.UserDTO;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;

public class Assistant {

  private final UserDTO userDTO;
  private final AssistantDTO assistantDTO;
  private String responseToUser;

  public Assistant(UserDTO userDTO, AssistantDTO assistantDTO) {
    this.userDTO = userDTO;
    this.assistantDTO = assistantDTO;
  }

  public Keyboard availableOperations() {

    ReplyKeyboardMarkup replyKeyboardMarkup =
        new ReplyKeyboardMarkup("").resizeKeyboard(true).selective(true);

    if ((userDTO.isAdmin() || userDTO.isHasReadAccess()) && !isWaitingForAnswer()) {
      replyKeyboardMarkup.addRow(
          GET_CURRENT_REPORT.name(),
          userDTO.isSubscribed() ? UNSUBSCRIBE.name() : SUBSCRIBE.name());
      replyKeyboardMarkup.addRow(LIST_GROUPS.name());
    }

    if (userDTO.isAdmin()) {
      if (isWaitingForAnswer()) {
        replyKeyboardMarkup.addRow(CANCEL.name());
        return replyKeyboardMarkup;
      }

      String[] userManagement =
          new String[] {ADD_ADMIN.name(), ADD_USER.name(), REMOVE_USER.name()};
      replyKeyboardMarkup.addRow(userManagement);

      String[] showUsers = new String[] {LIST_USERS.name(), LIST_ADMINS.name()};
      replyKeyboardMarkup.addRow(showUsers);

      String[] groupManagement = new String[] {ADD_NEW_GROUP.name()};
      replyKeyboardMarkup.addRow(groupManagement);
    }

    return replyKeyboardMarkup;
  }

  public void memorizeLastGivenAnswer(String lastGivenAnswer) {
    assistantDTO.setLastGivenAnswer(lastGivenAnswer);
  }

  public String lastGivenAnswer() {
    try {
      Operations.valueOf(assistantDTO.getLastGivenAnswer());
      return null;
    } catch (Exception e) {
      return assistantDTO.getLastGivenAnswer();
    }
  }

  public AssistantDTO toDTO() {
    return assistantDTO;
  }

  public Operations lastActiveOperation() {
    if (assistantDTO.getActiveOperation() == null || assistantDTO.getActiveOperation().isEmpty()) {
      return EMPTY;
    }
    return Operations.valueOf(assistantDTO.getActiveOperation());
  }

  public void memorizeLastActiveOperation(Operations lastActiveOperation) {
    assistantDTO.setActiveOperation(lastActiveOperation.name());
  }

  public boolean isAdmin() {
    return userDTO.isAdmin();
  }

  public boolean hasReadAccess() {
    return userDTO.isHasReadAccess();
  }

  public UserDTO getUserDTO() {
    return userDTO;
  }

  public boolean userWroteSomething() {
    return lastGivenAnswer() != null && !lastGivenAnswer().isEmpty();
  }

  public String responseToUser() {
    return responseToUser;
  }

  public void provideMessageToUser(String message) {
    this.responseToUser = message;
  }

  public AssistantDTO getAssistantDTO() {
    return assistantDTO;
  }

  public boolean isWaitingForAnswer() {
    return (assistantDTO.getActiveOperation() != null
            && !assistantDTO.getActiveOperation().isEmpty()
            && !assistantDTO.getActiveOperation().equals(EMPTY.name()))
        && (assistantDTO.getLastGivenAnswer().equals(assistantDTO.getActiveOperation()));
  }
}
