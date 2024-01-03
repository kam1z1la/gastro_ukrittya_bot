package com.gastro_ukrittya.bot.config;

public enum Command {
    CONTACT {
        @Override
        public String getCommand() {
            return "\uD83D\uDCDE  Контакти";
        }
    },
    STAND_UP {
        @Override
        public String getCommand() {
            return "\uD83C\uDFAD  Стендап";
        }
    },
    RESERVE_TABLE {
        @Override
        public String getCommand() {
            return "\uD83E\uDD42 Забронювати столик";
        }
    },
    CANCEL_RESERVATION {
        @Override
        public String getCommand() {
            return "❌ Скасувати оформлення";
        }
    },
    EDIT_RESERVE {
        @Override
        public String getCommand() {
            return "Редагувати замовлення";
        }
    },
    BOOKED {
        @Override
        public String getCommand() {
            return "Адміністратор закладу звʼяжеться з вами для підтвердження резерву";
        }
    };

    public abstract String getCommand();
}
