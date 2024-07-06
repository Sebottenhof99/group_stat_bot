package com.am.telegram.groupstat.user;

public class AssistantDTO {
    private String userName;
    private String lastActiveOperation;
    private String lastGivenAnswer;
    private boolean isSubscribed;
    private boolean isAdmin;
    private boolean hasReadAccess;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLastActiveOperation() {
        return lastActiveOperation;
    }

    public void setLastActiveOperation(String lastActiveOperation) {
        this.lastActiveOperation = lastActiveOperation;
    }

    public String getLastGivenAnswer() {
        return lastGivenAnswer;
    }

    public void setLastGivenAnswer(String lastGivenAnswer) {
        this.lastGivenAnswer = lastGivenAnswer;
    }

    public boolean isSubscribed() {
        return isSubscribed;
    }

    public void setSubscribed(boolean subscribed) {
        isSubscribed = subscribed;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isHasReadAccess() {
        return hasReadAccess;
    }

    public void setHasReadAccess(boolean hasReadAccess) {
        this.hasReadAccess = hasReadAccess;
    }
}
