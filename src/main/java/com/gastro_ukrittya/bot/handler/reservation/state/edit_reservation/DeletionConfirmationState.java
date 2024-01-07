package com.gastro_ukrittya.bot.handler.reservation.state.edit_reservation;

import com.gastro_ukrittya.bot.dto.ReservationDto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;

public class DeletionConfirmationState implements EditState {

    @Override
    public void handle(AbsSender absSender, Message message, EditContext context) {
        ReservationDto reservationDto = context.getReservationDto();
        editMessage(absSender, message, reservationDto);
        nextState(context);
    }

    private void editMessage(AbsSender absSender, Message message, ReservationDto reservationDto) {
        try {
            absSender.execute(
                    EditMessageReplyMarkup.builder()
                            .chatId(message.getChatId())
                            .messageId(reservationDto.getMessageId())
                            .replyMarkup(createDeleteInlineKeyboardMarkup())
                            .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public InlineKeyboardMarkup createDeleteInlineKeyboardMarkup() {
        return InlineKeyboardMarkup.builder()
                .keyboard(createDeleteButtons())
                .build();
    }

    private List<List<InlineKeyboardButton>> createDeleteButtons() {
        return List.of(
                List.of(InlineKeyboardButton.builder()
                        .text("✅ Видалити")
                        .callbackData("delete")
                        .build()),
                List.of(InlineKeyboardButton.builder()
                        .text("\uD83D\uDEAB Скасувати")
                        .callbackData("cancel")
                        .build()));
    }

    @Override
    public void nextState(EditContext context) {
        context.setState(null);
    }
}
