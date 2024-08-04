package com.am.telegram.groupstat.user.scennarios;

import com.am.telegram.groupstat.user.assistant.Assistant;
import com.am.telegram.groupstat.user.assistant.AssistantService;
import com.am.telegram.groupstat.user.assistant.UserAssistant;
import com.am.telegram.groupstat.user.user.UserDTO;
import com.am.telegram.groupstat.user.user.UserService;
import java.util.Optional;

public class RemoveUserScenario implements Scenario {

  private final Assistant assistant;
  private final AssistantService assistantService;
  private final UserService userService;

  public RemoveUserScenario(
      Assistant assistant, AssistantService assistantService, UserService userService) {
    this.assistant = assistant;
    this.assistantService = assistantService;
    this.userService = userService;
  }

  @Override
  public void execute(long chatId) {
    Optional<UserDTO> userDTO = new UserAssistant(assistant, userService).removeUser();
    userDTO.ifPresent(userService::remove);
    assistantService.save(assistant);
  }
}
