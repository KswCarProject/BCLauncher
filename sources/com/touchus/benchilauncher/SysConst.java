package com.touchus.benchilauncher;

import android.os.Build;

public class SysConst {
    public static final int AGILITY_SETTING = 6003;
    public static final int AIR_INFO = 1012;
    public static final int AUDIO_SOURCE = 1001;
    public static final int AUX_ACTIVATE_STUTAS = 1048;
    public static final int BACKLIGHT = 1019;
    public static final int BLUETOOTH_CONNECT = 1002;
    public static final int BLUETOOTH_PHONE_STATE = 6002;
    public static final int BLUE_TOOTH_ISOPNESTATE = 6005;
    public static final int BT_A2DP_STATUS = 1041;
    public static final int BT_HEADSET_STATUS = 1042;
    public static final int CALL_FLOAT = 1040;
    public static final int CANBOX_INFO_UPDATE = 1127;
    public static final int CAR_BASE_INFO = 1007;
    public static final int CD_MUSIC_PLAY = 1005;
    public static final int CHANGE_TO_HOME = 1024;
    public static final int CLOSE_VOICE_APP = 1034;
    public static final int COPY_ERROR = 1128;
    public static final int CURENTRADIO = 1011;
    public static boolean DEBUG = false;
    public static boolean DV_CUSTOM = false;
    public static final String EVENT_EASYCONN_A2DP_ACQUIRE = "net.easyconn.a2dp.acquire";
    public static final String EVENT_EASYCONN_A2DP_ACQUIRE_FAIL = "net.easyconn.a2dp.acquire.fail";
    public static final String EVENT_EASYCONN_A2DP_ACQUIRE_OK = "net.easyconn.a2dp.acquire.ok";
    public static final String EVENT_EASYCONN_A2DP_RELEASE = "net.easyconn.a2dp.release";
    public static final String EVENT_EASYCONN_A2DP_RELEASE_FAIL = "net.easyconn.a2dp.release.fail";
    public static final String EVENT_EASYCONN_A2DP_RELEASE_OK = "net.easyconn.a2dp.release.ok ";
    public static final String EVENT_EASYCONN_APP_QUIT = "net.easyconn.app.quit";
    public static final String EVENT_EASYCONN_BT_CHECKSTATUS = "net.easyconn.bt.checkstatus";
    public static final String EVENT_EASYCONN_BT_CONNECT = "net.easyconn.bt.connect";
    public static final String EVENT_EASYCONN_BT_CONNECTED = "net.easyconn.bt.connected";
    public static final String EVENT_EASYCONN_BT_OPENED = "net.easyconn.bt.opened";
    public static final String EVENT_EASYCONN_BT_UNCONNECTED = "net.easyconn.bt.unconnected";
    public static final String EVENT_MCU_ENTER_STANDBY = "com.unibroad.mcu.enterStandbyMode";
    public static final String EVENT_MCU_WAKE_UP = "com.unibroad.mcu.wakeUp";
    public static final String EVENT_UNIBROAD_AUDIO_FOCUS = "com.unibroad.AudioFocus";
    public static final String EVENT_UNIBROAD_AUDIO_REGAIN = "com.unibroad.AudioFocus.REGAIN";
    public static final String EVENT_UNIBROAD_OPEN_ORIGINALCAR = "com.unibroad.sys.originalcar";
    public static final String EVENT_UNIBROAD_PAPAGOGAIN = "com.unbroad.AudioFocus.PAPAGOGAIN";
    public static final String EVENT_UNIBROAD_PAPAGOLOSS = "com.unbroad.AudioFocus.PAPAGOLOSS";
    public static final String EVENT_UNIBROAD_WEATHERDATA = "com.unibroad.weatherdata";
    public static final String FLAG_AIR_INFO = "airInfo";
    public static final String FLAG_AUDIO_SOURCE = "audioSource";
    public static final String FLAG_AUX_ACTIVATE_STUTAS = "aux_activate_stutas";
    public static final String FLAG_CAR_BASE_INFO = "carBaseInfo";
    public static final String FLAG_CONFIG_CONNECT = "connectType";
    public static final String FLAG_CONFIG_CONNECT1 = "connectType1";
    public static final String FLAG_CONFIG_NAVI = "naviExist";
    public static final String FLAG_CONFIG_PLAY = "playType";
    public static final String FLAG_CONFIG_RADIO = "radioType";
    public static final String FLAG_CONFIG_SCREEN = "screenType";
    public static final String FLAG_CONFIG_USB_NUM = "usbNum";
    public static final String FLAG_LANGUAGE_TYPE = "languageType";
    public static final String FLAG_MUSIC_LOOP = "musicloop";
    public static final String FLAG_MUSIC_POS = "musicPos";
    public static final String FLAG_REVERSING_TYPE = "reversingType";
    public static final String FLAG_REVERSING_VOICE = "voiceType";
    public static final String FLAG_RUNNING_STATE = "runningState";
    public static final String FLAG_UPGRADE_PER = "upgradePer";
    public static final String FLAG_USB_LISTENER = "usblistener";
    public static final String FLAG_VIDEO_POS = "videoPos";
    public static final int FORCE_MAIN_VIEW = 1030;
    public static final int GET_CURRENT_CHANNEL = 1037;
    public static final int GOTO_BT_VIEW = 1013;
    public static final int GPS_STATUS = 1038;
    public static final int HIDE_ORIGINAL_VIEW = 1035;
    public static final String IDRIVER_ENUM = "idriver_enum";
    public static final String IDRIVER_STATE_ENUM = "idriver_state_enum";
    public static final int IDRVIER_STATE = 6001;
    public static final String IS_CILK_SHIZI = "isCilkshizi";
    public static int LANGUAGE = 0;
    public static final int LISTITEM_UPDATE = 1031;
    public static final int MCU_SHOW_UPGRADE_DIALOG = 1132;
    public static final int MCU_UPGRADE_CUR_DATA = 1133;
    public static final int MCU_UPGRADE_FINISH = 1131;
    public static final int MCU_UPGRADE_HEADER = 1130;
    public static int[] MENU_BIG_ICON = null;
    public static int[] MENU_LIST = null;
    public static int[] MENU_SMALL_ICON = null;
    public static final int MUSIC_PLAY = 1004;
    public static final int ONLINE_CONFIG_DATA = 1124;
    public static final int ONLINE_CONFIG_FINISH = 1126;
    public static final int ONLINE_CONFIG_VALID = 1125;
    public static final int ORIGINAL_FLAG = 1044;
    public static final int PRESS_NEXT = 1014;
    public static final int RADAR_BACK = 1016;
    public static final int RADAR_FRONT = 1015;
    public static final int RADIOLIST = 1025;
    public static final int REVERSING = 1018;
    public static final int REVERSING_FINISH = 1026;
    public static final int RUNNING_STATE = 1008;
    public static final int SHOW_LOADING_VIEW = 2001;
    public static final int SHOW_UPGRADE_DIALOG = 1122;
    public static final String SP_SETTING_TIME = "sp_setting_time";
    public static final String SP_SETTING_YEAR = "sp_setting_year";
    public static final int START_VOICE_APP = 1033;
    public static final int SYSTEM_SETTING = 1003;
    public static final int TIME_CANDLER = 1009;
    public static boolean TURKISH_VERSION = false;
    public static int UI_TYPE = 0;
    public static final int UPDATE_CUR_MUSIC_PLAY_INFO = 7001;
    public static final int UPDATE_MUSIC_PLAY_STATE = 7003;
    public static final int UPDATE_NAVI = 1045;
    public static final int UPDATE_NAVI_END = 1047;
    public static final int UPDATE_NAVI_START = 1046;
    public static final int UPDATE_TIME_AUTO = 2003;
    public static final int UPDATE_WEATHER = 1010;
    public static final int UPGRADE_CHECK_FINISH = 1111;
    public static final int UPGRADE_COPY_PER = 1112;
    public static final int UPGRADE_CUR_DATA = 1123;
    public static final int UPGRADE_ERROR = 1129;
    public static final int UPGRADE_FINISH = 1121;
    public static final int UPGRADE_HEADER = 1120;
    public static final int UPGRADE_UPGRADE_PER = 1113;
    public static final int USB_CD_MUSIC_PLAY = 1006;
    public static final int USB_LISTENER = 7002;
    public static final int VOICE_WAKEUP_MENU = 1036;
    public static final int WHEEL_TURN = 1017;
    public static final int YOUHAO_STATE = 6004;
    public static int basicNum = 23;
    public static String brightness = "brightness";
    public static String callVoice = "callVoice";
    public static int[] callnum;
    public static String dvVoice = "dvVoice";
    public static String effectpos = "effectpos";
    public static int mediaBasicNum = 10;
    public static String mediaVoice = "mediaVoice";
    public static String mixPro = "mixpro";
    public static float musicDecVolume = 0.3f;
    public static float musicNorVolume = 1.0f;
    public static String naviVoice = "naviVoice";
    public static int[] num;
    public static byte[] storeData = new byte[15];

