package cn.kuwo.autosdk.bean;

public class DownloadQuality {

    public enum Quality {
        Q_AUTO,
        Q_LOW,
        Q_HIGH,
        Q_PERFECT,
        Q_LOSSLESS;

        public static Quality bitrate2Quality(int i) {
            return i <= 48 ? Q_LOW : i <= 128 ? Q_HIGH : i <= 320 ? Q_PERFECT : Q_LOSSLESS;
        }
    }
}
