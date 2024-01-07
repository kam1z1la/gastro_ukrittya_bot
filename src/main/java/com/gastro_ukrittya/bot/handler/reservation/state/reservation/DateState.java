package com.gastro_ukrittya.bot.handler.reservation.state.reservation;

import com.gastro_ukrittya.bot.dto.ReservationDto;
import com.gastro_ukrittya.bot.handler.reservation.ValidationService;
import com.gastro_ukrittya.bot.handler.reservation.state.Error;
import com.gastro_ukrittya.bot.handler.reservation.state.ReservationMessage;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import static com.gastro_ukrittya.bot.config.Command.COME_BACK;
import static com.gastro_ukrittya.bot.keyboard.Keyboard.SAVE_AND_CANCEL;

@Slf4j
public class DateState implements State {
    private ValidationService validation;

    @Override
    public void handle(AbsSender absSender, Message message, Context context) {
        validation = context.getValidation();

        if (validation.isDateCorrect(message.getText()) || message.getText().equals(COME_BACK.getCommand())) {
            if (validation.isCurrentDate(message.getText()) || message.getText().equals(COME_BACK.getCommand())) {
                nextState(message, context);
                sendMessage(absSender, ReservationMessage.TIME.getMessage(), message.getChatId(), getKeyboard(context));
            } else {
                sendMessage(absSender, Error.DATA_FORMAT.getError(), message.getChatId(), getKeyboard(context));
            }
        } else {
            sendMessage(absSender, Error.INCORRECT_DATA.getError(), message.getChatId(), getKeyboard(context));
        }
    }

    @Override
    public void nextState(Message message, Context context) {
        ReservationDto reservation = context.findReservationByChatId(message.getChatId());
        reservation.setDate(validation.getDate(message.getText()));
        context.getStates().put(reservation, new TimeState());
    }

    private ReplyKeyboardMarkup getKeyboard(Context context) {
        return context.getKeyboard().getKeyboard(SAVE_AND_CANCEL).createReplyKeyboardMarkup();
    }

    private void sendMessage(AbsSender absSender, String text, long chatId, ReplyKeyboardMarkup reply) {
        try {
            absSender.execute(SendMessage.builder()
                    .chatId(chatId)
                    .text(text)
                    .replyMarkup(reply)
                    .build());
        } catch (TelegramApiException e) {
            log.error("Error in date processing state");
            throw new RuntimeException(e);
        }
    }
}
