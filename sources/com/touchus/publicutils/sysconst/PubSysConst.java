package com.touchus.publicutils.sysconst;

import com.touchus.publicutils.R;

public class PubSysConst {
    public static String ACTION_FACTORY_BREAKSET = "com.touchus.factorytest.breakpos";
    public static boolean DEBUG = false;
    public static String GT9XX_INT_LR = "/sys/bus/i2c/drivers/gt9xx/gt9xx_shlr";
    public static String GT9XX_INT_TRIGGER = "/sys/bus/i2c/drivers/gt9xx/gt9xx_int_trigger";
    public static String GT9XX_INT_UPDN = "/sys/bus/i2c/drivers/gt9xx/gt9xx_updn";
    public static String KEY_BREAKPOS = "breakpos";
    public static int[] ids = {R.id.simplified, R.id.traditional, R.id.english, R.id.spanish, R.id.german, R.id.russian, R.id.french, R.id.portuguese, R.id.serbian, R.id.turkish, R.id.swedish, R.id.italian, R.id.polish, R.id.japanese, R.id.korean, R.id.filipino, R.id.vietnamese, R.id.thai, R.id.dutch, R.id.danish, R.id.greek, R.id.hindi};
    public static int[] turkish_ids = {R.id.simplified, R.id.turkish, R.id.english};
}
