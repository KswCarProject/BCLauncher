package com.touchus.benchilauncher.bean;

import android.graphics.drawable.Drawable;

public class AppInfo {
    private Drawable appIcon = null;
    private String appName = "";
    private boolean iCheck = false;
    private String packageName = "";
    private int versionCode = 0;
    private String versionName = "";

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String appName2) {
        this.appName = appName2;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName2) {
        this.packageName = packageName2;
    }

    public String getVersionName() {
        return this.versionName;
    }

    public void setVersionName(String versionName2) {
        this.versionName = versionName2;
    }

    public int getVersionCode() {
        return this.versionCode;
    }

    public void setVersionCode(int versionCode2) {
        this.versionCode = versionCode2;
    }

    public Drawable getAppIcon() {
        return this.appIcon;
    }

    public void setAppIcon(Drawable appIcon2) {
        this.appIcon = appIcon2;
    }

    public boolean isiCheck() {
        return this.iCheck;
    }

    public void setiCheck(boolean iCheck2) {
        this.iCheck = iCheck2;
    }
}
