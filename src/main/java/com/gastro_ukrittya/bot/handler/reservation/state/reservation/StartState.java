package com.gastro_ukrittya.bot.handler.reservation.state.reservation;

import com.gastro_ukrittya.bot.dto.ReservationDto;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Map;

import static com.gastro_ukrittya.bot.handler.reservation.state.ReservationMessage.NAME;
import static com.gastro_ukrittya.bot.keyboard.Keyboard.SAVE_AND_CANCEL;

@Slf4j
public class StartState implements State {

    @Override
    public void handle(AbsSender absSender, Message message, Context context) {
        nextState(message, context);
        sendMessage(absSender, NAME.getMessage(), message.getChatId(), getKeyboard(context));
    }

    @Override
    public void nextState(Message message, Context context) {
        Map<ReservationDto, State> states = context.getStates();
        ReservationDto reservation = createReservationDto(message.getChatId());
        states.put(reservation, new NameState());
    }

    private ReservationDto createReservationDto(long chatId) {
        return ReservationDto.builder()
                .chatId(chatId)
                .isReservation(true)
                .build();
    }

    public void sendMessage(AbsSender absSender, String text, long chatId, ReplyKeyboardMarkup reply) {
        try {
            absSender.execute(SendMessage.builder()
                    .chatId(chatId)
                    .text(text)
                    .replyMarkup(reply)
                    .build());
        } catch (TelegramApiException e) {
            log.error("Error in start reservation processing state");
            throw new RuntimeException(e);
        }
    }

    private ReplyKeyboardMarkup getKeyboard(Context context) {
        return context.getKeyboard().getKeyboard(SAVE_AND_CANCEL).createReplyKeyboardMarkup();
    }
}
