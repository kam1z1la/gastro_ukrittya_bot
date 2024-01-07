package com.gastro_ukrittya.bot.db.reservation;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Reservation findByMessageId(Integer messageId);

    @Modifying
    @Transactional
    @Query(value = """
        update Reservation r set date = :#{#reservation.date},
        time = :#{#reservation.time},
        number_of_people = :#{#reservation.numberOfPeople},
        message_id = :#{#reservation.messageId}
        where r.id = :#{#reservation.id}
        """, nativeQuery = true)
    void updateReservation(@Param("reservation") Reservation reservation);

}