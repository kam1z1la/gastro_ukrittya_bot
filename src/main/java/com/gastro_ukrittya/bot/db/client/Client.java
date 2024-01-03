package com.gastro_ukrittya.bot.db.client;

import com.gastro_ukrittya.bot.db.reservation.Reservation;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "client")
@ToString(exclude = {"reservations"})
@Builder
public class Client {
    @Id
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<Reservation> reservations;
}
