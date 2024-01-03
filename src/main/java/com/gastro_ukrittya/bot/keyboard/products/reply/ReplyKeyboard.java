package com.gastro_ukrittya.bot.keyboard.products.reply;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

public interface ReplyKeyboard {
    ReplyKeyboardMarkup createReplyKeyboard();
    List<KeyboardRow> createButtons();
}