package com.gastro_ukrittya.bot.handler.reservation;

import com.gastro_ukrittya.bot.config.Order;
import com.gastro_ukrittya.bot.db.reservation.ReservationService;
import com.gastro_ukrittya.bot.db.user.ClientService;
import com.gastro_ukrittya.bot.handler.Notification;
import com.gastro_ukrittya.bot.handler.reservation.stateMachine.ReservationState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.StringJoiner;

@Slf4j
@Service
@RequiredArgsConstructor
public class Sender implements Notification {
    private final ClientService clientService;
    private final ReservationService reservationService;
    private Order order;

    public void buildUser(AbsSender absSender, ReservationState status, Order order, Message message) {
        switch (status) {
            case TIME -> order.setName(message.getText());
            case NUMBER_OF_PEOPLE -> order.setDate(message.getText());
            case PHONE_NUMBER -> order.setTime(getNumber(message.getText()));
            case BOOKING -> order.setNumberOfPeople(message.getText());
            case ORDER -> {
                order.setPhoneNumber(message.getText());
                processMessage(absSender, order);
            }
        }
    }

    public String getNumber(String time) {
        return time.matches("^\\d{2}:\\d{2}$")? time : "0" + time;
    }

    private void processMessage(AbsSender absSender, Order order) {
        try {
            this.order = order;

            clientService.addClient(order);
            reservationService.addReservation(order);

            absSender.execute(SendMessage.builder()
                    .chatId(-4069499854L)
                    .text(getNotification())
                    .parseMode(ParseMode.MARKDOWN)
                    .build());
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getNotification() {
        StringJoiner joiner = new StringJoiner("\n");
        joiner.add(String.format("Name: %s", order.getName()));
        joiner.add(String.format("Date: %s", order.getDate()));
        joiner.add(String.format("Time: %s", order.getTime()));
        joiner.add(String.format("Number of people: %s", order.getNumberOfPeople()));
        joiner.add(String.format("Phone number: `%s`", order.getPhoneNumber()));
        return joiner.toString();
    }
}
