package com.gastro_ukrittya.bot.db.reservation;

import com.gastro_ukrittya.bot.config.Order;
import com.gastro_ukrittya.bot.db.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService implements Mapper<Order, Reservation> {
    private final ReservationRepository reservationRepository;

    public void addReservation(Order order) {
        reservationRepository.save(toEntity(order));
        log.info("Reservation success add");
    }

    @Override
    public Reservation toEntity(Order dto) {
        return Reservation.builder()
                .date(LocalDate.parse(
                        getDate(dto.getDate()),
                        DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .time(LocalTime.parse(dto.getTime()))
                .numberOfPeople(dto.getNumberOfPeople())
                .build();
    }

    private String getDate(String date) {
        return date.matches("^\\d{2}.\\d{2}$")? date + "." + Year.now().getValue() : date;
    }
}
