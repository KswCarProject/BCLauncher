package com.touchus.benchilauncher.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.backaudio.android.driver.Mainboard;
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.benchilauncher.R;
import com.touchus.benchilauncher.SysConst;
import com.touchus.benchilauncher.inface.OnSoundClickListener;
import java.lang.ref.WeakReference;

public class EQDialog extends Dialog implements OnSoundClickListener, View.OnClickListener {
    private LauncherApplication app;
    private Context context;
    private int currentpos = -1;
    private int[] data = {10, 10, 10, 10, 10, 10, 10};
    private int[] eqModelList = {R.string.eq_custom, R.string.eq_rock, R.string.eq_knight, R.string.eq_pop, R.string.eq_classical};
    private String[] eqNameList = {"100HZ", "250HZ", "500HZ", "2kHZ", "4kHZ", "8kHZ", "16kHZ"};
    private LinearLayout eq_layout;
    private LinearLayout eq_model;
    /* access modifiers changed from: private */
    public HorizontalScrollView hScrollView;
    public MHandler mHandler = new MHandler(this);

    public EQDialog(Context context2) {
        super(context2);
        this.context = context2;
    }

    public EQDialog(Context context2, int themeResId) {
        super(context2, themeResId);
        this.context = context2;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_eq_layout);
        this.eq_layout = (LinearLayout) findViewById(R.id.eq_layout);
        this.eq_model = (LinearLayout) findViewById(R.id.eq_model);
        this.hScrollView = (HorizontalScrollView) findViewById(R.id.hscrollview);
        this.app = (LauncherApplication) this.context.getApplicationContext();
        initData();
        initView();
    }

    private void initData() {
        this.data = this.app.getEffectData(this.data);
    }

    private void initView() {
        this.eq_layout.removeAllViews();
        this.eq_model.removeAllViews();
        for (int i = 0; i < this.eqNameList.length; i++) {
            EQSetView eqSetView = new EQSetView(this.context);
            eqSetView.setId(i);
            eqSetView.setSoundValue(this.eqNameList[i], this.data[i]);
            eqSetView.setOnSoundClickListener(this);
            this.eq_layout.addView(eqSetView);
        }
        for (int i2 = 0; i2 < this.eqModelList.length; i2++) {
            this.eq_model.addView(addTextView(this.context.getString(this.eqModelList[i2]), i2));
        }
        this.eq_model.getChildAt(LauncherApplication.mSpUtils.getInt(SysConst.effectpos, 0)).performClick();
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                if (LauncherApplication.mSpUtils.getInt(SysConst.effectpos, 0) >= 2) {
                    EQDialog.this.hScrollView.fullScroll(66);
                }
            }
        }, 500);
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        this.app.registerHandler(this.mHandler);
        super.onStart();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        this.app.unregisterHandler(this.mHandler);
        super.onStop();
    }

    static class MHandler extends Handler {
        private WeakReference<EQDialog> target;

        public MHandler(EQDialog activity) {
            this.target = new WeakReference<>(activity);
        }

        public void handleMessage(Message msg) {
            if (this.target.get() != null) {
                ((EQDialog) this.target.get()).handlerMsgUSB(msg);
            }
        }
    }

    public void handlerMsgUSB(Message msg) {
        if (msg.what == 6001) {
            byte idriverCode = msg.getData().getByte(SysConst.IDRIVER_ENUM);
            if (idriverCode == Mainboard.EIdriverEnum.BACK.getCode() || idriverCode == Mainboard.EIdriverEnum.HOME.getCode() || idriverCode == Mainboard.EIdriverEnum.STAR_BTN.getCode()) {
                dismiss();
            }
        }
    }

    private TextView addTextView(String typeModel, int i) {
        TextView textView = new TextView(this.context);
        textView.setId(i);
        textView.setPadding(10, 10, 10, 10);
        textView.setText(typeModel);
        textView.setTextColor(this.context.getResources().getColor(R.color.white));
        textView.setBackgroundResource(R.drawable.eq_selector_item_beijing);
        textView.setTextSize(24.0f);
        textView.setGravity(17);
        textView.setOnClickListener(this);
        return textView;
    }

    public void click(View view, int value, boolean fromUser) {
        switch (view.getId()) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                if (this.currentpos == 0 || fromUser) {
                    this.eq_model.getChildAt(0).performClick();
                    this.data[view.getId()] = value;
                    this.app.setEffect(this.data, this.currentpos);
                    this.app.setEffectData(this.data);
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void onClick(View v) {
        if (this.currentpos != v.getId()) {
            this.currentpos = v.getId();
            for (int i = 0; i < this.eq_model.getChildCount(); i++) {
                if (this.currentpos == i) {
                    this.eq_model.getChildAt(i).setSelected(true);
                } else {
                    this.eq_model.getChildAt(i).setSelected(false);
                }
            }
            switch (this.currentpos) {
                case 0:
                    this.data = this.app.getEffectData(this.data);
                    break;
                case 1:
                    this.data = new int[]{10, 14, 17, 13, 8, 13, 10};
                    break;
                case 2:
                    this.data = new int[]{15, 13, 11, 10, 9, 12, 17};
                    break;
                case 3:
                    this.data = new int[]{8, 12, 13, 15, 13, 11, 8};
                    break;
                case 4:
                    this.data = new int[]{16, 17, 15, 11, 12, 14, 15};
                    break;
            }
            for (int i2 = 0; i2 < this.eqNameList.length; i2++) {
                ((EQSetView) this.eq_layout.getChildAt(i2)).setSoundValue(this.eqNameList[i2], this.data[i2]);
            }
            this.app.setEffect(this.data, this.currentpos);
            LauncherApplication.mSpUtils.putInt(SysConst.effectpos, this.currentpos);
        }
    }
}
