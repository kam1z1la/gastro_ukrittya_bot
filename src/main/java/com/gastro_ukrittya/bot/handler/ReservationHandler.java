package com.gastro_ukrittya.bot.handler;

import com.gastro_ukrittya.bot.handler.order.Sender;
import com.gastro_ukrittya.bot.handler.order.stateMachine.Reservation;
import com.gastro_ukrittya.bot.handler.order.stateMachine.ReservationState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

import static com.gastro_ukrittya.bot.handler.Command.BOOKED;
import static com.gastro_ukrittya.bot.handler.order.stateMachine.ReservationState.BOOKING;
import static com.gastro_ukrittya.bot.handler.order.stateMachine.ReservationState.ORDER;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationHandler implements IBotCommand {
    private final Reservation reservation;
    private final Sender sender;

    @Override
    public String getCommandIdentifier() {
        return Command.RESERVE_TABLE.getCommand();
    }

    @Override
    public String getDescription() {
        return "The user started booking a table\n";
    }

    //    @Async
    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        ReservationState status = reservation.findStatus(message.getChatId());
        switch (status) {
            case NAME -> sender(absSender, message, "Введіть своє ім'я", ReservationState.DATE);
            case DATE -> sender(absSender, message, "Введіть дату", ReservationState.TIME);
            case TIME -> sender(absSender, message, "Введіть час", ReservationState.NUMBER_OF_PEOPLE);
            case NUMBER_OF_PEOPLE -> sender(absSender, message, "Введіть кількість людей", ReservationState.PHONE_NUMBER);
            case PHONE_NUMBER -> sender(absSender, message, "Введіть номер телефону замовника", BOOKING);
            case BOOKING -> sender(absSender, message, BOOKED.getCommand(), ORDER);
        }
    }

    public void sender(AbsSender absSender, Message message, String text, ReservationState status) {
        try {
            absSender.execute(SendMessage.builder()
                    .chatId(message.getChatId())
                    .text(text)
                    .replyMarkup(createReplyKeyboardMarkup(status))
                    .build());

            reservation.goToAnotherState(message, status);
            sender.buildUser(absSender, status, reservation.findUserByChatId(message.getChatId()), message);
        } catch (TelegramApiException e) {
            log.error("Error in ordering a table", new RuntimeException(e));
        }
    }

    private ReplyKeyboardMarkup createReplyKeyboardMarkup(ReservationState status) {
        if (!status.equals(ORDER)) {
            return ReplyKeyboardMarkup.builder()
                    .selective(true)
                    .resizeKeyboard(true)
                    .oneTimeKeyboard(false)
                    .keyboard(createKeyboardRow(status))
                    .build();
        } else {
            return ReplyKeyboardMarkup.builder()
                    .selective(true)
                    .resizeKeyboard(true)
                    .oneTimeKeyboard(false)
                    .keyboardRow(new KeyboardRow(
                            List.of(
                                    new KeyboardButton("\uD83D\uDCDE  Контакти"),
                                    new KeyboardButton("\uD83C\uDFAD  Стендап")
                            )
                    ))
                    .keyboardRow(new KeyboardRow(
                            List.of(new KeyboardButton("\uD83E\uDD42 Забронювати столик"))
                    ))
                    .build();
        }
    }

    private List<KeyboardRow> createKeyboardRow(ReservationState status) {
        if (!status.equals(BOOKING)) {
            return List.of(new KeyboardRow(
                    List.of(new KeyboardButton("❌ Скасувати оформлення"))
            ));
        } else {
            return List.of(
                    new KeyboardRow(
                            List.of(
                                    KeyboardButton.builder()
                                            .text("\uD83D\uDCF1 Поширити свій номер телефону")
                                            .requestContact(true)
                                            .build()
                            )
                    ),
                    new KeyboardRow(
                            List.of(new KeyboardButton("❌ Скасувати оформлення"))
                    ));
        }
    }
}
