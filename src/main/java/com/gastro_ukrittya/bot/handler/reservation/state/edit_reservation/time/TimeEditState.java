package com.gastro_ukrittya.bot.handler.reservation.state.edit_reservation.time;

import com.gastro_ukrittya.bot.handler.reservation.state.edit_reservation.EditContext;
import com.gastro_ukrittya.bot.handler.reservation.state.edit_reservation.EditState;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
public class TimeEditState implements EditState {

    @Override
    public void handle(AbsSender absSender, Message message, EditContext context) {
        sendMessage(absSender, message.getChatId());
        nextState(context);
    }

    private void sendMessage(AbsSender absSender, long chatId) {
        try {
            absSender.execute(SendMessage.builder()
                    .chatId(chatId)
                    .text("Введіть новий час")
                    .build());
        } catch (TelegramApiException e) {
            log.error("Error in ordering a table");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void nextState(EditContext context) {
        context.setState(new TimeEditStateUpdater());
    }
}
