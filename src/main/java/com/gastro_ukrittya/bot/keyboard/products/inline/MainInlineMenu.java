package com.gastro_ukrittya.bot.keyboard.products.inline;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class MainInlineMenu implements InlineKeyboard {
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
                        .text("\uD83D\uDCDD Редагувати замовлення")
                        .callbackData("edit")
                        .build()
                ),
                List.of(InlineKeyboardButton.builder()
                        .text("\uD83D\uDDD1 Видалити замовлення")
                        .callbackData("delete-confirm")
                        .build()
                ));
    }
}
