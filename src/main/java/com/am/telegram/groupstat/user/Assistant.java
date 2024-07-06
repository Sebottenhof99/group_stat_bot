package com.am.telegram.groupstat.user;

import com.am.telegram.groupstat.user.operations.Operations;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;

import static com.am.telegram.groupstat.user.operations.Operations.*;

public class Assistant {

    private final AssistantDTO assistantDTO;

    public Assistant(AssistantDTO assistantDTO) {
        this.assistantDTO = assistantDTO;
    }

    public Keyboard availableOperations() {

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup("")
                //   .oneTimeKeyboard(true)   // optional
                .resizeKeyboard(true)    // optional
                .selective(true);

        if (assistantDTO.isAdmin() || assistantDTO.isHasReadAccess()) {
            replyKeyboardMarkup.addRow(GET_CURRENT_REPORT.name(), assistantDTO.isSubscribed() ? UNSUBSCRIBE.name() : SUBSCRIBE.name());
        }

        if (assistantDTO.isAdmin()) {
            String[] userManagement = new String[]{ADD_ADMIN.name(), ADD_USER.name(), REMOVE_USER.name()};
            replyKeyboardMarkup.addRow(userManagement);

            String[] showUsers = new String[]{LIST_USERS.name(), LIST_ADMINS.name()};
            replyKeyboardMarkup.addRow(showUsers);

            String[] groupManagement = new String[]{ADD_NEW_GROUP.name(), REMOVE_GROUP.name()};
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
        if (assistantDTO.getLastActiveOperation() == null || assistantDTO.getLastActiveOperation().isEmpty()) {
            return EMPTY;
        }
        return Operations.valueOf(assistantDTO.getLastActiveOperation());
    }

    public void memorizeLastActiveOperation(Operations lastActiveOperation) {
        assistantDTO.setLastActiveOperation(lastActiveOperation.name());
    }

    public boolean isAdmin() {
        return assistantDTO.isAdmin();
    }

    public void unsubscribeUser() {
        assistantDTO.setSubscribed(false);
    }

    public void subscribeUser() {
        assistantDTO.setSubscribed(true);
    }

    public boolean hasReadAccess() {
        return assistantDTO.isHasReadAccess();
    }
}
