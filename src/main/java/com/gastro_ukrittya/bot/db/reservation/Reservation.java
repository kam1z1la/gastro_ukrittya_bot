package com.gastro_ukrittya.bot.db.reservation;

import com.gastro_ukrittya.bot.db.user.Client;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "reservation")
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"reservations"})
@Builder
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private LocalTime time;

    @Column(name = "number_of_people")
    private String numberOfPeople;

    @ManyToMany(mappedBy = "reservations", cascade = CascadeType.REMOVE)
    private List<Client> reservations;
}

