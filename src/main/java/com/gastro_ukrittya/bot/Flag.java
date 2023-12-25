package com.gastro_ukrittya.bot;

public enum Flag {
    IS_RESERVATION;

    private static boolean flag;

    public static boolean getFlag() {
        return flag;
    }

    public static void setFlag(boolean newFlag) {
        flag = newFlag;
    }
}
