package com.gastro_ukrittya.bot.handler;

import com.gastro_ukrittya.bot.config.ContactConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.StringJoiner;

import static com.gastro_ukrittya.bot.handler.Command.CONTACT;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactCommand implements IBotCommand {
    private final ContactConfig contact;

    @Override
    public String getCommandIdentifier() {
        return CONTACT.getCommand();
    }

    @Override
    public String getDescription() {
        return "User views contact information";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        execute(absSender, message);
    }

    private void execute(AbsSender absSender, Message message) {
        try {
            absSender.execute(SendLocation.builder()
                            .chatId(message.getChatId())
                            .latitude(contact.getLatitude())
                            .longitude(contact.getLongitude())
                            .build());

            absSender.execute(SendMessage.builder()
                    .chatId(message.getChatId())
                    .text(createMessage())
                    .parseMode(ParseMode.MARKDOWN)
                    .disableWebPagePreview(true)
                    .replyMarkup(createReplyKeyboardMarkup())
                    .build());
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private String createMessage() {
        StringJoiner joiner = new StringJoiner("\n\n");
        joiner.add(String.format("\uD83D\uDCCD %s", contact.getAddress()));
        joiner.add(String.format("\uD83D\uDCDE `%s`", contact.getPhoneNumber()));
        joiner.add(String.format("✉\uFE0F `%s`", contact.getMail()));
        joiner.add(String.format("[Фейсбук](%s) | [Інстаграм](%s)", contact.getFacebook(), contact.getInstagram()));
        return joiner.toString();
    }

    public ReplyKeyboardMarkup createReplyKeyboardMarkup() {
        return ReplyKeyboardMarkup.builder()
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .keyboardRow(new KeyboardRow(
                        List.of(
                                new KeyboardButton("\uD83D\uDCDE  Контакти"),
                                new KeyboardButton("\uD83C\uDFAD  Стендап")
                        )
                ))
                .keyboardRow(new KeyboardRow(
                        List.of(new KeyboardButton("\uD83E\uDD42 Забронювати столик"))
                ))
                .build();
    }
}
