package com.gastro_ukrittya.bot.keyboard;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Service
public class ReplyMarkupFactory {

    //todo создать ошибку
    public ReplyKeyboardMarkup getKeyboard(Event event) {
        switch (event) {
            case MAIN -> {
                return new MainKeyboard().createKeyboard();
            }
            case CANCEL -> {
                return new CancelKeyboard().createKeyboard();
            }
            case CONTACT -> {
                return new ContactKeyboard().createKeyboard();
            }
            default -> {
                return null;
            }
        }
    }
}
