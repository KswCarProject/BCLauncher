package com.touchus.benchilauncher.fragment;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.backaudio.android.driver.Mainboard;
import com.touchus.benchilauncher.Launcher;
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.benchilauncher.R;
import com.touchus.benchilauncher.SysConst;
import com.touchus.benchilauncher.base.BaseFragment;
import com.touchus.benchilauncher.service.OtherMediaControl;
import com.touchus.benchilauncher.views.UnInstallDialog;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class FragmentYingyong extends BaseFragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    BroadcastReceiver appBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            FragmentYingyong.this.initData();
        }
    };
    /* access modifiers changed from: private */
    public int currentPage = 0;
    /* access modifiers changed from: private */
    public LinearLayout dot_container;
    /* access modifiers changed from: private */
    public int lastPage = 0;
    private LauncherApplication mApp;
    public AppHandler mAppHandler = new AppHandler(this);
    private Launcher mContext;
    private List<ResolveInfo> mNeedShowApps = new ArrayList();
    private View mRootView;
    private int pageCount;
    /* access modifiers changed from: private */
    public int selectIndex = 0;
    /* access modifiers changed from: private */
    public ArrayList<GridView> viewPageListData;
    private ViewPager viewPager;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mRootView = inflater.inflate(R.layout.fragment_yingyong, (ViewGroup) null);
        this.mContext = (Launcher) getActivity();
        initView();
        initData();
        initEvent();
        addAppReceiver();
        return this.mRootView;
    }

    public void onResume() {
        this.mApp = (LauncherApplication) this.mContext.getApplication();
        this.mApp.registerHandler(this.mAppHandler);
        super.onResume();
    }

    public void onPause() {
        this.mApp.unregisterHandler(this.mAppHandler);
        super.onPause();
    }

    public void onDestroy() {
        unAppReceiver();
        super.onDestroy();
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.selectIndex = (this.currentPage * 8) + position;
        ((AppsGridAdapter) parent.getAdapter()).notifyDataSetChanged();
        startApp();
    }

    public boolean onItemLongClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
        UnInstallDialog dialog = new UnInstallDialog(this.mContext, R.style.Dialog);
        Window w = dialog.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.x = 260;
        w.setAttributes(lp);
        ResolveInfo info = this.mNeedShowApps.get((this.currentPage * 8) + arg2);
        if (info.activityInfo != null) {
            String pkg = info.activityInfo.packageName;
            String cls = info.activityInfo.loadLabel(this.mContext.getPackageManager()).toString();
            if (!"com.ecar.AppManager".equals(pkg) && !"com.coagent.ecar".equals(pkg)) {
                dialog.setPkgName(pkg, cls);
                dialog.show();
            }
        }
        return false;
    }

    public boolean onBack() {
        return false;
    }

    private void startApp() {
        ResolveInfo info = this.mNeedShowApps.get(this.selectIndex);
        if (Build.MODEL.contains("c200_jly") && info.activityInfo == null && this.selectIndex == 0) {
            this.mApp.service.iIsDV = true;
            this.mApp.service.createNoTouchScreens();
            this.mApp.requestDVAudioFocus();
            Mainboard.getInstance().showCarLayer(Mainboard.ECarLayer.DV);
            Mainboard.getInstance().setDVAudio(true);
        } else if (!Build.MODEL.contains("c200_hy") && info.activityInfo == null && this.selectIndex == 0) {
            this.mApp.service.iIsInRecorder = true;
            this.mApp.service.createNoTouchScreens();
            Mainboard.getInstance().showCarLayer(Mainboard.ECarLayer.RECORDER);
        } else {
            String pkg = info.activityInfo.packageName;
            String cls = info.activityInfo.name;
            if (pkg.equals("com.tima.carnet.vt")) {
                this.mApp.startTimaService();
            }
            ComponentName componet = new ComponentName(pkg, cls);
            Intent i = new Intent();
            i.addFlags(268435456);
            i.addFlags(AccessibilityEventCompat.TYPE_TOUCH_INTERACTION_END);
            i.setComponent(componet);
            this.mContext.startActivity(i);
            if (pkg.equals("com.pbi.liveitv")) {
                new OtherMediaControl(pkg, 7);
            } else if (pkg.equals("com.l2tv.ltv")) {
                new OtherMediaControl(pkg, 8);
            } else if (pkg.equals("tv.fourgtv.fourgtv")) {
                new OtherMediaControl(pkg, 9);
            } else if (pkg.equals("mbinc12.mb32")) {
                new OtherMediaControl(pkg, 10);
            }
        }
    }

    public void initView() {
        this.dot_container = (LinearLayout) this.mRootView.findViewById(R.id.dot_container);
        this.viewPager = (ViewPager) this.mRootView.findViewById(R.id.app_viewpager);
    }

    public void initData() {
        this.currentPage = 0;
        loadApps();
        initDot();
        initViewPageData();
        this.viewPager.setAdapter(new AppPagerAdapter(this.mContext, this.viewPageListData));
        this.viewPager.setOffscreenPageLimit(this.viewPageListData.size());
    }

    public void initEvent() {
        this.viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageSelected(int arg0) {
                FragmentYingyong.this.clearAllDotState();
                FragmentYingyong.this.dot_container.getChildAt(arg0).setSelected(true);
                FragmentYingyong.this.currentPage = arg0;
            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            public void onPageScrollStateChanged(int arg0) {
                if (arg0 == 1) {
                    FragmentYingyong.this.lastPage = FragmentYingyong.this.currentPage;
                } else if (arg0 == 0) {
                    if (FragmentYingyong.this.lastPage > FragmentYingyong.this.currentPage) {
                        FragmentYingyong.this.selectIndex = (FragmentYingyong.this.lastPage * 8) - 1;
                    } else if (FragmentYingyong.this.lastPage < FragmentYingyong.this.currentPage) {
                        FragmentYingyong.this.selectIndex = FragmentYingyong.this.currentPage * 8;
                    }
                    ((AppsGridAdapter) ((GridView) FragmentYingyong.this.viewPageListData.get(FragmentYingyong.this.currentPage)).getAdapter()).notifyDataSetChanged();
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void clearAllDotState() {
        for (int i = 0; i < this.dot_container.getChildCount(); i++) {
            this.dot_container.getChildAt(i).setSelected(false);
        }
    }

    private void initDot() {
        this.pageCount = 1;
        if (this.mNeedShowApps != null) {
            this.pageCount = ((this.mNeedShowApps.size() - 1) / 8) + 1;
        }
        if (this.dot_container.getChildCount() > 0) {
            this.dot_container.removeAllViews();
        }
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        for (int i = 0; i < this.pageCount; i++) {
            ImageView img = new ImageView(this.mContext);
            img.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
            img.setImageResource(R.drawable.bt_selector_yingyong_dian);
            if (i != 0) {
                layoutParams.setMargins(20, 0, 0, 0);
                img.setLayoutParams(layoutParams);
            } else {
                img.setSelected(true);
            }
            this.dot_container.addView(img);
        }
    }

    private void initViewPageData() {
        this.viewPageListData = new ArrayList<>();
        for (int i = 0; i < this.pageCount; i++) {
            GridView mGridView = new GridView(this.mContext);
            mGridView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
            mGridView.setNumColumns(4);
            ArrayList<ResolveInfo> mLists1 = new ArrayList<>();
            int star = i * 8;
            int end = (i + 1) * 8;
            if (end > this.mNeedShowApps.size()) {
                end = this.mNeedShowApps.size();
            }
            for (int j = star; j < end; j++) {
                mLists1.add(this.mNeedShowApps.get(j));
            }
            mGridView.setAdapter(new AppsGridAdapter(this.mContext, mLists1, i));
            mGridView.setSelector(new ColorDrawable(0));
            mGridView.setOnItemClickListener(this);
            mGridView.setOnItemLongClickListener(this);
            this.viewPageListData.add(mGridView);
        }
    }

    private void loadApps() {
        Intent mainIntent = new Intent("android.intent.action.MAIN", (Uri) null);
        mainIntent.addCategory("android.intent.category.LAUNCHER");
        List<ResolveInfo> temps = this.mContext.getPackageManager().queryIntentActivities(mainIntent, 0);
        this.mNeedShowApps.clear();
        for (int i = 0; i < temps.size(); i++) {
            ResolveInfo resolveInfo = temps.get(i);
            boolean iIsSystem = false;
            String curPkg = resolveInfo.activityInfo.packageName;
            try {
                if ((this.mContext.getPackageManager().getApplicationInfo(curPkg, 0).flags & 1) > 0) {
                    iIsSystem = true;
                } else {
                    iIsSystem = false;
                }
            } catch (Exception e) {
            }
            if ((!iIsSystem && !curPkg.equals("com.touchus.factorytest") && !curPkg.equals("com.touchus.benchilauncher")) || curPkg.equals("com.coagent.ecar") || curPkg.equals("com.coagent.voip") || curPkg.equals("com.mediatek.filemanager") || curPkg.equals("com.autonavi.amapauto") || curPkg.equals("com.unibroad.voiceassistant") || curPkg.equals("com.uc.browser.hd") || curPkg.equals("com.android.vending") || curPkg.equals("com.android.chrome") || curPkg.equals("com.google.android.apps.maps") || curPkg.equals("com.google.android.youtube") || curPkg.equals("com.google.android.gms") || curPkg.equals("com.papago.s1OBU") || curPkg.equals("com.tima.carnet.vt") || curPkg.equals("eu.chainfire.supersu") || curPkg.equals("com.vipercn.viper4android_v2") || curPkg.equals("cn.manstep.phonemirrorBox")) {
                this.mNeedShowApps.add(resolveInfo);
            }
        }
        if (SysConst.DV_CUSTOM) {
            this.mNeedShowApps.add(0, new ResolveInfo());
        }
        if (Build.MODEL.equals("c200_en") || SysConst.LANGUAGE == 0) {
            this.mNeedShowApps.add(0, new ResolveInfo());
        }
    }

    class AppPagerAdapter extends PagerAdapter {
        private ArrayList<GridView> listData;
        private Context mContext;

        public AppPagerAdapter(Context context, ArrayList<GridView> _list) {
            this.mContext = context;
            this.listData = _list;
        }

        public int getCount() {
            return this.listData.size();
        }

        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(this.listData.get(position));
        }

        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(this.listData.get(position), position);
            return this.listData.get(position);
        }
    }

    class AppsGridAdapter extends BaseAdapter {
        private int currentPage = 0;
        private List<ResolveInfo> listData;
        private Context mContext;

        public AppsGridAdapter(Context context, ArrayList<ResolveInfo> _list, int page) {
            this.mContext = context;
            this.listData = _list;
            this.currentPage = page;
        }

        public void setData(List<ResolveInfo> apps) {
            this.listData = apps;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(this.mContext, R.layout.home_menu_item, (ViewGroup) null);
            }
            ImageView mIv = (ImageView) convertView.findViewById(R.id.yinying_tubiao);
            TextView mTv = (TextView) convertView.findViewById(R.id.yinying_name);
            LinearLayout pp = (LinearLayout) convertView.findViewById(R.id.pp);
            ResolveInfo info = this.listData.get(position);
            if (info.activityInfo != null || this.currentPage != 0 || position != 0) {
                mIv.setImageDrawable(info.activityInfo.loadIcon(this.mContext.getPackageManager()));
                mTv.setText(info.activityInfo.loadLabel(this.mContext.getPackageManager()).toString());
            } else if (SysConst.DV_CUSTOM) {
                mIv.setImageResource(R.drawable.icon_aux);
                mTv.setText(R.string.name_connect_AUX);
            } else {
                mIv.setImageResource(R.drawable.icon_apps_record);
                mTv.setText(R.string.name_main_recorder);
            }
            if (FragmentYingyong.this.selectIndex == (this.currentPage * 8) + position) {
                pp.setBackgroundResource(R.drawable.bt_selector_yingyong_tubiao);
            } else {
                pp.setBackgroundResource(R.drawable.bt_selector_yingyong_no_tubiao);
            }
            return convertView;
        }

        public final int getCount() {
            return this.listData.size();
        }

        public final Object getItem(int position) {
            return this.listData.get(position);
        }

        public final long getItemId(int position) {
            return (long) position;
        }
    }

    class AppHandler extends Handler {
        private WeakReference<FragmentYingyong> target;

        public AppHandler(FragmentYingyong activity) {
            this.target = new WeakReference<>(activity);
        }

        public void handleMessage(Message msg) {
            if (this.target.get() != null) {
                ((FragmentYingyong) this.target.get()).handlerMsg(msg);
            }
        }
    }

    public void handlerMsg(Message msg) {
        if (msg.what == 6001) {
            byte code = msg.getData().getByte(SysConst.IDRIVER_ENUM);
            if (code == Mainboard.EIdriverEnum.TURN_RIGHT.getCode() || code == Mainboard.EIdriverEnum.RIGHT.getCode()) {
                this.selectIndex++;
                if (this.selectIndex >= this.mNeedShowApps.size()) {
                    this.selectIndex = this.mNeedShowApps.size() - 1;
                }
                int cur = this.selectIndex / 8;
                if (cur != this.currentPage) {
                    this.viewPager.setCurrentItem(cur, true);
                }
                ((AppsGridAdapter) this.viewPageListData.get(this.currentPage).getAdapter()).notifyDataSetChanged();
            } else if (code == Mainboard.EIdriverEnum.TURN_LEFT.getCode() || code == Mainboard.EIdriverEnum.LEFT.getCode()) {
                this.selectIndex--;
                if (this.selectIndex <= 0) {
                    this.selectIndex = 0;
                }
                int cur2 = this.selectIndex / 8;
                if (cur2 != this.currentPage) {
                    this.viewPager.setCurrentItem(cur2, true);
                }
                ((AppsGridAdapter) this.viewPageListData.get(this.currentPage).getAdapter()).notifyDataSetChanged();
            } else if (code == Mainboard.EIdriverEnum.UP.getCode()) {
                if (this.selectIndex != 0) {
                    this.selectIndex -= 4;
                    if (this.selectIndex <= 0) {
                        this.selectIndex = 0;
                    }
                    int cur3 = this.selectIndex / 8;
                    if (cur3 != this.currentPage) {
                        this.viewPager.setCurrentItem(cur3, true);
                    }
                    ((AppsGridAdapter) this.viewPageListData.get(this.currentPage).getAdapter()).notifyDataSetChanged();
                }
            } else if (code == Mainboard.EIdriverEnum.DOWN.getCode()) {
                if (this.selectIndex != this.mNeedShowApps.size() - 1) {
                    this.selectIndex += 4;
                    if (this.selectIndex >= this.mNeedShowApps.size()) {
                        this.selectIndex = this.mNeedShowApps.size() - 1;
                    }
                    int cur4 = this.selectIndex / 8;
                    if (cur4 != this.currentPage) {
                        this.viewPager.setCurrentItem(cur4, true);
                    }
                    ((AppsGridAdapter) this.viewPageListData.get(this.currentPage).getAdapter()).notifyDataSetChanged();
                }
            } else if (code == Mainboard.EIdriverEnum.PRESS.getCode()) {
                startApp();
            }
        }
    }

    private void addAppReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.PACKAGE_ADDED");
        filter.addAction("android.intent.action.PACKAGE_REPLACED");
        filter.addAction("android.intent.action.PACKAGE_REMOVED");
        filter.addDataScheme("package");
        this.mContext.registerReceiver(this.appBroadcastReceiver, filter);
    }

    private void unAppReceiver() {
        this.mContext.unregisterReceiver(this.appBroadcastReceiver);
    }
}
