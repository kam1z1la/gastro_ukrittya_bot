package com.gastro_ukrittya.bot.db.reservation;

import com.gastro_ukrittya.bot.db.client.Client;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Table(name = "reservation")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private LocalTime time;

    @Column(name = "number_of_people")
    private String numberOfPeople;

    @Column(name = "message_id")
    private Integer messageId;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "client_id")
    private Client client;
}

