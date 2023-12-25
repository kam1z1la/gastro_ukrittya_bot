package com.gastro_ukrittya.bot.command;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.ICommandRegistry;

@Component
@RequiredArgsConstructor
public class CommandInitializer implements InitializingBean {
    private final ICommandRegistry registry;
    private final IBotCommand[] commands;

    @Override
    public void afterPropertiesSet() {
        registry.registerAll(commands);
    }
}