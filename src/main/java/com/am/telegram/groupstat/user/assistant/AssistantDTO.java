package com.am.telegram.groupstat.user.assistant;

public class AssistantDTO {

  private String activeOperation;
  private String lastGivenAnswer;

  public String getActiveOperation() {
    return activeOperation;
  }

  public void setActiveOperation(String activeOperation) {
    this.activeOperation = activeOperation;
  }

  public String getLastGivenAnswer() {
    return lastGivenAnswer;
  }

  public void setLastGivenAnswer(String lastGivenAnswer) {
    this.lastGivenAnswer = lastGivenAnswer;
  }
}
