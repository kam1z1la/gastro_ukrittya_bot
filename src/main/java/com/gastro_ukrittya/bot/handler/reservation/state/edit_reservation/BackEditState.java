package com.gastro_ukrittya.bot.handler.reservation.state.edit_reservation;

import com.gastro_ukrittya.bot.dto.ReservationDto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;

import static com.gastro_ukrittya.bot.keyboard.Keyboard.SAVE_AND_CANCEL;

public class BackEditState implements EditState {

    @Override
    public void handle(AbsSender absSender, Message message, EditContext context) {
        ReservationDto reservationDto = context.getReservationDto();
        editMessage(absSender, message, reservationDto, context);
        nextState(context);
    }

    private void editMessage(AbsSender absSender, Message message, ReservationDto reservationDto, EditContext context) {
        try {
            absSender.execute(
                    EditMessageReplyMarkup.builder()
                            .chatId(message.getChatId())
                            .messageId(reservationDto.getMessageId())
                            .replyMarkup(getKeyboard(context))
                            .build());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private InlineKeyboardMarkup getKeyboard(EditContext context) {
        return context.getKeyboard().getKeyboard(SAVE_AND_CANCEL).createInlineKeyboardMarkup();
    }

    @Override
    public void nextState(EditContext context) {
        context.setState(null);
    }
}
