package com.gastro_ukrittya.bot.handler.state.reservation;

import com.gastro_ukrittya.bot.config.ReservationDto;
import com.gastro_ukrittya.bot.handler.state.Context;
import com.gastro_ukrittya.bot.handler.state.State;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Map;

import static com.gastro_ukrittya.bot.handler.state.ReservationMessage.NAME;
import static com.gastro_ukrittya.bot.keyboard.Keyboard.SAVE_AND_CANCEL;

@Slf4j
public class StartReservationState implements State {

    @Override
    public void doAction(AbsSender absSender, String text, long chatId, Context context) {
        nextState(text, chatId, context);
        sendMessage(absSender, NAME.getMessage(), chatId, context);
    }

    @Override
    public void nextState(String text, long id, Context context) {
        Map<ReservationDto, State> states = context.getStates();
        ReservationDto reservation = createReservationDto(id);
        states.put(reservation, new NameState());
    }

    private ReservationDto createReservationDto(long chatId) {
        return ReservationDto.builder()
                .chatId(chatId)
                .isReservation(true)
                .build();
    }

    @Override
    public Message sendMessage(AbsSender absSender, String text, long chatId, Context context) {
        try {
            return absSender.execute(SendMessage.builder()
                    .chatId(chatId)
                    .text(text)
                    .replyMarkup(getKeyboard(context))
                    .build());
        } catch (TelegramApiException e) {
            log.error("Error in ordering a table");
            throw new RuntimeException(e);
        }
    }

    private ReplyKeyboardMarkup getKeyboard(Context context) {
        return context.getKeyboard().getKeyboard(SAVE_AND_CANCEL).createReplyKeyboardMarkup();
    }
}
