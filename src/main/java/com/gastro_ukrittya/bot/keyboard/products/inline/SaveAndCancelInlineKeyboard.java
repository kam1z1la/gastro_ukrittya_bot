package com.gastro_ukrittya.bot.keyboard.products.inline;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class SaveAndCancelInlineKeyboard implements InlineKeyboard {
    @Override
    public InlineKeyboardMarkup createInlineKeyboard() {
        return InlineKeyboardMarkup.builder()
                .keyboard(createButtons())
                .build();
    }

    @Override
    public List<List<InlineKeyboardButton>> createButtons() {
        return List.of(
                List.of(InlineKeyboardButton.builder()
                        .text("✅ Зберегти зміни")
                        .callbackData("save")
                        .build()),
                List.of(InlineKeyboardButton.builder()
                        .text("\uD83D\uDEAB Скасувати зміни")
                        .callbackData("main")
                        .build()));
    }
}
