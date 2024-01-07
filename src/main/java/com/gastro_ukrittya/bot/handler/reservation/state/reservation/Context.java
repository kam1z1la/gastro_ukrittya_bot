package com.gastro_ukrittya.bot.handler.reservation.state.reservation;

import com.gastro_ukrittya.bot.dto.ReservationDto;
import com.gastro_ukrittya.bot.handler.reservation.SaveReservationHandler;
import com.gastro_ukrittya.bot.handler.reservation.ValidationService;
import com.gastro_ukrittya.bot.keyboard.KeyboardProvider;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
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
    private final SaveReservationHandler saveReservationHandler;
    private final Map<ReservationDto, State> states;

    public Context(ValidationService validation, KeyboardProvider keyboard, SaveReservationHandler saveReservationHandler) {
        this.states = new HashMap<>();
        this.validation = validation;
        this.keyboard = keyboard;
        this.saveReservationHandler = saveReservationHandler;
    }

    public void handle(AbsSender absSender, Message message) {
        State state = findState(message.getChatId());
        state.handle(absSender, message, this);
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
        return state != null? state : new StartState();
    }


    public ReservationDto findReservationByChatId(long chatId) {
        return states.keySet().stream().filter(reservation -> reservation.getChatId().equals(chatId))
                .findFirst().orElse(null);
    }
}
