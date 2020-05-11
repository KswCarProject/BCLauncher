package com.touchus.publicutils.sysconst;

public class BMWModel {
    public static String ACTION_BMW_DOOR = "com.touchus.factorytest.doorNum";
    public static String ACTION_BMW_DOOR_REVERSE = "com.touchus.factorytest.door_reverse";
    public static String ACTION_BMW_ORIGINAL = "com.touchus.factorytest.bmworiginal";
    public static String ACTION_BMW_RudderDOOR = "com.touchus.factorytest.seat";
    public static String ACTION_BMW_SIZE = "com.touchus.factorytest.bmwsize";
    public static String ACTION_BMW_TYPE = "com.touchus.factorytest.bmwtype";
    public static String DOOR_KEY = "doorNum";
    public static String IS_LEFTORRIGHT_SEAT = "is_leftorright_seat";
    public static String KEY = "bmwType";
    public static String ORIGINAL_KEY = "bmwOriginal";
    public static String SIZE_KEY = "bmwSize";
    public static boolean isRightSeat = false;

    public enum EBMWTpye {
        Other(0),
        X1(1);
        
        private int code;

        private EBMWTpye(int temp) {
            this.code = temp;
        }

        public byte getCode() {
            return (byte) this.code;
        }
    }

    public enum EBMWOriginal {
        EXIST(0),
        UNEXIST(1);
        
        private int code;

        private EBMWOriginal(int temp) {
            this.code = temp;
        }

        public byte getCode() {
            return (byte) this.code;
        }
    }
}