    public static class Cardoor {
        public static final int BEHIND = 1;
        public static final int FRONT = 0;
        public static final int LEFT_BEHIND = 2;
        public static final int LEFT_FRONT = 3;
        public static final int RIGHT_BEHIND = 4;
        public static final int RIGHT_FRONT = 5;
    }

    static {
        int[] iArr = new int[11];
        iArr[1] = 2;
        iArr[2] = 4;
        iArr[3] = 6;
        iArr[4] = 8;
        iArr[5] = 10;
        iArr[6] = 12;
        iArr[7] = 14;
        iArr[8] = 16;
        iArr[9] = 18;
        iArr[10] = 20;
        num = iArr;
        int[] iArr2 = new int[11];
        iArr2[1] = 3;
        iArr2[2] = 6;
        iArr2[3] = 9;
        iArr2[4] = 12;
        iArr2[5] = 15;
        iArr2[6] = 18;
        iArr2[7] = 21;
        iArr2[8] = 24;
        iArr2[9] = 27;
        iArr2[10] = 30;
        callnum = iArr2;
        LANGUAGE = 0;
        UI_TYPE = 0;
        if (Build.MODEL.contains("_en") || Build.MODEL.contains("_tw")) {
            LANGUAGE = 1;
        } else {
            LANGUAGE = 0;
        }
        if (Build.MODEL.contains("c200_ks") || Build.MODEL.contains("benz_hy")) {
            UI_TYPE = 1;
        } else {
            UI_TYPE = 0;
        }
        if (LANGUAGE == 0) {
            if (Build.MODEL.equals("c200") || Build.MODEL.equals("c200_lsx") || Build.MODEL.equals("benz")) {
                MENU_LIST = new int[]{R.string.name_main_navi, R.string.name_main_radio, R.string.name_main_media, R.string.name_main_phone, R.string.name_main_voice, R.string.name_main_instrument, R.string.name_main_carlife, R.string.name_main_android, R.string.name_main_setting, R.string.disp};
                MENU_BIG_ICON = new int[]{R.drawable.main_zaixiandaohang_h, R.drawable.main_radio_h, R.drawable.main_meiti_h, R.drawable.main_phone_h, R.drawable.main_voice_h, R.drawable.yejin_h, R.drawable.shoujihulian_h, R.drawable.main_anzhuo_h, R.drawable.main_set_h, R.drawable.guanbi_h};
                MENU_SMALL_ICON = new int[]{R.drawable.main_zaixiandaohang_n, R.drawable.main_radio_n, R.drawable.main_meiti_n, R.drawable.main_phone_n, R.drawable.main_voice_n, R.drawable.yejin_n, R.drawable.shoujihulian_n, R.drawable.main_anzhuo_n, R.drawable.main_set_n, R.drawable.guanbi_n};
            } else if (Build.MODEL.equals("c200_hy") || Build.MODEL.equals("c200_psr") || Build.MODEL.equals("c200_jly")) {
                MENU_LIST = new int[]{R.string.name_main_navi, R.string.name_main_radio, R.string.name_main_media, R.string.name_main_phone, R.string.name_main_instrument, R.string.name_main_voice, R.string.name_main_recorder, R.string.name_main_android, R.string.name_main_setting, R.string.disp};
                MENU_BIG_ICON = new int[]{R.drawable.main_zaixiandaohang_h1, R.drawable.main_radio_h, R.drawable.main_meiti_h, R.drawable.main_phone_h1, R.drawable.yejin_h, R.drawable.main_voice_h, R.drawable.main_jiluyi_h1, R.drawable.main_anzhuo_h, R.drawable.main_set_h1, R.drawable.guanbi_h};
                MENU_SMALL_ICON = new int[]{R.drawable.main_zaixiandaohang_n1, R.drawable.main_radio_n, R.drawable.main_meiti_n, R.drawable.main_phone_n1, R.drawable.yejin_n, R.drawable.main_voice_n, R.drawable.main_jiluyi_n1, R.drawable.main_anzhuo_n, R.drawable.main_set_n1, R.drawable.guanbi_n};
            } else if (Build.MODEL.equals("c200_ks")) {
                MENU_LIST = new int[]{R.string.name_main_navi, R.string.name_main_radio, R.string.name_main_media, R.string.name_main_phone, R.string.name_main_ecar, R.string.name_main_instrument, R.string.name_main_carlife, R.string.name_main_voice, R.string.name_main_android, R.string.name_main_setting};
                MENU_BIG_ICON = new int[]{R.drawable.main_zaixiandaohang_h_1, R.drawable.main_radio_h_1, R.drawable.main_meiti_h_1, R.drawable.main_phone_h_1, R.drawable.main_ecar_h_1, R.drawable.yejin_h_1, R.drawable.shoujihulian_h_1, R.drawable.main_voice_h_1, R.drawable.main_anzhuo_h_1, R.drawable.main_set_h_1};
                MENU_SMALL_ICON = new int[]{R.drawable.main_zaixiandaohang_n_1, R.drawable.main_radio_n_1, R.drawable.main_meiti_n_1, R.drawable.main_phone_n_1, R.drawable.main_ecar_n_1, R.drawable.yejin_n_1, R.drawable.shoujihulian_n_1, R.drawable.main_voice_n_1, R.drawable.main_anzhuo_n_1, R.drawable.main_set_n_1};
            } else if (Build.MODEL.equals("benz_hy")) {
                MENU_LIST = new int[]{R.string.name_main_navi, R.string.name_main_radio, R.string.name_main_media, R.string.name_main_phone, R.string.name_main_instrument, R.string.name_main_voice, R.string.name_main_recorder, R.string.name_main_android, R.string.name_main_setting, R.string.disp};
                MENU_BIG_ICON = new int[]{R.drawable.main_zaixiandaohang_h_1, R.drawable.main_radio_h_1, R.drawable.main_meiti_h_1, R.drawable.main_phone_h_1, R.drawable.yejin_h_1, R.drawable.main_voice_h_1, R.drawable.main_jiluyi_h_1, R.drawable.main_anzhuo_h_1, R.drawable.main_set_h_1, R.drawable.guanbi_h_1};
                MENU_SMALL_ICON = new int[]{R.drawable.main_zaixiandaohang_n_1, R.drawable.main_radio_n_1, R.drawable.main_meiti_n_1, R.drawable.main_phone_n_1, R.drawable.yejin_n_1, R.drawable.main_voice_n_1, R.drawable.main_jiluyi_n_1, R.drawable.main_anzhuo_n_1, R.drawable.main_set_n_1, R.drawable.guanbi_n_1};
            } else {
                MENU_LIST = new int[]{R.string.name_main_navi, R.string.name_main_radio, R.string.name_main_media, R.string.name_main_phone, R.string.name_main_instrument, R.string.name_main_voice, R.string.name_main_carlife, R.string.name_main_android, R.string.name_main_setting, R.string.disp};
                MENU_BIG_ICON = new int[]{R.drawable.main_zaixiandaohang_h1, R.drawable.main_radio_h, R.drawable.main_meiti_h, R.drawable.main_phone_h1, R.drawable.yejin_h, R.drawable.main_voice_h, R.drawable.shoujihulian_h, R.drawable.main_anzhuo_h, R.drawable.main_set_h1, R.drawable.guanbi_h};
                MENU_SMALL_ICON = new int[]{R.drawable.main_zaixiandaohang_n1, R.drawable.main_radio_n, R.drawable.main_meiti_n, R.drawable.main_phone_n1, R.drawable.yejin_n, R.drawable.main_voice_n, R.drawable.shoujihulian_n, R.drawable.main_anzhuo_n, R.drawable.main_set_n1, R.drawable.guanbi_n};
            }
        } else if (Build.MODEL.equals("c200_en")) {
            MENU_LIST = new int[]{R.string.name_main_navi, R.string.name_main_radio, R.string.name_main_media, R.string.name_main_phone, R.string.name_main_instrument, R.string.name_main_carlife, R.string.name_main_browser, R.string.name_main_android, R.string.name_main_setting, R.string.disp};
            MENU_BIG_ICON = new int[]{R.drawable.main_zaixiandaohang_h, R.drawable.main_radio_h, R.drawable.main_meiti_h, R.drawable.main_phone_h, R.drawable.yejin_h, R.drawable.shoujihulian_h, R.drawable.liulanqi_h, R.drawable.main_anzhuo_h, R.drawable.main_set_h, R.drawable.guanbi_h};
            MENU_SMALL_ICON = new int[]{R.drawable.main_zaixiandaohang_n, R.drawable.main_radio_n, R.drawable.main_meiti_n, R.drawable.main_phone_n, R.drawable.yejin_n, R.drawable.shoujihulian_n, R.drawable.liulanqi_n, R.drawable.main_anzhuo_n, R.drawable.main_set_n, R.drawable.guanbi_n};
        } else if (Build.MODEL.equals("c200_hy_en") || Build.MODEL.equals("c200_zlh_en")) {
            MENU_LIST = new int[]{R.string.name_main_navi, R.string.name_main_radio, R.string.name_main_media, R.string.name_main_phone, R.string.name_main_instrument, R.string.name_main_browser, R.string.name_main_recorder, R.string.name_main_android, R.string.name_main_setting, R.string.disp};
            MENU_BIG_ICON = new int[]{R.drawable.main_zaixiandaohang_h1, R.drawable.main_radio_h, R.drawable.main_meiti_h, R.drawable.main_phone_h1, R.drawable.yejin_h, R.drawable.liulanqi_h, R.drawable.main_jiluyi_h, R.drawable.main_anzhuo_h, R.drawable.main_set_h1, R.drawable.guanbi_h};
            MENU_SMALL_ICON = new int[]{R.drawable.main_zaixiandaohang_n1, R.drawable.main_radio_n, R.drawable.main_meiti_n, R.drawable.main_phone_n1, R.drawable.yejin_n, R.drawable.liulanqi_n, R.drawable.main_jiluyi_n, R.drawable.main_anzhuo_n, R.drawable.main_set_n1, R.drawable.guanbi_n};
        } else if (Build.MODEL.equals("ks_en") || Build.MODEL.equals("benz_hy_en")) {
            MENU_LIST = new int[]{R.string.name_main_navi, R.string.name_main_radio, R.string.name_main_media, R.string.name_main_phone, R.string.name_main_instrument, R.string.name_main_browser, R.string.name_main_recorder, R.string.name_main_android, R.string.name_main_setting, R.string.disp};
            MENU_BIG_ICON = new int[]{R.drawable.main_zaixiandaohang_h_1, R.drawable.main_radio_h_1, R.drawable.main_meiti_h_1, R.drawable.main_phone_h_1, R.drawable.yejin_h_1, R.drawable.liulanqi_h_1, R.drawable.main_jiluyi_h_1, R.drawable.main_anzhuo_h_1, R.drawable.main_set_h_1, R.drawable.guanbi_h_1};
            MENU_SMALL_ICON = new int[]{R.drawable.main_zaixiandaohang_n_1, R.drawable.main_radio_n_1, R.drawable.main_meiti_n_1, R.drawable.main_phone_n_1, R.drawable.yejin_n_1, R.drawable.liulanqi_n_1, R.drawable.main_jiluyi_n_1, R.drawable.main_anzhuo_n_1, R.drawable.main_set_n_1, R.drawable.guanbi_n_1};
        } else {
            MENU_LIST = new int[]{R.string.name_main_navi, R.string.name_main_radio, R.string.name_main_media, R.string.name_main_phone, R.string.name_main_instrument, R.string.name_main_carlife, R.string.name_main_recorder, R.string.name_main_android, R.string.name_main_setting, R.string.disp};
            MENU_BIG_ICON = new int[]{R.drawable.main_zaixiandaohang_h1, R.drawable.main_radio_h, R.drawable.main_meiti_h, R.drawable.main_phone_h1, R.drawable.yejin_h, R.drawable.shoujihulian_h, R.drawable.main_jiluyi_h, R.drawable.main_anzhuo_h, R.drawable.main_set_h1, R.drawable.guanbi_h};
            MENU_SMALL_ICON = new int[]{R.drawable.main_zaixiandaohang_n1, R.drawable.main_radio_n, R.drawable.main_meiti_n, R.drawable.main_phone_n1, R.drawable.yejin_n, R.drawable.shoujihulian_n, R.drawable.main_jiluyi_n, R.drawable.main_anzhuo_n, R.drawable.main_set_n1, R.drawable.guanbi_n};
        }
    }

    public static boolean isBT() {
        return LauncherApplication.isBT;
    }

    public enum DataTpye {
        NAVI_VOLUME(1),
        BT_PHONE_VOLUME(2),
        LANGUAGE(3);
        
        private int code;

        private DataTpye(int temp) {
            this.code = temp;
        }
    }
}
