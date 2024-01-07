package com.gastro_ukrittya.bot.handler.reservation.state.reservation;

import com.gastro_ukrittya.bot.dto.ReservationDto;
import com.gastro_ukrittya.bot.handler.reservation.SaveReservationHandler;
import com.gastro_ukrittya.bot.handler.reservation.ValidationService;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.gastro_ukrittya.bot.config.Command.BOOKED;
import static com.gastro_ukrittya.bot.handler.reservation.state.Error.INCORRECT_PHONE_NUMBER;
import static com.gastro_ukrittya.bot.keyboard.Keyboard.ADDITIONAL_FEATURES;
import static com.gastro_ukrittya.bot.keyboard.Keyboard.MAIN;

@Slf4j
public class PhoneNumberState implements State {
    private ValidationService validation;

    @Override
    public void handle(AbsSender absSender, Message message, Context context) {
        validation = context.getValidation();

        if (validation.isPhoneNumberCorrect(message.getText())) {
            nextState(message, context);
            ReplyKeyboardMarkup reply = context.getKeyboard().getKeyboard(MAIN).createReplyKeyboardMarkup();
            Message statement = sendMessage(absSender, BOOKED.getCommand(), message.getChatId(), reply);
            saveStatement(absSender, statement, context);
        } else {
            ReplyKeyboardMarkup reply = context.getKeyboard().getKeyboard(ADDITIONAL_FEATURES).createReplyKeyboardMarkup();
            sendMessage(absSender, INCORRECT_PHONE_NUMBER.getError(), message.getChatId(), reply);
        }
    }

    @Override
    public void nextState(Message message, Context context) {
        ReservationDto reservation = context.findReservationByChatId(message.getChatId());
        reservation.setPhoneNumber(validation.getPhoneNumber(message.getText()));
        reservation.setReservation(false);
        context.getStates().put(reservation, null);
    }

    public Message sendMessage(AbsSender absSender, String text, long chatId, ReplyKeyboardMarkup reply) {
        try {
            return absSender.execute(SendMessage.builder()
                    .chatId(chatId)
                    .text(text)
                    .replyMarkup(reply)
                    .build());
        } catch (TelegramApiException e) {
            log.error("Error in phone number processing state");
            throw new RuntimeException(e);
        }
    }

    private void saveStatement(AbsSender absSender, Message message, Context context) {
        SaveReservationHandler saveReservationHandler = context.getSaveReservationHandler();

        ReservationDto reservation = context.findReservationByChatId(message.getChatId());
        saveReservationHandler.setReservationDto(reservation);
        context.deleteUserByChatId(message.getChatId());

        saveReservationHandler.processMessage(absSender, message, null);
    }
}
