package com.gastro_ukrittya.bot.handler;

import com.gastro_ukrittya.bot.config.BotConfig;
import com.gastro_ukrittya.bot.handler.Command;
import com.gastro_ukrittya.bot.handler.ReservationHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.UserShared;

import static com.gastro_ukrittya.bot.handler.Command.BOOKED;
import static com.gastro_ukrittya.bot.handler.Command.CANCEL_ORDER;


@Slf4j
@Component
public class TelegramBotCommand extends TelegramLongPollingCommandBot {
    private boolean isReservation;
    private final BotConfig bot;

    public TelegramBotCommand(BotConfig bot) {
        super(bot.getToken());
        this.bot = bot;
    }

    @Override
    public String getBotUsername() {
        return bot.getName();
    }

    //todo made factory
    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            IBotCommand registeredCommand = getCommand(message);
            registeredCommand.processMessage(this, message, null);
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

    public IBotCommand getCommand(Message message) {
        IBotCommand registeredCommand = getRegisteredCommand(message.getText());

        if (registeredCommand instanceof ReservationHandler) {
            isReservation = true;
        } else if (isReservation) {
            //todo refactor!!!!
            if (message.getText().equals(BOOKED.getCommand()) || message.getText().equals(CANCEL_ORDER.getCommand())) {
                isReservation = false;
            } else {
                registeredCommand = getRegisteredCommand("\uD83E\uDD42 Забронювати столик");
            }
        }

        return registeredCommand;
    }
}
