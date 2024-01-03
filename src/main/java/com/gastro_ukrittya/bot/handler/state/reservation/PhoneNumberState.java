package com.gastro_ukrittya.bot.handler.state.reservation;

import com.gastro_ukrittya.bot.config.ReservationDto;
import com.gastro_ukrittya.bot.handler.SaveReservation;
import com.gastro_ukrittya.bot.handler.ValidationService;
import com.gastro_ukrittya.bot.handler.state.Context;
import com.gastro_ukrittya.bot.handler.state.State;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.gastro_ukrittya.bot.config.Command.BOOKED;
import static com.gastro_ukrittya.bot.handler.state.Error.INCORRECT_PHONE_NUMBER;
import static com.gastro_ukrittya.bot.keyboard.Keyboard.ADDITIONAL_FEATURES;
import static com.gastro_ukrittya.bot.keyboard.Keyboard.MAIN;

@Slf4j
public class PhoneNumberState implements State {
    @Override
    public void doAction(AbsSender absSender, String text, long chatId, Context context) {
        ValidationService validation = context.getValidation();

        if (validation.isPhoneNumberCorrect(text)) {
            nextState(text, chatId, context);
            Message message = sendMessage(absSender, BOOKED.getCommand(), chatId, context);
            saveReservation(absSender, message, context);
        } else {
            sendMessage(absSender, INCORRECT_PHONE_NUMBER.getError(), chatId, context);
        }
    }

    @Override
    public void nextState(String text, long id, Context context) {
        ReservationDto reservation = context.findReservationByChatId(id);
        reservation.setPhoneNumber(text);
        reservation.setReservation(false);
        context.getStates().put(reservation, null);
    }

    @Override
    public Message sendMessage(AbsSender absSender, String text, long chatId, Context context) {
        try {
            return absSender.execute(SendMessage.builder()
                    .chatId(chatId)
                    .text(text)
                    .replyMarkup(getKeyboard(text, context))
                    .build());
        } catch (TelegramApiException e) {
            log.error("Error in ordering a table");
            throw new RuntimeException(e);
        }
    }

    private ReplyKeyboardMarkup getKeyboard(String text, Context context) {
        return text.equals(BOOKED.getCommand()) ?
                context.getKeyboard().getKeyboard(MAIN).createReplyKeyboardMarkup()
                : context.getKeyboard().getKeyboard(ADDITIONAL_FEATURES).createReplyKeyboardMarkup();
    }

    private void saveReservation(AbsSender absSender,  Message message, Context context) {
        SaveReservation saveReservation = context.getSaveReservation();

        ReservationDto reservation = context.findReservationByChatId(message.getChatId());
        saveReservation.setReservationDto(reservation);
        context.deleteUserByChatId(message.getChatId());

        saveReservation.processMessage(absSender, message, null);
    }
}
