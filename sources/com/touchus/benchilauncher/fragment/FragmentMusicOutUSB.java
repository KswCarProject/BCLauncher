package com.touchus.benchilauncher.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.backaudio.android.driver.Mainboard;
import com.touchus.benchilauncher.Launcher;
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.benchilauncher.R;
import com.touchus.benchilauncher.SysConst;
import com.touchus.benchilauncher.adapter.MediaAdapter;
import com.touchus.benchilauncher.base.BaseFragment;
import com.touchus.benchilauncher.service.MusicLoader;
import com.touchus.benchilauncher.service.MusicPlayControl;
import com.touchus.benchilauncher.service.PictrueUtil;
import com.touchus.publicutils.bean.MediaBean;
import com.touchus.publicutils.media.VideoService;
import com.touchus.publicutils.utils.UtilTools;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class FragmentMusicOutUSB extends BaseFragment implements AdapterView.OnItemClickListener {
    boolean isUSB1 = false;
    private MediaAdapter mAdapter;
    private LauncherApplication mApp;
    private Launcher mContext;
    private int mCount = 0;
    private byte mIDRIVERENUM;
    private ListView mListView;
    public MusicLanyaHandler mMusicHandler = new MusicLanyaHandler(this);
    private View mRootView;
    private List<MediaBean> mediaBeans = new ArrayList();
    private TextView noDataTv;
    private int type = 0;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mRootView = inflater.inflate(R.layout.fragment_music_out_usb, (ViewGroup) null);
        this.mContext = (Launcher) getActivity();
        this.mApp = (LauncherApplication) this.mContext.getApplication();
        initView();
        return this.mRootView;
    }

    public void onStart() {
        this.mApp.registerHandler(this.mMusicHandler);
        if (UtilTools.checkUSBExist() && this.mApp.isScanner) {
            this.mApp.service.createLoadingFloatView(getString(R.string.msg_usb_loading));
        }
        initList();
        super.onStart();
    }

    private void initList() {
        if (LauncherApplication.menuSelectType == 1) {
            this.type = 1;
            artVedio();
        } else if (LauncherApplication.menuSelectType == 2) {
            this.type = 2;
            artPic();
        } else {
            this.type = 0;
            artMusic();
            if (LauncherApplication.iPlayingAuto && LauncherApplication.iPlaying) {
                changeToPlayMusicView();
            }
        }
        if (this.mediaBeans.size() == 0) {
            this.noDataTv.setVisibility(0);
        } else {
            this.noDataTv.setVisibility(8);
        }
    }

    private void initView() {
        this.mListView = (ListView) this.mRootView.findViewById(R.id.listview);
        this.noDataTv = (TextView) this.mRootView.findViewById(R.id.norecord);
        this.noDataTv.setText(getText(R.string.no_media_data));
        this.mAdapter = new MediaAdapter(this.mContext, this.mediaBeans);
        this.mListView.setAdapter(this.mAdapter);
        this.mListView.setOnItemClickListener(this);
    }

    public boolean onBack() {
        LauncherApplication.iPlayingAuto = false;
        return false;
    }

    static class MusicLanyaHandler extends Handler {
        private WeakReference<FragmentMusicOutUSB> target;

        public MusicLanyaHandler(FragmentMusicOutUSB activity) {
            this.target = new WeakReference<>(activity);
        }

        public void handleMessage(Message msg) {
            if (this.target.get() != null) {
                ((FragmentMusicOutUSB) this.target.get()).handlerMsg(msg);
            }
        }
    }

    public void handlerMsg(Message msg) {
        if (msg.what == 6001) {
            if (this.type == 0) {
                this.mCount = LauncherApplication.musicIndex;
            } else if (this.type == 1) {
                this.mCount = LauncherApplication.videoIndex;
            } else if (this.type == 2) {
                this.mCount = LauncherApplication.imageIndex;
            }
            this.mIDRIVERENUM = msg.getData().getByte(SysConst.IDRIVER_ENUM);
            if (this.mIDRIVERENUM == Mainboard.EIdriverEnum.TURN_RIGHT.getCode() || this.mIDRIVERENUM == Mainboard.EIdriverEnum.DOWN.getCode()) {
                down(0);
            } else if (this.mIDRIVERENUM == Mainboard.EIdriverEnum.TURN_LEFT.getCode() || this.mIDRIVERENUM == Mainboard.EIdriverEnum.UP.getCode()) {
                up(0);
            } else if (!(this.mIDRIVERENUM == Mainboard.EIdriverEnum.RIGHT.getCode() || this.mIDRIVERENUM == Mainboard.EIdriverEnum.LEFT.getCode() || this.mIDRIVERENUM != Mainboard.EIdriverEnum.PRESS.getCode())) {
                if (this.type == 0) {
                    changeToPlayMusicView();
                } else if (this.type == 1) {
                    changeToPlayVideoView();
                } else if (this.type == 2) {
                    changeToPicShowiew();
                }
            }
            if (this.type == 0) {
                LauncherApplication.musicIndex = this.mCount;
            } else if (this.type == 1) {
                LauncherApplication.videoIndex = this.mCount;
            } else if (this.type == 2) {
                LauncherApplication.imageIndex = this.mCount;
            }
        } else if (msg.what == 7002) {
            String action = msg.getData().getString(SysConst.FLAG_USB_LISTENER);
            if (!"android.intent.action.MEDIA_CHECKING".equals(action) && !"android.intent.action.MEDIA_MOUNTED".equals(action)) {
                if ("android.intent.action.MEDIA_EJECT".equals(action)) {
                    this.mApp.service.removeLoadingFloatView();
                    this.mediaBeans.clear();
                    this.mAdapter.notifyDataSetChanged();
                } else if ("android.intent.action.MEDIA_SCANNER_STARTED".equals(action)) {
                    if (this.mApp.serviceHandler != null) {
                        this.mApp.service.createLoadingFloatView(getString(R.string.msg_usb_loading));
                    }
                } else if (!"android.intent.action.MEDIA_UNMOUNTED".equals(action) && "android.intent.action.MEDIA_SCANNER_FINISHED".equals(action)) {
                    this.mApp.service.removeLoadingFloatView();
                    initList();
                }
            }
        } else if (msg.what == 1031) {
            initList();
        }
    }

    public void up(int seclectIndex) {
        int firstVisiblePosition = this.mListView.getFirstVisiblePosition();
        if (seclectIndex == 0) {
            if (this.mCount > 0) {
                this.mCount--;
            }
            int seclectIndex2 = this.mCount;
            if (seclectIndex2 < firstVisiblePosition + 1) {
                this.mListView.smoothScrollToPositionFromTop(seclectIndex2 - 3, 0);
            }
        } else {
            int lastVisiblePosition = this.mListView.getLastVisiblePosition();
            this.mCount += seclectIndex;
            if (this.mCount < 0) {
                this.mCount = 0;
            }
            this.mListView.smoothScrollToPositionFromTop(lastVisiblePosition + seclectIndex, 0);
        }
        this.mAdapter.setSeclectIndex(this.mCount);
        this.mAdapter.notifyDataSetChanged(this.type);
    }

    public void down(int seclectIndex) {
        int lastVisiblePosition = this.mListView.getLastVisiblePosition();
        if (seclectIndex == 0) {
            if (this.mCount < this.mediaBeans.size() - 1) {
                this.mCount++;
            }
            int seclectIndex2 = this.mCount;
            if (seclectIndex2 > lastVisiblePosition - 1) {
                this.mListView.smoothScrollToPositionFromTop(seclectIndex2, 0);
            }
        } else {
            int firstVisiblePosition = this.mListView.getFirstVisiblePosition();
            this.mCount += seclectIndex;
            if (this.mCount > this.mediaBeans.size() - 1) {
                this.mCount = this.mediaBeans.size() - 1;
            }
            this.mListView.smoothScrollToPositionFromTop(firstVisiblePosition + seclectIndex, 0);
        }
        this.mAdapter.setSeclectIndex(this.mCount);
        this.mAdapter.notifyDataSetChanged(this.type);
    }

    private void artMusic() {
        if (getActivity() != null) {
            if (this.mApp.musicPlayControl == null) {
                this.mApp.musicPlayControl = new MusicPlayControl(getActivity());
            }
            if (LauncherApplication.musicList.size() == 0) {
                LauncherApplication.musicList = MusicLoader.instance(this.mContext.getContentResolver()).getMusicList();
            }
            this.mApp.musicPlayControl.setPlayList(LauncherApplication.musicList);
            this.mediaBeans.clear();
            this.mediaBeans.addAll(LauncherApplication.musicList);
            if (LauncherApplication.musicIndex >= this.mediaBeans.size()) {
                LauncherApplication.musicIndex = 0;
                LauncherApplication.mSpUtils.putInt(SysConst.FLAG_MUSIC_POS, 0);
            }
            this.mAdapter.setSeclectIndex(LauncherApplication.musicIndex);
            this.mListView.setSelection(LauncherApplication.musicIndex);
            this.mAdapter.notifyDataSetChanged(this.type);
        }
    }

    private void artPic() {
        if (getActivity() != null) {
            if (LauncherApplication.imageList.size() == 0) {
                LauncherApplication.imageList = new PictrueUtil(this.mContext).getPicList();
            }
            this.mediaBeans.clear();
            this.mediaBeans.addAll(LauncherApplication.imageList);
            this.mAdapter.setSeclectIndex(LauncherApplication.imageIndex);
            this.mListView.setSelection(LauncherApplication.imageIndex);
            this.mAdapter.notifyDataSetChanged(this.type);
        }
    }

    private void artVedio() {
        if (getActivity() != null) {
            if (LauncherApplication.videoList.size() == 0) {
                LauncherApplication.videoList = new VideoService(this.mContext).getVideoList();
            }
            this.mediaBeans.clear();
            this.mediaBeans.addAll(LauncherApplication.videoList);
            if (LauncherApplication.videoIndex >= this.mediaBeans.size()) {
                LauncherApplication.videoIndex = 0;
                LauncherApplication.mSpUtils.putInt(SysConst.FLAG_VIDEO_POS, 0);
            }
            this.mAdapter.setSeclectIndex(LauncherApplication.videoIndex);
            this.mListView.setSelection(LauncherApplication.videoIndex);
            this.mAdapter.notifyDataSetChanged(this.type);
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        view.setSelected(true);
        if (this.type == 0) {
            LauncherApplication.musicIndex = position;
            changeToPlayMusicView();
        } else if (this.type == 1) {
            LauncherApplication.videoIndex = position;
            changeToPlayVideoView();
        } else {
            LauncherApplication.imageIndex = position;
            changeToPicShowiew();
        }
    }

    private void changeToPlayMusicView() {
        if (LauncherApplication.musicList.size() != 0) {
            FragmentMusicShow fragmentShow = new FragmentMusicShow();
            this.mContext.changeRightTo(fragmentShow);
            Bundle bundle = new Bundle();
            bundle.putInt("position", LauncherApplication.musicIndex);
            fragmentShow.setArguments(bundle);
        }
    }

    private void changeToPlayVideoView() {
        if (LauncherApplication.videoList.size() != 0) {
            FragmentVideoPlay fragmentShow = new FragmentVideoPlay();
            fragmentShow.mediaBeans = this.mediaBeans;
            Bundle bundle = new Bundle();
            bundle.putInt("position", LauncherApplication.videoIndex);
            fragmentShow.setArguments(bundle);
            this.mContext.changeToPlayVideo(fragmentShow);
        }
    }

    private void changeToPicShowiew() {
        if (LauncherApplication.imageList.size() != 0) {
            FragmentPicShow fragmentShow = new FragmentPicShow();
            this.mContext.changeRightTo(fragmentShow);
            fragmentShow.picList = LauncherApplication.imageList;
            Bundle bundle = new Bundle();
            bundle.putInt("position", LauncherApplication.imageIndex);
            fragmentShow.setArguments(bundle);
        }
    }

    public void onStop() {
        this.mApp.unregisterHandler(this.mMusicHandler);
        super.onStop();
    }

    public void onDestroy() {
        super.onDestroy();
    }
}
