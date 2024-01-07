package com.gastro_ukrittya.bot.handler.reservation.state.edit_reservation.number_of_people;

import com.gastro_ukrittya.bot.dto.ReservationDto;
import com.gastro_ukrittya.bot.handler.reservation.state.edit_reservation.EditContext;
import com.gastro_ukrittya.bot.handler.reservation.state.edit_reservation.EditState;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.gastro_ukrittya.bot.keyboard.Keyboard.ADDITIONAL_FEATURES;

public class NumberOfPeopleEditStateUpdater implements EditState {

    @Override
    public void handle(AbsSender absSender, Message message, EditContext context) {
        ReservationDto reservationDto = context.getReservationDto();
        reservationDto.setNumberOfPeople(message.getText());
        editMessage(absSender, message, reservationDto, context);
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
        return context.getKeyboard().getKeyboard(ADDITIONAL_FEATURES).createInlineKeyboardMarkup();
    }

    @Override
    public void nextState(EditContext context) {
        context.setState(null);
    }
}
