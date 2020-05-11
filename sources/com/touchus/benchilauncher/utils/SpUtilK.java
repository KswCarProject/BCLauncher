package com.touchus.benchilauncher.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.touchus.benchilauncher.LauncherApplication;

public class SpUtilK {
    private SharedPreferences.Editor mEditor = this.mSp.edit();
    private SharedPreferences mSp;

    public SpUtilK(Context context) {
        this.mSp = context.getSharedPreferences("launcher", 0);
    }

    public SpUtilK(Context context, String spName) {
        this.mSp = context.getSharedPreferences(spName, 0);
    }

    public SpUtilK(String spName) {
        this.mSp = LauncherApplication.getContext().getSharedPreferences(spName, 0);
    }

    public String getString(String key, String defValue) {
        return this.mSp.getString(key, defValue);
    }

    public int getInt(String key, int defValue) {
        return this.mSp.getInt(key, defValue);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return this.mSp.getBoolean(key, defValue);
    }

    public void putString(String key, String value) {
        this.mEditor.putString(key, value);
        this.mEditor.commit();
    }

    public void putInt(String key, int value) {
        this.mEditor.putInt(key, value);
        this.mEditor.commit();
    }

    public void putBoolean(String key, boolean value) {
        this.mEditor.putBoolean(key, value);
        this.mEditor.commit();
    }
}
