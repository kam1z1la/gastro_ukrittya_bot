package com.gastro_ukrittya.bot.handler.reservation.state.edit_reservation;

import com.gastro_ukrittya.bot.dto.ReservationDto;
import com.gastro_ukrittya.bot.db.client.ClientService;
import com.gastro_ukrittya.bot.db.reservation.ReservationService;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.gastro_ukrittya.bot.keyboard.Keyboard.MAIN;

public class SaveEditState implements EditState {

    @Override
    public void handle(AbsSender absSender, Message message, EditContext context) {
        ReservationDto reservationDto = context.getReservationDto();
        updateData(reservationDto, context);
        editMessage(absSender, message, reservationDto, context);
        nextState(context);
    }

    private void updateData(ReservationDto reservationDto, EditContext context) {
        ClientService clientService = context.getClientService();
        clientService.updateClientById(reservationDto);
        ReservationService reservationService = context.getReservationService();
        reservationService.updateReservation(reservationDto);
    }

    private void editMessage(AbsSender absSender, Message message, ReservationDto reservationDto, EditContext context) {
        try {
            absSender.execute(
                    EditMessageText.builder()
                            .chatId(message.getChatId())
                            .messageId(reservationDto.getMessageId())
                            .text(context.getNotification(reservationDto))
                            .parseMode(ParseMode.MARKDOWN)
                            .replyMarkup(getKeyboard(context))
                            .build());
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private InlineKeyboardMarkup getKeyboard(EditContext context) {
        return context.getKeyboard().getKeyboard(MAIN).createInlineKeyboardMarkup();
    }

    @Override
    public void nextState(EditContext context) {
        context.setState(null);
        context.setReservationDto(null);
        context.getHistory().getMementos().clear();
    }
}
