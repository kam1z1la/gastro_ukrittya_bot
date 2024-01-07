package com.gastro_ukrittya.bot.db.reservation;

import com.gastro_ukrittya.bot.dto.ReservationDto;
import com.gastro_ukrittya.bot.db.Mapper;
import com.gastro_ukrittya.bot.handler.reservation.ValidationService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService implements Mapper<ReservationDto, Reservation> {
    private final ReservationRepository reservationRepository;
    private final ValidationService validation;

    @Transactional
    public Reservation addReservation(ReservationDto reservationDto) {
        Reservation reservation = toEntity(reservationDto);
        log.info("Reservation success add");
        return reservationRepository.save(reservation);
    }

    @Modifying
    @Transactional
    public void deleteReservation(long id) {
        reservationRepository.deleteById(id);
    }

    public void updateReservation(@NonNull ReservationDto dto) {
        Reservation reservation = toEntity(dto);
        reservationRepository.updateReservation(reservation);
    }

    public Reservation findReservationByMessageId(Integer id) {
         return reservationRepository.findByMessageId(id);
    }

    @Override
    public Reservation toEntity(ReservationDto dto) {
        return Reservation.builder()
                .id(dto.getReservationId())
                .messageId(dto.getMessageId())
                .date(LocalDate.parse(validation.getDate(dto.getDate()), DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .time(LocalTime.parse(dto.getTime()))
                .numberOfPeople(dto.getNumberOfPeople())
                .client(dto.getClient())
                .build();
    }
}
