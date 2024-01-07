package com.gastro_ukrittya.bot.keyboard.products.inline;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class ReservationEditorInlineKeyboard implements InlineKeyboard {

    @Override
    public InlineKeyboardMarkup createInlineKeyboard() {
        return InlineKeyboardMarkup.builder()
                .keyboard(createButtons())
                .build();
    }

    @Override
    public List<List<InlineKeyboardButton>> createButtons() {
        return List.of(
                createButton("Змінити ім'я","name"),
                createButton("Змінити дату","date"),
                createButton("Змінити час","time"),
                createButton("Змінити кількість людей","numberOfPeople"),
                createButton("Змінити номер телефону","phoneNumber"),
                createButton("⬅","back"));
    }

    private List<InlineKeyboardButton> createButton(String text, String callbackData) {
        return List.of(InlineKeyboardButton.builder()
                .text(text)
                .callbackData(callbackData)
                .build());
    }
}
