package cn.kuwo.autosdk.bean;

import android.text.TextUtils;

public enum MusicQuality {
    FLUENT {
        public String getDiscribe() {
            return "流畅";
        }
    },
    HIGHQUALITY {
        public String getDiscribe() {
            return "高品质";
        }
    },
    PERFECT {
        public String getDiscribe() {
            return "完美";
        }
    },
    LOSSLESS {
        public String getDiscribe() {
            return "无损";
        }
    };

    public static MusicQuality getQualityFromBitrate(int i) {
        return i <= 48 ? FLUENT : i <= 128 ? HIGHQUALITY : i <= 320 ? PERFECT : LOSSLESS;
    }

    public static MusicQuality getQualityFromDiscribe(String str) {
        if (str == null) {
            return FLUENT;
        }
        for (MusicQuality musicQuality : values()) {
            if (musicQuality.getDiscribe().equals(str)) {
                return musicQuality;
            }
        }
        return FLUENT;
    }

    public static MusicQuality getQualityFromDiscribe4Quku(String str) {
        return TextUtils.isEmpty(str) ? FLUENT : "s".equals(str) ? FLUENT : "h".equals(str) ? HIGHQUALITY : "p".equals(str) ? PERFECT : "pp".equals(str) ? LOSSLESS : "ff".equals(str) ? LOSSLESS : FLUENT;
    }

    /* access modifiers changed from: package-private */
    public abstract String getDiscribe();

    public boolean isEQ() {
        return this == LOSSLESS || this == PERFECT;
    }

    public boolean isFLAC() {
        return this == LOSSLESS;
    }
}
