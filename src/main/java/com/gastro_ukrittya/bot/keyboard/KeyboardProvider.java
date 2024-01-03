package com.gastro_ukrittya.bot.keyboard;

import com.gastro_ukrittya.bot.keyboard.factory.AdditionalFeatures;
import com.gastro_ukrittya.bot.keyboard.factory.MainMenuFactory;
import com.gastro_ukrittya.bot.keyboard.factory.ActionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KeyboardProvider {

    public KeyboardAbstractFactory getKeyboard(Keyboard keyboard) {
        try {
            switch (keyboard) {
                case MAIN -> {
                    return new MainMenuFactory();
                }
                case SAVE_AND_CANCEL -> {
                    return new ActionFactory();
                }
                case ADDITIONAL_FEATURES -> {
                    return new AdditionalFeatures();
                }
                default -> throw new IllegalAccessException();
            }
        } catch (IllegalAccessException e) {
            log.error("Error creating keys");
            throw new RuntimeException(e);
        }
    }
}
