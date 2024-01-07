package com.gastro_ukrittya.bot.handler.reservation;

import com.gastro_ukrittya.bot.handler.reservation.state.edit_reservation.EditContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Arrays;

import static com.gastro_ukrittya.bot.config.Command.EDIT_RESERVE;

@Slf4j
@Component
@RequiredArgsConstructor
public class EditReservationHandler implements IBotCommand {
    private final EditContext editContext;

    @Override
    public String getCommandIdentifier() {
        return EDIT_RESERVE.getCommand();
    }

    @Override
    public String getDescription() {
        return "Change reservation by admin\n";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        editContext.handle(absSender, message, getArgument(arguments));
    }

    private String getArgument(String[] arguments) {
        return Arrays.stream(arguments).findFirst().orElse("");
    }
}