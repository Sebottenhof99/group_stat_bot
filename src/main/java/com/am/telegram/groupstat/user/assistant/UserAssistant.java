package com.am.telegram.groupstat.user.assistant;

import com.am.telegram.groupstat.user.UserManagementPanel;
import com.am.telegram.groupstat.user.operations.Operations;
import com.am.telegram.groupstat.user.user.UserDTO;
import com.am.telegram.groupstat.user.user.UserService;
import java.util.Optional;

public class UserAssistant {

  private final Assistant assistant;
  private final UserService userService;
  private final UserManagementPanel userManagementPanel = new UserManagementPanel();

  public UserAssistant(Assistant assistant, UserService userService) {
    this.assistant = assistant;
    this.userService = userService;
  }

  public Optional<UserDTO> removeUser() {
    if (!assistant.isAdmin()) {
      assistant.provideMessageToUser("You have no permissions for this operation");
      return Optional.empty();
    }
    if (assistant.userWroteSomething()) {
      String userName = assistant.lastGivenAnswer();
      Optional<UserDTO> userByName = userService.getUserByName(userName);
      if (userByName.isPresent()) {
        assistant.provideMessageToUser(
            "User with nickname " + assistant.lastGivenAnswer() + " has been removed");
      } else {
        assistant.provideMessageToUser(
            "User with nickname " + assistant.lastGivenAnswer() + " does not exist");
      }

      assistant.memorizeLastActiveOperation(Operations.EMPTY);
      assistant.memorizeLastGivenAnswer(null);
      return userByName;

    } else {
      assistant.memorizeLastActiveOperation(Operations.REMOVE_USER);
      assistant.provideMessageToUser("Please, provide username");
      return Optional.empty();
    }
  }

  public Optional<UserDTO> addAdmin() {
    return addUser(
        Operations.ADD_ADMIN,
        userManagementPanel.createAdmin(assistant.getUserDTO().getUserName()));
  }

  public Optional<UserDTO> addRegularUser() {
    return addUser(
        Operations.ADD_USER, userManagementPanel.createUser(assistant.getUserDTO().getUserName()));
  }

  public Optional<UserDTO> addUser(Operations operation, UserDTO user) {

    if (!assistant.isAdmin()) {
      assistant.provideMessageToUser("You have no permissions for this operation");
      return Optional.empty();
    }

    if (assistant.userWroteSomething()) {
      String userName = assistant.lastGivenAnswer();
      user.setUserName(userName);

      assistant.provideMessageToUser(
          "User with nickname " + assistant.lastGivenAnswer() + " has been added");

      assistant.memorizeLastActiveOperation(Operations.EMPTY);
      assistant.memorizeLastGivenAnswer(null);
      return Optional.of(user);

    } else {
      assistant.memorizeLastActiveOperation(operation);
      assistant.provideMessageToUser("Please, provide username");
      return Optional.empty();
    }
  }
}
