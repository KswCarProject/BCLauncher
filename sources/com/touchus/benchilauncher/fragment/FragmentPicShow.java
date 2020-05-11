package com.touchus.benchilauncher.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.backaudio.android.driver.Mainboard;
import com.squareup.picasso.Picasso;
import com.touchus.benchilauncher.Launcher;
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.benchilauncher.R;
import com.touchus.benchilauncher.SysConst;
import com.touchus.benchilauncher.base.BaseFragment;
import com.touchus.publicutils.bean.MediaBean;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FragmentPicShow extends BaseFragment implements View.OnClickListener {
    private ImageView[] arr = new ImageView[8];
    private Bitmap bitmap;
    /* access modifiers changed from: private */
    public int count = 0;
    private float fScale = 1.0f;
    private ImageView fangda;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (FragmentPicShow.this.picList.size() != 0) {
                    FragmentPicShow.this.pressItem(4);
                } else {
                    return;
                }
            }
            super.handleMessage(msg);
        }
    };
    /* access modifiers changed from: private */
    public int indexCount;
    private boolean isBofang = false;
    private LauncherApplication mApp;
    private ImageView mBofangPic;
    private byte mIDRIVERENUM;
    private Bitmap mMBitmapda;
    private FrameLayout mMFl;
    private ImageView mMFliV;
    private Launcher mMMainActivity;
    /* access modifiers changed from: private */
    public LinearLayout mMuenPic;
    public MusicLanyaHandler mMusicHandler = new MusicLanyaHandler(this);
    private TextView mPicName;
    private int mPosition;
    private View mRootView;
    private ImageView mShangPic;
    private ImageView mXiaPic;
    private int nBitmapWidth;
    private ImageView picCaidan;
    public List<MediaBean> picList;
    private ImageView suoxiao;
    TimerTask task = new TimerTask() {
        public void run() {
            Message message = new Message();
            message.what = 1;
            FragmentPicShow.this.handler.sendMessage(message);
        }
    };
    Timer timer = new Timer();
    private int turnRotate = 90;
    private String url;
    private ImageView youxuan;
    private ImageView zuoxuan;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mRootView = inflater.inflate(R.layout.fragment_pic_show, (ViewGroup) null);
        this.mMMainActivity = (Launcher) getActivity();
        this.mApp = (LauncherApplication) this.mMMainActivity.getApplication();
        this.mApp.registerHandler(this.mMusicHandler);
        this.mMFl = (FrameLayout) this.mRootView.findViewById(R.id.pic_fl);
        this.mShangPic = (ImageView) this.mRootView.findViewById(R.id.shang_pic);
        this.mXiaPic = (ImageView) this.mRootView.findViewById(R.id.xia_pic);
        this.mBofangPic = (ImageView) this.mRootView.findViewById(R.id.pic_bofang);
        this.mMuenPic = (LinearLayout) this.mRootView.findViewById(R.id.muen_pic);
        this.zuoxuan = (ImageView) this.mRootView.findViewById(R.id.zuoxuan);
        this.youxuan = (ImageView) this.mRootView.findViewById(R.id.youxuan);
        this.fangda = (ImageView) this.mRootView.findViewById(R.id.fangda);
        this.suoxiao = (ImageView) this.mRootView.findViewById(R.id.suoxiao);
        this.picCaidan = (ImageView) this.mRootView.findViewById(R.id.pic_caidan);
        this.mMFliV = (ImageView) this.mRootView.findViewById(R.id.pic_fliv);
        this.mPicName = (TextView) this.mRootView.findViewById(R.id.pic_name);
        this.arr[0] = this.fangda;
        this.arr[1] = this.suoxiao;
        this.arr[2] = this.mShangPic;
        this.arr[3] = this.mBofangPic;
        this.arr[4] = this.mXiaPic;
        this.arr[5] = this.zuoxuan;
        this.arr[6] = this.youxuan;
        this.arr[7] = this.picCaidan;
        initData();
        initListener();
        hideFangDa();
        this.zuoxuan.setOnClickListener(this);
        this.youxuan.setOnClickListener(this);
        return this.mRootView;
    }

    private void initData() {
        this.mPosition = getArguments().getInt("position");
        this.url = this.picList.get(this.mPosition).getData();
        String picName = this.picList.get(this.mPosition).getTitle();
        this.bitmap = decodeSampledBitmapFromFile(this.url, 799, 399);
        this.mMFliV.setImageBitmap(this.bitmap);
        this.mPicName.setText(picName);
    }

    private void hideFangDa() {
        if (this.fScale == 1.0f) {
            this.arr[0].setAlpha(0.5f);
        } else {
            this.arr[0].setAlpha(1.0f);
        }
    }

    private void initListener() {
        this.picCaidan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentPicShow.this.indexCount = 7;
                FragmentPicShow.this.seclectTrue(FragmentPicShow.this.indexCount);
                FragmentPicShow.this.pressItem(FragmentPicShow.this.indexCount);
            }
        });
        this.suoxiao.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentPicShow.this.indexCount = 1;
                FragmentPicShow.this.seclectTrue(FragmentPicShow.this.indexCount);
                FragmentPicShow.this.pressItem(FragmentPicShow.this.indexCount);
            }
        });
        this.fangda.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentPicShow.this.indexCount = 0;
                FragmentPicShow.this.seclectTrue(FragmentPicShow.this.indexCount);
                FragmentPicShow.this.pressItem(FragmentPicShow.this.indexCount);
            }
        });
        this.mBofangPic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentPicShow.this.indexCount = 3;
                FragmentPicShow.this.seclectTrue(FragmentPicShow.this.indexCount);
                FragmentPicShow.this.pressItem(FragmentPicShow.this.indexCount);
            }
        });
        this.mXiaPic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentPicShow.this.indexCount = 4;
                FragmentPicShow.this.seclectTrue(FragmentPicShow.this.indexCount);
                FragmentPicShow.this.pressItem(FragmentPicShow.this.indexCount);
            }
        });
        this.mShangPic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentPicShow.this.indexCount = 2;
                FragmentPicShow.this.seclectTrue(FragmentPicShow.this.indexCount);
                FragmentPicShow.this.pressItem(FragmentPicShow.this.indexCount);
            }
        });
        this.mMFl.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentPicShow fragmentPicShow = FragmentPicShow.this;
                fragmentPicShow.count = fragmentPicShow.count + 1;
                if (FragmentPicShow.this.count % 2 == 0) {
                    FragmentPicShow.this.mMuenPic.setVisibility(8);
                } else {
                    FragmentPicShow.this.mMuenPic.setVisibility(0);
                }
            }
        });
    }

    public boolean onBack() {
        return false;
    }

    private void dealScale() {
        Matrix matrix = new Matrix();
        matrix.setScale(this.fScale, this.fScale);
        this.mMFliV.setImageBitmap(Bitmap.createBitmap(this.bitmap, 0, 0, this.bitmap.getWidth(), this.bitmap.getHeight(), matrix, true));
    }

    public Bitmap toturn(Bitmap img) {
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.postRotate((float) this.turnRotate);
        return Bitmap.createBitmap(img, 0, 0, this.bitmap.getWidth(), this.bitmap.getHeight(), matrix, true);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.zuoxuan) {
            this.indexCount = 5;
        } else if (view.getId() == R.id.youxuan) {
            this.indexCount = 6;
        }
        seclectTrue(this.indexCount);
        pressItem(this.indexCount);
    }

    public static Bitmap decodeSampledBitmapFromFile(String filepath, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filepath, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filepath, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int picheight = options.outHeight;
        int picwidth = options.outWidth;
        int targetheight = picheight;
        int targetwidth = picwidth;
        int inSampleSize = 1;
        if (targetheight > reqHeight || targetwidth > reqWidth) {
            while (targetheight >= reqHeight && targetwidth >= reqWidth) {
                inSampleSize++;
                targetheight = picheight / inSampleSize;
                targetwidth = picwidth / inSampleSize;
            }
        }
        return inSampleSize;
    }

    /* access modifiers changed from: private */
    public void seclectTrue(int seclectCuttun) {
        for (int i = 0; i < this.arr.length; i++) {
            if (seclectCuttun == i) {
                this.arr[i].setSelected(true);
            } else {
                this.arr[i].setSelected(false);
            }
        }
    }

    public void onDestroy() {
        this.mApp.unregisterHandler(this.mMusicHandler);
        if (this.task != null) {
            this.task.cancel();
        }
        super.onDestroy();
    }

    static class MusicLanyaHandler extends Handler {
        private WeakReference<FragmentPicShow> target;

        public MusicLanyaHandler(FragmentPicShow activity) {
            this.target = new WeakReference<>(activity);
        }

        public void handleMessage(Message msg) {
            if (this.target.get() != null) {
                ((FragmentPicShow) this.target.get()).handlerMsg(msg);
            }
        }
    }

    public void handlerMsg(Message msg) {
        if (msg.what == 6001) {
            this.mIDRIVERENUM = msg.getData().getByte(SysConst.IDRIVER_ENUM);
            if (this.mIDRIVERENUM == Mainboard.EIdriverEnum.TURN_RIGHT.getCode() || this.mIDRIVERENUM == Mainboard.EIdriverEnum.RIGHT.getCode()) {
                this.mMuenPic.setVisibility(0);
                if (this.indexCount < 7) {
                    this.indexCount++;
                }
                seclectTrue(this.indexCount);
            } else if (this.mIDRIVERENUM == Mainboard.EIdriverEnum.PRESS.getCode()) {
                pressItem(this.indexCount);
            } else if (this.mIDRIVERENUM != Mainboard.EIdriverEnum.UP.getCode() && this.mIDRIVERENUM != Mainboard.EIdriverEnum.DOWN.getCode()) {
                if (this.mIDRIVERENUM == Mainboard.EIdriverEnum.TURN_LEFT.getCode() || this.mIDRIVERENUM == Mainboard.EIdriverEnum.LEFT.getCode()) {
                    this.mMuenPic.setVisibility(0);
                    if (this.indexCount > 0) {
                        this.indexCount--;
                    }
                    seclectTrue(this.indexCount);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void pressItem(int indexCount2) {
        switch (indexCount2) {
            case 0:
                hideFangDa();
                if (this.bitmap != null) {
                    this.fScale += 0.2f;
                    if (this.fScale > 1.0f) {
                        this.fScale = 1.0f;
                    }
                    if (this.nBitmapWidth <= 0) {
                        this.nBitmapWidth = this.bitmap.getWidth();
                    }
                    dealScale();
                    return;
                }
                return;
            case 1:
                hideFangDa();
                if (this.bitmap != null) {
                    this.fScale -= 0.2f;
                    if (((double) this.fScale) < 0.2d) {
                        this.fScale = 0.2f;
                    }
                    if (this.nBitmapWidth <= 0) {
                        this.nBitmapWidth = this.bitmap.getWidth();
                    }
                    dealScale();
                    return;
                }
                return;
            case 2:
                if (this.mPosition > 0) {
                    List<MediaBean> list = this.picList;
                    int i = this.mPosition - 1;
                    this.mPosition = i;
                    String urll = list.get(i).getData();
                    File file = new File(urll);
                    this.bitmap = decodeSampledBitmapFromFile(urll, 799, 399);
                    Picasso.with(getActivity()).load(file).into(this.mMFliV);
                    this.mPicName.setText(this.picList.get(this.mPosition).getTitle());
                    LauncherApplication.imageIndex = this.mPosition;
                    return;
                }
                return;
            case 3:
                if (!this.isBofang) {
                    if (this.task != null) {
                        this.task.cancel();
                        this.task = null;
                        this.task = new TimerTask() {
                            public void run() {
                                Message message = new Message();
                                message.what = 1;
                                FragmentPicShow.this.handler.sendMessage(message);
                            }
                        };
                    }
                    this.timer.schedule(this.task, 1000, 2000);
                    this.isBofang = !this.isBofang;
                    this.mBofangPic.setImageResource(R.drawable.tupian_xia_zanting);
                    return;
                }
                this.task.cancel();
                this.isBofang = !this.isBofang;
                this.mBofangPic.setImageResource(R.drawable.tupian_xia_bofang);
                return;
            case 4:
                if (this.mPosition < this.picList.size() - 1) {
                    List<MediaBean> list2 = this.picList;
                    int i2 = this.mPosition + 1;
                    this.mPosition = i2;
                    String urlll = list2.get(i2).getData();
                    this.mPicName.setText(this.picList.get(this.mPosition).getTitle());
                    this.bitmap = decodeSampledBitmapFromFile(urlll, 799, 399);
                    Picasso.with(getActivity()).load(new File(urlll)).into(this.mMFliV);
                    LauncherApplication.imageIndex = this.mPosition;
                    return;
                }
                return;
            case 5:
                if (this.bitmap != null) {
                    this.turnRotate = -90;
                    this.bitmap = toturn(this.bitmap);
                    this.mMFliV.setImageBitmap(this.bitmap);
                    return;
                }
                return;
            case 6:
                if (this.bitmap != null) {
                    this.turnRotate = 90;
                    this.bitmap = toturn(this.bitmap);
                    this.mMFliV.setImageBitmap(this.bitmap);
                    return;
                }
                return;
            case 7:
                this.mMMainActivity.handleBackAction();
                return;
            default:
                return;
        }
    }
}
