package com.gastro_ukrittya.bot.handler.reservation.state.reservation;

import com.gastro_ukrittya.bot.dto.ReservationDto;
import com.gastro_ukrittya.bot.handler.reservation.ValidationService;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.gastro_ukrittya.bot.config.Command.COME_BACK;
import static com.gastro_ukrittya.bot.handler.reservation.state.Error.INCORRECT_TIME;
import static com.gastro_ukrittya.bot.handler.reservation.state.Error.TIME_FORMAT;
import static com.gastro_ukrittya.bot.handler.reservation.state.ReservationMessage.NUMBER_OF_PEOPLE;
import static com.gastro_ukrittya.bot.keyboard.Keyboard.SAVE_AND_CANCEL;

@Slf4j
public class TimeState implements State {

    @Override
    public void handle(AbsSender absSender, Message message, Context context) {
        ValidationService validation = context.getValidation();

        if (validation.isTimeCorrect(message.getText()) || message.getText().equals(COME_BACK.getCommand())) {
            if (validation.isComplianceWorkSchedule(message.getText()) || message.getText().equals(COME_BACK.getCommand())) {
                nextState(message, context);
                sendMessage(absSender, NUMBER_OF_PEOPLE.getMessage(), message.getChatId(), getKeyboard(context));
            } else {
                sendMessage(absSender, INCORRECT_TIME.getError(), message.getChatId(), getKeyboard(context));
            }
        } else {
            sendMessage(absSender, TIME_FORMAT.getError(), message.getChatId(), getKeyboard(context));
        }
    }

    @Override
    public void nextState(Message message, Context context) {
        ReservationDto reservation = context.findReservationByChatId(message.getChatId());
        reservation.setTime(message.getText());
        context.getStates().put(reservation, new NumberOfPeopleState());
    }

    private void sendMessage(AbsSender absSender, String text, Long chatId, ReplyKeyboardMarkup reply) {
        try {
            absSender.execute(SendMessage.builder()
                    .chatId(chatId)
                    .text(text)
                    .replyMarkup(reply)
                    .build());
        } catch (TelegramApiException e) {
            log.error("Error in time processing state");
            throw new RuntimeException(e);
        }
    }

    private ReplyKeyboardMarkup getKeyboard(Context context) {
        return context.getKeyboard().getKeyboard(SAVE_AND_CANCEL).createReplyKeyboardMarkup();
    }
}
