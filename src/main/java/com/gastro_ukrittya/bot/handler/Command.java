package com.gastro_ukrittya.bot.handler;

public enum Command {
    CONTACT {
        @Override
        String getCommand() {
            return "\uD83D\uDCDE  Контакти";
        }
    },
    STAND_UP {
        @Override
        String getCommand() {
            return "\uD83C\uDFAD  Стендап";
        }
    },
    RESERVE_TABLE {
        @Override
        String getCommand() {
            return "\uD83E\uDD42 Забронювати столик";
        }
    },
    CANCEL_RESERVATION {
        @Override
        String getCommand() {
            return "❌ Скасувати оформлення";
        }
    },
    BOOKED {
        @Override
        String getCommand() {
            return "Адміністратор закладу звʼяжеться з вами для підтвердження резерву";
        }
    };

    abstract String getCommand();
}
