package com.gastro_ukrittya.bot.handler.reservation.state;

public enum ReservationMessage {
    NAME {
        @Override
        public String getMessage() {
            return "Введіть своє ім'я";
        }
    },
    TIME {
        @Override
        public String getMessage() {
            return "Введіть час\n⏰ Наш графік роботи\n Пн-Нд 10:00–22:00";
        }
    },
    DATE {
        @Override
        public String getMessage() {
            return "Введіть дату";
        }
    },
    NUMBER_OF_PEOPLE {
        @Override
        public String getMessage() {
            return "Введіть кількість людей";
        }
    },
    PHONE_NUMBER {
        @Override
        public String getMessage() {
            return "Введіть номер телефону замовника";
        }
    };
    public abstract String getMessage();
}
