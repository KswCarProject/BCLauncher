package cn.kuwo.autosdk.bean;

import android.text.TextUtils;

public enum MusicFormat {
    NONE {
        public String getDiscribe() {
            return "NONE";
        }
    },
    AAC {
        public String getDiscribe() {
            return "aac";
        }
    },
    MP3 {
        public String getDiscribe() {
            return "mp3";
        }
    },
    MP4 {
        public String getDiscribe() {
            return "mp4";
        }
    },
    WMA {
        public String getDiscribe() {
            return "wma";
        }
    },
    APE {
        public String getDiscribe() {
            return "ape";
        }
    },
    FLAC {
        public String getDiscribe() {
            return "flac";
        }
    };

    public static MusicFormat getFormatFromDiscribe(String str) {
        if (str == null) {
            return NONE;
        }
        for (MusicFormat musicFormat : values()) {
            if (musicFormat.getDiscribe().equals(str)) {
                return musicFormat;
            }
        }
        return NONE;
    }

    public static MusicFormat getFormatFromDiscribe4Quku(String str) {
        if (TextUtils.isEmpty(str)) {
            return NONE;
        }
        for (MusicFormat musicFormat : values()) {
            if (musicFormat.getDiscribe().equals(str)) {
                return musicFormat;
            }
        }
        return NONE;
    }

    public abstract String getDiscribe();
}
