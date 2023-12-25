package com.gastro_ukrittya.bot.handler.order;

import com.gastro_ukrittya.bot.config.User;
import com.gastro_ukrittya.bot.handler.order.stateMachine.ReservationState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Service
public class Sender {

//    @Async
    public void buildUser(AbsSender absSender, ReservationState status, User user, Message message) {
        switch (status) {
            case TIME -> user.setName(message.getText());
            case NUMBER_OF_PEOPLE -> user.setDate(message.getText());
            case PHONE_NUMBER -> user.setTime(message.getText());
            case BOOKING -> user.setNumberOfPeople(message.getText());
            case ORDER -> {
                user.setPhoneNumber(message.getText());
                processMessage(absSender, user, message.getUserShared().getUserId());
            }
        }
    }

    private void processMessage(AbsSender absSender, User user, long userId) {
        try {
            absSender.execute(SendMessage.builder()
                    .chatId(-4069499854L)
                    .text(String.format("Name: %s\n" +
                                    "Date: %s\n" +
                                    "Time: %s\n" +
                                    "Number of people: %s\n" +
                                    "Phone number: [%s](tg://user?id=%d)\n",
                            user.getName(),
                            user.getDate(),
                            user.getTime(),
                            user.getNumberOfPeople(),
                            user.getPhoneNumber(),
                            userId)
                    ).parseMode(ParseMode.MARKDOWN)
                    .build());
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
