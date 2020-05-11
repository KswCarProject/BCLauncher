package cn.kuwo.autosdk.api;

public interface OnPlayerModeListener {
    public static final int MODE_ALL_CIRCLE = 2;
    public static final int MODE_ALL_ORDER = 1;
    public static final int MODE_ALL_RANDOM = 3;
    public static final int MODE_SINGLE_CIRCLE = 0;

    void onPlayerMode(int i);
}
