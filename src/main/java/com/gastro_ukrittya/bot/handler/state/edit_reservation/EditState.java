package com.gastro_ukrittya.bot.handler.state.edit_reservation;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

public interface EditState {
    void doAction(AbsSender absSender, Message message, EditContext context);
}
