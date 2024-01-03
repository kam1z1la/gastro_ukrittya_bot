package com.gastro_ukrittya.bot.handler.state.edit_reservation;

import com.gastro_ukrittya.bot.config.ReservationDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Setter
@Getter
@Service
public class EditContext {
    private EditState state;
    private ReservationDto reservationDto;

    public void doAction(AbsSender absSender, Message message) {
        state.doAction(absSender, message, this);
    }
}
