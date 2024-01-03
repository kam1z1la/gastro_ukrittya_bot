package com.gastro_ukrittya.bot.handler.state.edit_reservation;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
public class EditNameState implements EditState {
    @Override
    public void doAction(AbsSender absSender, Message message, EditContext context) {
        sendMessage(absSender, "Введіть нове ім'я", message.getChatId());
        context.setState(new ValidationEditNameState());
    }

    public Message sendMessage(AbsSender absSender, String text, long chatId) {
        try {
            return absSender.execute(SendMessage.builder()
                    .chatId(chatId)
                    .text(text)
                    .build());
        } catch (TelegramApiException e) {
            log.error("Error in ordering a table");
            throw new RuntimeException(e);
        }
    }
}
