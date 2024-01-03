package com.gastro_ukrittya.bot.handler.state;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

public interface State {
    void doAction(AbsSender absSender, String text, long chatId, Context context);
    Message sendMessage(AbsSender absSender, String text, long chatId, Context context);
    void nextState(String text, long id, Context context);
}
