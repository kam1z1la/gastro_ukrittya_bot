package com.gastro_ukrittya.bot.handler.order.stateMachine;

import com.gastro_ukrittya.bot.config.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Service
@ToString
public class Reservation {
    private Map<User, ReservationState> states;

    public Reservation() {
        this.states = new HashMap<>();
    }

    public void goToAnotherState(Message message, ReservationState state) {
        User user = findUserByChatId(message.getChatId());
        states.put(user, state);
    }

    public ReservationState findStatus(long chatId) {
        ReservationState reservationState = states.get(findUserByChatId(chatId));
        return reservationState != null? reservationState : ReservationState.NAME;
    }

    public void deleteUserByChatId(long chatId) {
        states.remove(findUserByChatId(chatId));
    }


    public User findUserByChatId(long chatId) {
        return states.keySet().stream().filter(user -> user.getChatId().equals(chatId)).findAny()
                .orElseGet(() -> User.builder().chatId(chatId).isReservation(true).build());
    }
}
