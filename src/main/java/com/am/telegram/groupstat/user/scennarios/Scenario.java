package com.am.telegram.groupstat.user.scennarios;

import com.pengrad.telegrambot.request.BaseRequest;

public interface Scenario {
     void execute(long chatId);
}
