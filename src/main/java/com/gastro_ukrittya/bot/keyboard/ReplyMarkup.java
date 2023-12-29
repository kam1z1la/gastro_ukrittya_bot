package com.gastro_ukrittya.bot.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

public interface ReplyMarkup {
    ReplyKeyboardMarkup createKeyboard();
    List<KeyboardRow> createButtons();
}
