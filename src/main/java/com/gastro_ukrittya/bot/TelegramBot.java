package com.gastro_ukrittya.bot;

import com.gastro_ukrittya.bot.config.BotConfig;
import com.gastro_ukrittya.bot.dto.ReservationDto;
import com.gastro_ukrittya.bot.handler.reservation.state.edit_reservation.EditContext;
import com.gastro_ukrittya.bot.handler.reservation.state.reservation.Context;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.gastro_ukrittya.bot.config.Command.*;


@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingCommandBot {
    private final Context context;
    private final EditContext editContext;
    private final BotConfig bot;

    public TelegramBot(BotConfig bot, Context context, EditContext editContext) {
        super(bot.getToken());
        this.bot = bot;
        this.context = context;
        this.editContext = editContext;
    }

    @Override
    public String getBotUsername() {
        return bot.getName();
    }

    //todo regfactor
    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            IBotCommand registeredCommand = getCommand(message);
            if (registeredCommand != null) {
                registeredCommand.processMessage(this, message, new String[]{});
            }
        }

        if (update.getMessage() != null && update.getMessage().hasContact()) {
            Contact contact = update.getMessage().getContact();
            Message message = update.getMessage();
            message.setText(contact.getPhoneNumber());

            UserShared userShared = new UserShared();
            userShared.setUserId(contact.getUserId());

            message.setUserShared(userShared);
            IBotCommand registeredCommand = getCommand(message);
            registeredCommand.processMessage(this, message, new String[]{});
        }

        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            IBotCommand command = getRegisteredCommand(EDIT_RESERVE.getCommand());
            answerCallbackQuery(callbackQuery);
            command.processMessage(this, callbackQuery.getMessage(), new String[]{callbackQuery.getData()});
        }
    }

    private void answerCallbackQuery(CallbackQuery callbackQuery) {
        try {
            sendApiMethod(AnswerCallbackQuery.builder()
                    .callbackQueryId(callbackQuery.getId())
                    .build());
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    //todo подумать над многопоточностью
    public IBotCommand getCommand(Message message) {
        if (context.isUserMakesReservation(message.getChatId())) {
            if (message.getText().equals(COME_BACK.getCommand())) {
                return getRegisteredCommand(COME_BACK.getCommand());
            }
            if (message.getText().equals(CANCEL_RESERVATION.getCommand())) {
                ReservationDto reservationDto = context.findReservationByChatId(message.getChatId());
                reservationDto.setReservation(false);
            } else {
                return getRegisteredCommand(RESERVE_TABLE.getCommand());
            }
        }
        if (editContext.getState() != null) {
            return getRegisteredCommand(EDIT_RESERVE.getCommand());
        }
        return getRegisteredCommand(message.getText());
    }
}
