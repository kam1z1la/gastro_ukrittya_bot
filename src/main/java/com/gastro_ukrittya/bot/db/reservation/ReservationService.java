package com.gastro_ukrittya.bot.db.reservation;

import com.gastro_ukrittya.bot.config.ReservationDto;
import com.gastro_ukrittya.bot.db.Mapper;
import com.gastro_ukrittya.bot.handler.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService implements Mapper<ReservationDto, Reservation> {
    private final ReservationRepository reservationRepository;
    private final ValidationService validation;

    public Long addReservation(ReservationDto reservationDto) {
        Long id = reservationRepository.save(toEntity(reservationDto)).getId();
        log.info("Reservation success add");
        return id;
    }

    @Modifying
    public void updateReservation(Reservation reservation) {
        reservationRepository.save(reservation);
    }

    public Reservation findReservationById(long id) {
        return reservationRepository.findById(id).orElseThrow();
    }

    public Reservation findReservationByMessageId(long id) {
        return reservationRepository.findByMessageId(id);
    }

    @Override
    public Reservation toEntity(ReservationDto dto) {
        return Reservation.builder()
                .messageId(dto.getMessageId())
                .date(LocalDate.parse(validation.getDate(dto.getDate())))
                .time(LocalTime.parse(dto.getTime()))
                .numberOfPeople(dto.getNumberOfPeople())
                .client(dto.getClient())
                .build();
    }


}
