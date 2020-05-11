package com.touchus.publicutils.sysconst;

public class BenzModel {
    public static String KEY = "benzType";
    public static String SIZE_KEY = "benzSize";
    public static EBenzCAN benzCan = EBenzCAN.ZMYT;
    public static EBenzSize benzSize = EBenzSize.size480_800;
    public static EBenzTpye benzTpye = EBenzTpye.C;

    public enum EBenzTpye {
        A(0),
        B(1),
        C(2),
        E(3),
        GLA(4),
        GLC(5),
        GLE(6),
        GLK(7),
        CLA(8),
        ML(9),
        V(10);
        
        private int code;

        private EBenzTpye(int temp) {
            this.code = temp;
        }

        public byte getCode() {
            return (byte) this.code;
        }
    }

    public enum EBenzSize {
        size480_800(0),
        size1280_480(1);
        
        private int code;

        private EBenzSize(int temp) {
            this.code = temp;
        }

        public byte getCode() {
            return (byte) this.code;
        }
    }

    public enum EBenzModel {
        BENZ_A("BENZ_A"),
        BENZ_B("BENZ_B"),
        BENZ_C("BENZ_C"),
        BENZ_GLC("BENZ_GLC"),
        BENZ_E("BENZ_E"),
        BENZ_GLE("BENZ_GLE"),
        BENZ_GLA("BENZ_GLA"),
        BENZ_GLK("BENZ_GLK"),
        BENZ_ML("BENZ_ML"),
        BENZ_V("BENZ_V"),
        BENZ_CLA("BENZ_CLA");
        
        private String name;

        private EBenzModel(String name2) {
            this.name = name2;
        }

        public String getName() {
            return this.name;
        }
    }

    public static boolean isBenzA() {
        if (benzTpye.equals(EBenzTpye.A)) {
            return true;
        }
        return false;
    }

    public static boolean isBenzB() {
        if (benzTpye.equals(EBenzTpye.B)) {
            return true;
        }
        return false;
    }

    public static boolean isBenzC() {
        if (benzTpye.equals(EBenzTpye.C)) {
            return true;
        }
        return false;
    }

    public static boolean isBenzGLC() {
        if (benzTpye.equals(EBenzTpye.GLC)) {
            return true;
        }
        return false;
    }

    public static boolean isBenzML() {
        if (benzTpye.equals(EBenzTpye.ML)) {
            return true;
        }
        return false;
    }

    public static boolean isBenzE() {
        if (benzTpye.equals(EBenzTpye.E)) {
            return true;
        }
        return false;
    }

    public static boolean isBenzGLE() {
        if (benzTpye.equals(EBenzTpye.GLE)) {
            return true;
        }
        return false;
    }

    public static boolean isBenzGLK() {
        if (benzTpye.equals(EBenzTpye.GLK)) {
            return true;
        }
        return false;
    }

    public static boolean isBenzGLA() {
        if (benzTpye.equals(EBenzTpye.GLA)) {
            return true;
        }
        return false;
    }

    public static boolean isBenzCLA() {
        if (benzTpye.equals(EBenzTpye.CLA)) {
            return true;
        }
        return false;
    }

    public static boolean isBenzV() {
        if (benzTpye.equals(EBenzTpye.V)) {
            return true;
        }
        return false;
    }

    public static String benzName() {
        if (isBenzA()) {
            return EBenzModel.BENZ_A.getName();
        }
        if (isBenzB()) {
            return EBenzModel.BENZ_B.getName();
        }
        if (isBenzC()) {
            return EBenzModel.BENZ_C.getName();
        }
        if (isBenzE()) {
            return EBenzModel.BENZ_E.getName();
        }
        if (isBenzGLA()) {
            return EBenzModel.BENZ_GLA.getName();
        }
        if (isBenzGLK()) {
            return EBenzModel.BENZ_GLK.getName();
        }
        if (isBenzGLC()) {
            return EBenzModel.BENZ_GLC.getName();
        }
        if (isBenzGLE()) {
            return EBenzModel.BENZ_GLE.getName();
        }
        if (isBenzML()) {
            return EBenzModel.BENZ_ML.getName();
        }
        if (isBenzV()) {
            return EBenzModel.BENZ_V.getName();
        }
        if (isBenzCLA()) {
            return EBenzModel.BENZ_CLA.getName();
        }
        return EBenzModel.BENZ_C.getName();
    }

    public static boolean isSUV() {
        if (benzTpye == EBenzTpye.GLA || benzTpye == EBenzTpye.GLC || benzTpye == EBenzTpye.GLE || benzTpye == EBenzTpye.GLK || benzTpye == EBenzTpye.ML) {
            return true;
        }
        return false;
    }

    public static boolean isLimo() {
        if (benzTpye == EBenzTpye.V) {
            return true;
        }
        return false;
    }

    public enum EBenzCAN {
        ZMYT("ZMYT"),
        ZHTD("ZHTD"),
        XBS("XBS"),
        WCL("WCL");
        
        private String name;

        private EBenzCAN(String name2) {
            this.name = name2;
        }

        public String getName() {
            return this.name;
        }
    }

    public enum EBenzFM {
        NAV_BT(0),
        NAV_AUX(1),
        NONAV_BT(2),
        NONAV_AUX(3);
        
        private int name;

        private EBenzFM(int name2) {
            this.name = name2;
        }

        public int getName() {
            return this.name;
        }
    }
}
