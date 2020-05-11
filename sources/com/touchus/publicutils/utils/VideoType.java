package com.touchus.publicutils.utils;

public class VideoType {
    private static String[] videoTpye = {".rm", ".rmvb", ".avi", ".pmeg", ".wmv", ".3gp", ".mp4", ".dat", ".vob", ".flv"};

    public static boolean isVideo(String video) {
        for (String v : videoTpye) {
            if (v.equals(video.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
