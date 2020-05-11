package com.touchus.benchilauncher.views;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.backaudio.android.driver.Mainboard;
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.benchilauncher.R;
import com.touchus.benchilauncher.SysConst;
import com.touchus.benchilauncher.adapter.AppsetItemAdapter;
import com.touchus.benchilauncher.bean.AppInfo;
import com.touchus.publicutils.utils.APPSettings;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppSettingDialog extends Dialog implements AdapterView.OnItemClickListener {
    private LauncherApplication app;
    private List<AppInfo> appInfos = new ArrayList();
    private Context context;
    int currentSelectIndex;
    private AppsetItemAdapter mAdapter;
    public MHandler mHandler = new MHandler(this);
    private ListView mListView;

    public AppSettingDialog(Context context2) {
        super(context2);
        this.context = context2;
    }

    public AppSettingDialog(Context context2, int themeResId) {
        super(context2, themeResId);
        this.context = context2;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.dialog_appsetting_layout);
        this.app = (LauncherApplication) this.context.getApplicationContext();
        initData();
        this.mListView = (ListView) findViewById(R.id.list);
        this.mAdapter = new AppsetItemAdapter(this.app, this.context, this.appInfos);
        this.mListView.setAdapter(this.mAdapter);
        this.mListView.setOnItemClickListener(this);
        super.onCreate(savedInstanceState);
    }

    private void initData() {
        this.appInfos.clear();
        List<PackageInfo> packages = this.context.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            if (APPSettings.isNavgation(packageInfo.packageName)) {
                AppInfo tmpInfo = new AppInfo();
                tmpInfo.setAppName(packageInfo.applicationInfo.loadLabel(this.context.getPackageManager()).toString());
                tmpInfo.setPackageName(packageInfo.packageName);
                tmpInfo.setVersionName(packageInfo.versionName);
                tmpInfo.setVersionCode(packageInfo.versionCode);
                tmpInfo.setAppIcon(packageInfo.applicationInfo.loadIcon(this.context.getPackageManager()));
                tmpInfo.setiCheck(this.app.getNaviAPP().equals(packageInfo.packageName));
                this.appInfos.add(tmpInfo);
            }
        }
        for (int i2 = 0; i2 < this.appInfos.size(); i2++) {
            if (this.appInfos.get(i2).isiCheck()) {
                Collections.swap(this.appInfos, 0, i2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        this.app.registerHandler(this.mHandler);
        super.onStart();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        this.mHandler.removeCallbacksAndMessages((Object) null);
        this.app.unregisterHandler(this.mHandler);
        super.onStop();
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        pressItem(position);
    }

    private void pressItem(int position) {
        this.currentSelectIndex = position;
        this.app.setNaviAPP(this.appInfos.get(position).getPackageName());
        this.mAdapter.setSeclectIndex(position);
    }

    static class MHandler extends Handler {
        private WeakReference<AppSettingDialog> target;

        public MHandler(AppSettingDialog activity) {
            this.target = new WeakReference<>(activity);
        }

        public void handleMessage(Message msg) {
            if (this.target.get() != null) {
                ((AppSettingDialog) this.target.get()).handlerMsg(msg);
            }
        }
    }

    public void handlerMsg(Message msg) {
        if (msg.what == 6001) {
            byte idriverCode = msg.getData().getByte(SysConst.IDRIVER_ENUM);
            if (idriverCode == Mainboard.EIdriverEnum.TURN_RIGHT.getCode() || idriverCode == Mainboard.EIdriverEnum.DOWN.getCode()) {
                if (this.currentSelectIndex < this.appInfos.size() - 1) {
                    this.currentSelectIndex++;
                }
                seclectTrue(this.currentSelectIndex);
            } else if (idriverCode == Mainboard.EIdriverEnum.TURN_LEFT.getCode() || idriverCode == Mainboard.EIdriverEnum.UP.getCode()) {
                if (this.currentSelectIndex > 0) {
                    this.currentSelectIndex--;
                }
                seclectTrue(this.currentSelectIndex);
            } else if (idriverCode == Mainboard.EIdriverEnum.PRESS.getCode()) {
                pressItem(this.currentSelectIndex);
            } else if (idriverCode == Mainboard.EIdriverEnum.BACK.getCode() || idriverCode == Mainboard.EIdriverEnum.HOME.getCode() || idriverCode == Mainboard.EIdriverEnum.STAR_BTN.getCode()) {
                dismiss();
            }
        }
    }

    private void seclectTrue(int currentSelectIndex2) {
        this.mAdapter.setSeclectIndex(currentSelectIndex2);
    }
}
