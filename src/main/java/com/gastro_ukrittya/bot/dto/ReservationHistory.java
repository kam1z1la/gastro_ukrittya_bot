package com.gastro_ukrittya.bot.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ReservationHistory {
    private List<ReservationDto.ReservationMemento> mementos;
    private ReservationDto dto;

    public ReservationHistory(ReservationDto dto) {
        this.mementos = new ArrayList<>();
        this.dto = dto;
    }

    public void addMemento(ReservationDto.ReservationMemento memento) {
        mementos.add(memento);
    }

    public ReservationDto getFirstMemento() {
        ReservationDto.ReservationMemento memento = mementos.get(0);
        return dto.restore(memento);
    }
}
