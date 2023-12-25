package com.gastro_ukrittya.bot.config;

import org.springframework.beans.factory.annotation.Value;
import lombok.*;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Configuration
public class ContactConfig {
    @Value("${contact.address}")
    private String address;

    @Value("${contact.phoneNumber}")
    private String phoneNumber;

    @Value("${contact.mail}")
    private String mail;

    @Value("${contact.link.facebook}")
    private String facebook;

    @Value("${contact.link.instagram}")
    private String instagram;

    @Value("${contact.location.latitude}")
    private Double latitude;

    @Value("${contact.location.longitude}")
    private Double longitude;
}
