package com.gastro_ukrittya.bot.keyboard.products.reply;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

public class ContactReplyKeyboard implements ReplyKeyboard {
    @Override
    public ReplyKeyboardMarkup createReplyKeyboard() {
        return ReplyKeyboardMarkup.builder()
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .keyboard(createButtons())
                .build();
    }

    @Override
    public List<KeyboardRow> createButtons() {
        return List.of(
                new KeyboardRow(
                        List.of(
                                KeyboardButton.builder()
                                        .text("\uD83D\uDCF1 Поширити свій номер телефону")
                                        .requestContact(true)
                                        .build())
                ),
                new KeyboardRow(
                        List.of(new KeyboardButton("❌ Скасувати оформлення"))
                ));
    }
}
