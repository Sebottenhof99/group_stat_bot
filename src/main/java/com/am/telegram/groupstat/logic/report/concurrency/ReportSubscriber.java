package com.am.telegram.groupstat.logic.report.concurrency;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendDocument;
import java.io.File;
import java.util.Objects;

public class ReportSubscriber {

  private final long chatId;
  private final TelegramBot bot;

  public ReportSubscriber(long chatId, TelegramBot bot) {
    this.chatId = chatId;
    this.bot = bot;
  }

  public void update(File file) {
    bot.execute(new SendDocument(chatId, file));
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) return true;
    if (object == null || getClass() != object.getClass()) return false;
    ReportSubscriber that = (ReportSubscriber) object;
    return chatId == that.chatId;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(chatId);
  }
}
