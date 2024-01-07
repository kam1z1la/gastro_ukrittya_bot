package com.gastro_ukrittya.bot.handler.reservation;

import com.gastro_ukrittya.bot.dto.ReservationDto;
import com.gastro_ukrittya.bot.db.client.Client;
import com.gastro_ukrittya.bot.db.client.ClientService;
import com.gastro_ukrittya.bot.db.reservation.Reservation;
import com.gastro_ukrittya.bot.db.reservation.ReservationService;
import com.gastro_ukrittya.bot.handler.Notification;
import com.gastro_ukrittya.bot.keyboard.KeyboardProvider;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.StringJoiner;

import static com.gastro_ukrittya.bot.config.Command.BOOKED;
import static com.gastro_ukrittya.bot.keyboard.Keyboard.MAIN;

@Slf4j
@Setter
@Component
@RequiredArgsConstructor
public class SaveReservationHandler implements IBotCommand, Notification {
    private final ReservationService reservationService;
    private final ClientService clientService;
    private final KeyboardProvider keyboard;
    private ReservationDto reservationDto;

    @Override
    public String getCommandIdentifier() {
        return BOOKED.getCommand();
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        try {
            Long reservationId = save(reservationDto);

//            1852560188L
            Message execute = absSender.execute(SendMessage.builder()
                    .chatId(565537140L)
                    .replyMarkup(keyboard.getKeyboard(MAIN).createInlineKeyboardMarkup())
                    .text(getNotification())
                    .parseMode(ParseMode.MARKDOWN)
                    .build());

            reservationDto.setMessageId(execute.getMessageId());
            reservationDto.setReservationId(reservationId);
            reservationService.updateReservation(reservationDto);

        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private Long save(ReservationDto reservationDto) {
        Client client = clientService.addClient(reservationDto);
        reservationDto.setClient(client);
        Reservation reservation = reservationService.addReservation(reservationDto);
        reservationDto.setNumberReservation(reservation.getId());
        return reservation.getId();
    }

    @Override
    public String getNotification() {
        StringJoiner joiner = new StringJoiner("\n");
        joiner.add(String.format("Бронь № %d\n", reservationDto.getNumberReservation()));
        joiner.add(String.format("Ім'я замовника:\t %s", reservationDto.getName()));
        joiner.add(String.format("Дата:\t %s", reservationDto.getDate()));
        joiner.add(String.format("Час:\t %s", reservationDto.getTime()));
        joiner.add(String.format("Кількість людей:\t %s", reservationDto.getNumberOfPeople()));
        joiner.add(String.format("Номер телефону:\t `%s`", reservationDto.getPhoneNumber()));
        return joiner.toString();
    }
}
