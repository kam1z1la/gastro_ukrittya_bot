package com.gastro_ukrittya.bot.handler;

import com.gastro_ukrittya.bot.config.BotConfig;
import com.gastro_ukrittya.bot.config.Order;
import com.gastro_ukrittya.bot.handler.reservation.stateMachine.ReservationStateMachine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.UserShared;

import static com.gastro_ukrittya.bot.handler.Command.BOOKED;
import static com.gastro_ukrittya.bot.handler.Command.CANCEL_RESERVATION;


@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingCommandBot {
    private final ReservationStateMachine reservation;
    private final BotConfig bot;

    public TelegramBot(BotConfig bot, ReservationStateMachine reservation) {
        super(bot.getToken());
        this.bot = bot;
        this.reservation = reservation;
    }

    @Override
    public String getBotUsername() {
        return bot.getName();
    }

    //todo made factory and regfactor
    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            IBotCommand registeredCommand = getCommand(message);
            if (registeredCommand != null) {
                registeredCommand.processMessage(this, message, null);
            }
        }

        if (update.getMessage().hasContact()) {
            Contact contact = update.getMessage().getContact();
            Message message = update.getMessage();
            message.setText(contact.getPhoneNumber());

            UserShared userShared = new UserShared();
            userShared.setUserId(contact.getUserId());

            message.setUserShared(userShared);
            IBotCommand registeredCommand = getCommand(message);
            registeredCommand.processMessage(this, message, null);
        }
    }

    //todo подумать над многопоточностью
    public IBotCommand getCommand(Message message) {
        if (reservation.isUserMakesReservation(message.getChatId())) {
            if (message.getText().equals(BOOKED.getCommand()) || message.getText().equals(CANCEL_RESERVATION.getCommand())) {
                Order order = reservation.findUserByChatId(message.getChatId());
                order.setReservation(false);
            } else {
                return getRegisteredCommand("\uD83E\uDD42 Забронювати столик");
            }
        }
        return getRegisteredCommand(message.getText());
    }
}
