package com.gastro_ukrittya.bot.handler;

import com.gastro_ukrittya.bot.handler.reservation.Sender;
import com.gastro_ukrittya.bot.handler.reservation.stateMachine.ReservationStateMachine;
import com.gastro_ukrittya.bot.handler.reservation.stateMachine.ReservationState;
import com.gastro_ukrittya.bot.keyboard.ReplyMarkupFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;

import static com.gastro_ukrittya.bot.handler.Command.BOOKED;
import static com.gastro_ukrittya.bot.handler.reservation.stateMachine.ReservationState.*;
import static com.gastro_ukrittya.bot.keyboard.Event.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationHandler implements IBotCommand {
    private final ReservationStateMachine reservationStateMachine;
    private final ReplyMarkupFactory keyboard;
    private final Sender sender;

    @Override
    public String getCommandIdentifier() {
        return Command.RESERVE_TABLE.getCommand();
    }

    @Override
    public String getDescription() {
        return "The user started booking a table\n";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        ReservationState status = reservationStateMachine.findStatus(message.getChatId());
        switch (status) {
            case NAME -> {
                sender(absSender, message, "Введіть своє ім'я", DATE);
                reservationStateMachine.goToAnotherState(message, DATE);
            }
            case DATE -> {
                sender(absSender, message, "Введіть дату", TIME);
                reservationStateMachine.goToAnotherState(message, TIME);
                sender.buildUser(absSender, TIME, reservationStateMachine.findUserByChatId(message.getChatId()), message);
            }
            case TIME -> {
                if (isDateCorrect(message.getText())) {
                    if (!isDatePast(message.getText())) {
                        sender(absSender, message, "Введіть час\n\t ⏰ Наш графік роботи 10:00–22:00", NUMBER_OF_PEOPLE);
                        reservationStateMachine.goToAnotherState(message, NUMBER_OF_PEOPLE);
                        sender.buildUser(absSender, NUMBER_OF_PEOPLE, reservationStateMachine.findUserByChatId(message.getChatId()), message);
                    } else {
                        sender(absSender, message, "❗\uFE0F Будь ласка, введіть коректну дату.", NUMBER_OF_PEOPLE);
                    }
                } else {
                    sender(absSender, message, "❗\uFE0F Будь ласка, введіть дату у правильному форматі, наприклад, 01.01 або 01.01.2024.", NUMBER_OF_PEOPLE);
                }
            }
            case NUMBER_OF_PEOPLE -> {
                if (isTimeCorrect(message.getText())) {
                    if (isComplianceWorkSchedule(message.getText())) {
                        sender(absSender, message, "Введіть кількість людей", PHONE_NUMBER);
                        reservationStateMachine.goToAnotherState(message, PHONE_NUMBER);
                        sender.buildUser(absSender, PHONE_NUMBER, reservationStateMachine.findUserByChatId(message.getChatId()), message);
                    } else {
                        sender(absSender, message, "❗\uFE0F Вибачте, але наш заклад не працює у зазначений вами час.", NUMBER_OF_PEOPLE);
                    }
                } else {
                    sender(absSender, message, "❗\uFE0F Будь ласка, введіть час у правильному форматі, наприклад, 10:00.", NUMBER_OF_PEOPLE);
                }
            }
            case PHONE_NUMBER -> {
                sender(absSender, message, "Введіть номер телефону замовника", BOOKING);
                reservationStateMachine.goToAnotherState(message, BOOKING);
                sender.buildUser(absSender, BOOKING, reservationStateMachine.findUserByChatId(message.getChatId()), message);
            }
            case BOOKING -> {
                if (isPhoneNumberCorrect(message.getText())) {
                    sender(absSender, message, BOOKED.getCommand(), ORDER);
                    reservationStateMachine.goToAnotherState(message, ORDER);
                    sender.buildUser(absSender, ORDER, reservationStateMachine.findUserByChatId(message.getChatId()), message);
                    reservationStateMachine.deleteUserByChatId(message.getChatId());
                } else {
                    sender(absSender, message, "❗\uFE0F Будь ласка, введіть коректний номер телефону", BOOKING);
                }
            }
        }
    }


    public boolean isComplianceWorkSchedule(String time) {
        LocalTime parse = LocalTime.parse(getNumber(time));
        return parse.isAfter(LocalTime.parse("09:59")) && parse.isBefore(LocalTime.parse("22:00"));
    }

    public String getNumber(String time) {
        return time.matches("^\\d{2}:\\d{2}$")? time : "0" + time;
    }


    private boolean isDateCorrect(String date) {
//        LocalDate DATE = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
//        DATE.getDayOfWeek();

        return date.matches("^\\d{2}.\\d{2}.\\d{4}$") || date.matches("^\\d{2}.\\d{2}$");
    }

    private boolean isDatePast(String date) {
        LocalDate DATE = LocalDate.parse(getDate(date), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        return DATE.isBefore(LocalDate.now());
    }

    private String getDate(String date) {
        return date.matches("^\\d{2}.\\d{2}$")? date + "." + Year.now().getValue() : date;
    }



    private boolean isTimeCorrect(String time) {
        return time.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]");
    }

    private boolean isPhoneNumberCorrect(String phoneNumber) {
        return phoneNumber.matches("^0\\d{9}$") || phoneNumber.matches("^\\+380\\d{9}$");
    }


    public void sender(AbsSender absSender, Message message, String text, ReservationState status) {
        try {
            absSender.execute(SendMessage.builder()
                    .chatId(message.getChatId())
                    .text(text)
                    .replyMarkup(getKeyboard(status))
                    .build());
        } catch (TelegramApiException e) {
            log.error("Error in ordering a table");
            throw new RuntimeException(e);
        }
    }

    private ReplyKeyboardMarkup getKeyboard(ReservationState status) {
        if (!status.equals(ORDER)) {
            if (!status.equals(BOOKING)) {
                return keyboard.getKeyboard(CANCEL);
            }
            return keyboard.getKeyboard(CONTACT);
        } else {
            return keyboard.getKeyboard(MAIN);
        }
    }
}
