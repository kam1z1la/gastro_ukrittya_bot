package com.gastro_ukrittya.bot.handler.reservation.state.edit_reservation;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

public interface EditState {
    void handle(AbsSender absSender, Message message, EditContext context);
    void nextState(EditContext context);
}
