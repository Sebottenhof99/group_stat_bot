package com.am.telegram.groupstat.logic.assistant;

import com.am.telegram.groupstat.logic.Operations;
import com.am.telegram.groupstat.logic.group.GroupDTO;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.GetChatMemberCount;
import com.pengrad.telegrambot.response.BaseResponse;
import java.util.Optional;

public class GroupAssistant {
  private final Assistant assistant;
  private final TelegramBot bot;

  public GroupAssistant(Assistant assistant, TelegramBot bot) {
    this.assistant = assistant;
    this.bot = bot;
  }

  public Optional<GroupDTO> addNewGroup() {
    if (!assistant.isAdmin()) {
      assistant.provideMessageToUser("You have no permissions for this operation");
      return Optional.empty();
    }

    if (assistant.userWroteSomething()) {
      String groupRawData = assistant.lastGivenAnswer();
      String[] groupData = groupRawData.split(":");
      if (groupData.length != 3) {
        assistant.provideMessageToUser("Not valid data provided. No Group can be added");
        assistant.memorizeLastActiveOperation(Operations.EMPTY);
        assistant.memorizeLastGivenAnswer(null);
        return Optional.empty();
      }

      BaseResponse execute = bot.execute(new GetChatMemberCount(groupData[0]));

      GroupDTO groupDTO =
          new GroupDTO(
              groupData[0], groupData[1], groupData[2], assistant.getUserDTO().getUserName());

      assistant.memorizeLastActiveOperation(Operations.EMPTY);
      assistant.memorizeLastGivenAnswer(null);

      if (execute.isOk()) {
        assistant.provideMessageToUser("Group has been added");
        return Optional.of(groupDTO);
      }

      return Optional.empty();
    } else {
      assistant.memorizeLastActiveOperation(Operations.ADD_NEW_GROUP);
      assistant.provideMessageToUser(
          "Please, provide group information in following format: "
              + "@group_name_on_telegram:City:Category\n"
              + "e.g: @urokimeditacii_bryansk:Брянск:Россия");
      return Optional.empty();
    }
  }
}
