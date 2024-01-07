package com.gastro_ukrittya.bot.handler.reservation;

import com.gastro_ukrittya.bot.dto.ReservationDto;
import com.gastro_ukrittya.bot.handler.reservation.state.reservation.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import static com.gastro_ukrittya.bot.config.Command.COME_BACK;

@Slf4j
@Component
@RequiredArgsConstructor
public class ComeBackHandler implements IBotCommand {
    private final Context context;

    @Override
    public String getCommandIdentifier() {
        return COME_BACK.getCommand();
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        State state = context.findState(message.getChatId());
        String name = state.getClass().getSimpleName();
        changeOfState(name, message);
        context.handle(absSender, message);
    }

    private void changeOfState(String name, Message message) {
        switch (name) {
            case "DateState" -> {
                ReservationDto reservation = context.findReservationByChatId(message.getChatId());
                context.getStates().put(reservation, new StartState());
            }
            case "TimeState" -> {
                ReservationDto reservation = context.findReservationByChatId(message.getChatId());
                context.getStates().put(reservation, new NameState());
            }
            case "NumberOfPeopleState" -> {
                ReservationDto reservation = context.findReservationByChatId(message.getChatId());
                context.getStates().put(reservation, new DateState());
            }
            case "PhoneNumberState" -> {
                ReservationDto reservation = context.findReservationByChatId(message.getChatId());
                context.getStates().put(reservation, new NumberOfPeopleState());
            }
        }
    }
}
