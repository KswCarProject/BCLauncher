package cn.kuwo.autosdk.api;

public enum PlayState {
    STATE_PRE {
        public String getPlayState() {
            return PlayState.MEDIA_PRE;
        }
    },
    STATE_NEXT {
        public String getPlayState() {
            return PlayState.MEDIA_NEXT;
        }
    },
    STATE_PAUSE {
        public String getPlayState() {
            return PlayState.MEDIA_PAUSE;
        }
    },
    STATE_PLAY {
        public String getPlayState() {
            return PlayState.MEDIA_PLAY;
        }
    };
    
    public static final String CHANGE_SONGLIST = "CHANGE_SONGLIST";
    private static final String MEDIA_NEXT = "MEDIA_NEXT";
    private static final String MEDIA_PAUSE = "MEDIA_PAUSE";
    private static final String MEDIA_PLAY = "MEDIA_PLAY";
    private static final String MEDIA_PRE = "MEDIA_PRE";

    public abstract String getPlayState();
}
