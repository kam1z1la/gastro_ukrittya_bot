package com.gastro_ukrittya.bot.handler.state;

import com.gastro_ukrittya.bot.config.ReservationDto;
import com.gastro_ukrittya.bot.handler.SaveReservation;
import com.gastro_ukrittya.bot.handler.ValidationService;
import com.gastro_ukrittya.bot.handler.state.reservation.StartReservationState;
import com.gastro_ukrittya.bot.keyboard.KeyboardProvider;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Service
@ToString
public class Context {
    private final ValidationService validation;
    private final KeyboardProvider keyboard;
    private final SaveReservation saveReservation;
    private final Map<ReservationDto, State> states;

    public Context(ValidationService validation, KeyboardProvider keyboard, SaveReservation saveReservation) {
        this.states = new HashMap<>();
        this.validation = validation;
        this.keyboard = keyboard;
        this.saveReservation = saveReservation;
    }


    public void handle(AbsSender absSender, String text, long id) {
        State state = findState(id);
        state.doAction(absSender, text, id, this);
    }

    public void deleteUserByChatId(long chatId) {
        ReservationDto reservation = findReservationByChatId(chatId);
        if (reservation != null) {
            states.remove(reservation);
        }
    }

    public boolean isUserMakesReservation(long chatId) {
        ReservationDto reservation = findReservationByChatId(chatId);
        return reservation != null && reservation.isReservation();
    }

    public State findState(long chatId) {
        ReservationDto reservation = findReservationByChatId(chatId);
        State state = states.get(reservation);
        return state != null? state : new StartReservationState();
    }

    public ReservationDto findReservationByChatId(long chatId) {
        return states.keySet().stream().filter(reservation -> reservation.getChatId().equals(chatId))
                .findFirst().orElse(null);
    }

    public ReservationDto findReservationByMessageId(long messageId) {
        return states.keySet().stream().filter(reservation -> reservation.getMessageId().equals(messageId))
                .findFirst().orElse(null);
    }
}
