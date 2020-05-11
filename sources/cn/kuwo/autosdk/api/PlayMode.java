package cn.kuwo.autosdk.api;

public enum PlayMode {
    MODE_SINGLE_CIRCLE {
        public String getPlayMode() {
            return PlayMode.MEDIA_ONE;
        }
    },
    MODE_SINGLE_PLAY {
        public String getPlayMode() {
            return PlayMode.MEDIA_SINGLE;
        }
    },
    MODE_ALL_ORDER {
        public String getPlayMode() {
            return PlayMode.MEDIA_ORDER;
        }
    },
    MODE_ALL_CIRCLE {
        public String getPlayMode() {
            return PlayMode.MEDIA_CIRCLE;
        }
    },
    MODE_ALL_RANDOM {
        public String getPlayMode() {
            return PlayMode.MEDIA_RANDOM;
        }
    };
    
    private static final String MEDIA_CIRCLE = "MEDIA_CIRCLE";
    private static final String MEDIA_ONE = "MEDIA_ONE";
    private static final String MEDIA_ORDER = "MEDIA_ORDER";
    private static final String MEDIA_RANDOM = "MEDIA_RANDOM";
    private static final String MEDIA_SINGLE = "MEDIA_SINGLE";

    public abstract String getPlayMode();
}
