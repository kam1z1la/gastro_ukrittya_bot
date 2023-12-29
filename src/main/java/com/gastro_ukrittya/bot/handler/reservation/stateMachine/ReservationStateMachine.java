package com.gastro_ukrittya.bot.handler.reservation.stateMachine;

import com.gastro_ukrittya.bot.config.Order;
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
public class ReservationStateMachine {
    private Map<Order, ReservationState> states;

    public ReservationStateMachine() {
        this.states = new HashMap<>();
    }

    public void goToAnotherState(Message message, ReservationState state) {
        Order order = findUserByChatId(message.getChatId());
        states.put(order, state);
    }

    public ReservationState findStatus(long chatId) {
        Order order = findUserByChatId(chatId);
        ReservationState reservationState = states.get(order);
        return reservationState != null? reservationState : ReservationState.NAME;
    }

    public void deleteUserByChatId(long chatId) {
        Order order = findUserByChatId(chatId);
        states.remove(order);
    }

    public Order findUserByChatId(long chatId) {
        return states.keySet().stream().filter(order -> order.getChatId().equals(chatId)).findAny()
                .orElseGet(() -> Order.builder()
                        .chatId(chatId)
                        .isReservation(true)
                        .build());
    }

    public boolean isUserMakesReservation(long chatId) {
        Order order = states.keySet().stream().filter(o -> o.getChatId().equals(chatId)).findFirst()
                .orElseGet(() -> Order.builder().isReservation(false).build());
        return order.isReservation();
    }
}
