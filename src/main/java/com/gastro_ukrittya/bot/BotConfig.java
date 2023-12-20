package com.gastro_ukrittya.bot;

import org.springframework.beans.factory.annotation.Value;
import lombok.*;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Configuration
public class BotConfig {
    @Value("${bot.token}")
    private String token;

    @Value("${bot.name}")
    private String name;
}
