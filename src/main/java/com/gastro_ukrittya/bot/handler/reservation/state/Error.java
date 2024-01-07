package com.gastro_ukrittya.bot.handler.reservation.state;

public enum Error {
    DATA_FORMAT {
        @Override
        public String getError() {
            return "❗ Будь ласка, введіть коректну дату.";
        }
    },
    INCORRECT_DATA {
        @Override
        public String getError() {
            return "❗ Будь ласка, введіть дату у правильному форматі, наприклад, 01.01 або 01.01.2024.";
        }
    },
    INCORRECT_TIME {
        @Override
        public String getError() {
            return "❗ Вибачте, але наш заклад не працює у зазначений вами час.";
        }
    },
    TIME_FORMAT {
        @Override
        public String getError() {
            return "❗ Будь ласка, введіть час у правильному форматі, наприклад, 10:00.";
        }
    },
    INCORRECT_PHONE_NUMBER {
        @Override
        public String getError() {
            return "❗ Будь ласка, введіть коректний номер телефону";
        }
    };

    public abstract String getError();
}
