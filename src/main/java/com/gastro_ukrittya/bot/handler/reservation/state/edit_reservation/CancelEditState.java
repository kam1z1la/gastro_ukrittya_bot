package com.gastro_ukrittya.bot.handler.reservation.state.edit_reservation;

import com.gastro_ukrittya.bot.dto.ReservationDto;
import com.gastro_ukrittya.bot.dto.ReservationHistory;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.gastro_ukrittya.bot.keyboard.Keyboard.MAIN;


public class CancelEditState implements EditState {
    private ReservationHistory history;

    @Override
    public void handle(AbsSender absSender, Message message, EditContext context) {
        history = context.getHistory();
        ReservationDto reservation = history.getFirstMemento();
        editMessage(absSender, message, reservation, context);
        nextState(context);
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
        history.getMementos().clear();
        context.setReservationDto(null);
        context.setState(null);
    }
}
