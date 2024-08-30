package com.am.telegram.groupstat.logic.report;

public class ReportGenerationException extends RuntimeException {
  public ReportGenerationException(String message, Exception e) {
    super(message, e);
  }
}
