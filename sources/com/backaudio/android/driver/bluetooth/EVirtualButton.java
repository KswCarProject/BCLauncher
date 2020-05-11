package com.backaudio.android.driver.bluetooth;

import com.sun.mail.iap.Response;
import com.touchus.benchilauncher.views.MenuSlide;
import org.slf4j.Marker;

public enum EVirtualButton {
    ONE,
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN,
    EIGHT,
    NINE,
    ZERO,
    ASTERISK,
    WELL;

    public static final String parse(EVirtualButton button) {
        switch ($SWITCH_TABLE$com$backaudio$android$driver$bluetooth$EVirtualButton()[button.ordinal()]) {
            case 1:
                return "1";
            case 2:
                return "2";
            case 3:
                return "3";
            case 4:
                return "4";
            case 5:
                return "5";
            case 6:
                return "6";
            case 7:
                return "7";
            case 8:
                return "8";
            case 9:
                return "9";
            case 10:
                return "0";
            case MenuSlide.STATE_UP222 /*11*/:
                return Marker.ANY_MARKER;
            case Response.BAD /*12*/:
                return "#";
            default:
                return null;
        }
    }

    public static final String parse(char button) {
        if (button == '*' || button == '#' || (button >= '0' && button <= '9')) {
            return String.valueOf(button);
        }
        return null;
    }
}
