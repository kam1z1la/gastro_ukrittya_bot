package com.gastro_ukrittya.bot.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public interface KeyboardAbstractFactory {
    InlineKeyboardMarkup createInlineKeyboardMarkup();
    ReplyKeyboardMarkup createReplyKeyboardMarkup();
}
