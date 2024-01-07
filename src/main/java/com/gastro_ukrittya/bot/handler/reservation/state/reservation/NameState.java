package com.gastro_ukrittya.bot.handler.reservation.state.reservation;

import com.gastro_ukrittya.bot.dto.ReservationDto;
import com.gastro_ukrittya.bot.handler.reservation.state.ReservationMessage;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

import static com.gastro_ukrittya.bot.keyboard.Keyboard.SAVE_AND_CANCEL;

@Slf4j
public class NameState implements State {

    @Override
    public void handle(AbsSender absSender, Message message, Context context) {
        nextState(message, context);
        sendMessage(absSender, ReservationMessage.DATE.getMessage(), message.getChatId(), getKeyboard(context));
    }

    @Override
    public void nextState(Message message, Context context) {
        ReservationDto reservation = context.findReservationByChatId(message.getChatId());
        reservation.setName(message.getText());
        context.getStates().put(reservation, new DateState());
    }

    private void sendMessage(AbsSender absSender, String text, long chatId, ReplyKeyboardMarkup reply) {
        try {
            absSender.execute(SendMessage.builder()
                    .chatId(chatId)
                    .text(text)
                    .replyMarkup(reply)
                    .build());
        } catch (TelegramApiException e) {
            log.error("Error in name processing state");
            throw new RuntimeException(e);
        }
    }

    private ReplyKeyboardMarkup getKeyboard(Context context) {
        return context.getKeyboard().getKeyboard(SAVE_AND_CANCEL).createReplyKeyboardMarkup();
    }
}
