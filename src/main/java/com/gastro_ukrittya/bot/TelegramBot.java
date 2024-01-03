package com.gastro_ukrittya.bot;

import com.gastro_ukrittya.bot.config.BotConfig;
import com.gastro_ukrittya.bot.config.ReservationDto;
import com.gastro_ukrittya.bot.db.client.Client;
import com.gastro_ukrittya.bot.db.client.ClientService;
import com.gastro_ukrittya.bot.db.reservation.Reservation;
import com.gastro_ukrittya.bot.db.reservation.ReservationService;
import com.gastro_ukrittya.bot.handler.state.Context;
import com.gastro_ukrittya.bot.handler.state.edit_reservation.EditContext;
import com.gastro_ukrittya.bot.handler.state.edit_reservation.EditNameState;
import com.gastro_ukrittya.bot.keyboard.KeyboardProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.StringJoiner;

import static com.gastro_ukrittya.bot.config.Command.CANCEL_RESERVATION;
import static com.gastro_ukrittya.bot.config.Command.EDIT_RESERVE;
import static com.gastro_ukrittya.bot.keyboard.Keyboard.*;


@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingCommandBot {
    private final ReservationService reservationService;
    private final ClientService clientService;
    private final KeyboardProvider keyboard;
    private final Context context;
    private final EditContext editContext;
    private final BotConfig bot;

    public TelegramBot(EditContext editContext, ClientService clientService, ReservationService reservationService, BotConfig bot, KeyboardProvider keyboard, Context context) {
        super(bot.getToken());
        this.bot = bot;
        this.keyboard = keyboard;
        this.context = context;
        this.reservationService = reservationService;
        this.clientService = clientService;
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
                registeredCommand.processMessage(this, message, null);
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
            registeredCommand.processMessage(this, message, null);
        }

        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();

            switch (callbackQuery.getData()) {
                case "main" -> {
                    try {
                        sendApiMethod(
                                EditMessageReplyMarkup.builder()
                                        .chatId(callbackQuery.getMessage().getChatId())
                                        .messageId(callbackQuery.getMessage().getMessageId())
                                        .replyMarkup(keyboard.getKeyboard(MAIN)
                                                .createInlineKeyboardMarkup())
                                        .build());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                //todo mapper to entity
                case "edit" -> {
                    try {
                        Reservation reservationById =
                                reservationService.findReservationByMessageId(callbackQuery.getMessage().getMessageId());
                        Client client = reservationById.getClient();

                        LocalDate date = reservationById.getDate();
                        String format = date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

                        ReservationDto reservation =
                                ReservationDto.builder()
                                        .chatId(callbackQuery.getMessage().getChatId())
                                        .name(client.getFirstName())
                                        .messageId(reservationById.getMessageId())
                                        .client(client)
                                        .isEditReservation(true)
                                        .date(format)
                                        .time(reservationById.getTime().toString())
                                        .numberOfPeople(reservationById.getNumberOfPeople())
                                        .client(client)
                                        .build();

                        editContext.setReservationDto(reservation);

                        sendApiMethod(
                                EditMessageReplyMarkup.builder()
                                        .chatId(callbackQuery.getMessage().getChatId())
                                        .messageId(callbackQuery.getMessage().getMessageId())
                                        .replyMarkup(keyboard.getKeyboard(ADDITIONAL_FEATURES)
                                                .createInlineKeyboardMarkup())
                                        .build());

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                case "name" -> {
                    editContext.setState(new EditNameState());
                    IBotCommand command = getRegisteredCommand(EDIT_RESERVE.getCommand());
                    command.processMessage(this, callbackQuery.getMessage(), null);
                }
                case "back" -> {
                    try {
                        sendApiMethod(
                                EditMessageReplyMarkup.builder()
                                        .chatId(callbackQuery.getMessage().getChatId())
                                        .messageId(callbackQuery.getMessage().getMessageId())
                                        .replyMarkup(keyboard.getKeyboard(SAVE_AND_CANCEL)
                                                .createInlineKeyboardMarkup())
                                        .build());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                case "save" -> {
                    ReservationDto reservationDto = editContext.getReservationDto();

                    clientService.addClient(reservationDto);
                    reservationService.addReservation(reservationDto);

                    StringJoiner joiner = new StringJoiner("\n");
                    joiner.add(String.format("Бронь № %d\n", reservationDto.getNumberReservation()));
                    joiner.add(String.format("Ім'я замовника:\t %s", reservationDto.getName()));
                    joiner.add(String.format("Дата:\t %s", reservationDto.getDate()));
                    joiner.add(String.format("Час:\t %s", reservationDto.getTime()));
                    joiner.add(String.format("Кількість людей:\t %s", reservationDto.getNumberOfPeople()));
                    joiner.add(String.format("Номер телефону:\t `%s`", reservationDto.getPhoneNumber()));

                    editContext.setReservationDto(null);
                    try {
                        sendApiMethod(
                                EditMessageText.builder()
                                        .chatId(callbackQuery.getMessage().getChatId())
                                        .messageId(callbackQuery.getMessage().getMessageId())
                                        .text(joiner.toString())
                                        .build());

                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            try {
                sendApiMethod(new AnswerCallbackQuery(callbackQuery.getId()));
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //todo подумать над многопоточностью
    public IBotCommand getCommand(Message message) {
        if (context.isUserMakesReservation(message.getChatId())) {
            if (message.getText().equals(CANCEL_RESERVATION.getCommand())) {
                ReservationDto reservationDto = context.findReservationByChatId(message.getChatId());
                reservationDto.setReservation(false);
            } else {
                return getRegisteredCommand("\uD83E\uDD42 Забронювати столик");
            }
        }
        ReservationDto reservationDto = editContext.getReservationDto();
        if (reservationDto.isEditReservation()) {
            return getRegisteredCommand(EDIT_RESERVE.getCommand());
        }
        return getRegisteredCommand(message.getText());
    }
}
