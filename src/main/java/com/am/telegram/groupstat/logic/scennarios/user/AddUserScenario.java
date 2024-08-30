package com.am.telegram.groupstat.logic.scennarios.user;

import com.am.telegram.groupstat.logic.assistant.Assistant;
import com.am.telegram.groupstat.logic.assistant.AssistantService;
import com.am.telegram.groupstat.logic.assistant.specialization.UserAssistant;
import com.am.telegram.groupstat.logic.scennarios.Scenario;
import com.am.telegram.groupstat.logic.user.UserDTO;
import com.am.telegram.groupstat.logic.user.UserService;
import java.util.Optional;

public class AddUserScenario implements Scenario {

  private final Assistant assistant;
  private final AssistantService assistantService;
  private final UserService userService;

  public AddUserScenario(
      Assistant assistant, AssistantService assistantService, UserService userService) {
    this.assistant = assistant;
    this.assistantService = assistantService;
    this.userService = userService;
  }

  @Override
  public void execute(long chatId) {
    Optional<UserDTO> userDTO = new UserAssistant(assistant, userService).addRegularUser();
    userDTO.ifPresent(userService::save);
    assistantService.save(assistant);
  }
}
