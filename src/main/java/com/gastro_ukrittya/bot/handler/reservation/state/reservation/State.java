package com.gastro_ukrittya.bot.handler.reservation.state.reservation;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

public interface State {
    void handle(AbsSender absSender, Message message, Context context);
    void nextState(Message message, Context context);
}
