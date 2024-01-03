package com.gastro_ukrittya.bot.handler;

import com.gastro_ukrittya.bot.handler.state.Context;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import static com.gastro_ukrittya.bot.config.Command.RESERVE_TABLE;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationHandler implements IBotCommand {
    private final Context context;

    @Override
    public String getCommandIdentifier() {
        return RESERVE_TABLE.getCommand();
    }

    @Override
    public String getDescription() {
        return "The user started booking a table\n";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        context.handle(absSender, message.getText(), message.getChatId());
    }
}
