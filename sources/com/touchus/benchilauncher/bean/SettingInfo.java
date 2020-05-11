package com.touchus.benchilauncher.bean;

public class SettingInfo {
    private boolean checked;
    private boolean isCheckItem;
    private String item;

    public String getItem() {
        return this.item;
    }

    public void setItem(String item2) {
        this.item = item2;
    }

    public boolean isCheckItem() {
        return this.isCheckItem;
    }

    public void setCheckItem(boolean isCheckItem2) {
        this.isCheckItem = isCheckItem2;
    }

    public SettingInfo(String item2, boolean isCheckItem2) {
        this.item = item2;
        this.isCheckItem = isCheckItem2;
    }

    public SettingInfo(String item2, boolean isCheckItem2, boolean checked2) {
        this.item = item2;
        this.isCheckItem = isCheckItem2;
        this.checked = checked2;
    }

    public boolean isChecked() {
        return this.checked;
    }

    public void setChecked(boolean checked2) {
        this.checked = checked2;
    }
}
