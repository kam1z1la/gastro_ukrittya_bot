package com.gastro_ukrittya.bot.handler.reservation.state.edit_reservation.date;

import com.gastro_ukrittya.bot.dto.ReservationDto;
import com.gastro_ukrittya.bot.handler.reservation.ValidationService;
import com.gastro_ukrittya.bot.handler.reservation.state.Error;
import com.gastro_ukrittya.bot.handler.reservation.state.edit_reservation.EditContext;
import com.gastro_ukrittya.bot.handler.reservation.state.edit_reservation.EditState;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.gastro_ukrittya.bot.keyboard.Keyboard.ADDITIONAL_FEATURES;

@Slf4j
public class DateEditStateUpdater implements EditState {

    @Override
    public void handle(AbsSender absSender, Message message, EditContext context) {
        ValidationService validation = context.getValidation();

        if (validation.isDateCorrect(message.getText())) {
            if (validation.isCurrentDate(message.getText())) {
                ReservationDto reservationDto = context.getReservationDto();
                reservationDto.setDate(message.getText());
                editMessage(absSender, message, reservationDto, context);
                nextState(context);
            } else {
                sendMessage(absSender, Error.DATA_FORMAT.getError(), message.getChatId());
            }
        } else {
            sendMessage(absSender, Error.INCORRECT_DATA.getError(), message.getChatId());
        }
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

    private void sendMessage(AbsSender absSender, String text, long chatId) {
        try {
            absSender.execute(SendMessage.builder()
                    .chatId(chatId)
                    .text(text)
                    .build());
        } catch (TelegramApiException e) {
            log.error("Error in date processing state");
            throw new RuntimeException(e);
        }
    }
}
