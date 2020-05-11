package com.touchus.benchilauncher.bean;

public class MenuInfo {
    private int bigIconRes;
    private int nameRes;
    private int smallIconRes;

    public MenuInfo() {
    }

    public int getNameRes() {
        return this.nameRes;
    }

    public void setNameRes(int nameRes2) {
        this.nameRes = nameRes2;
    }

    public int getBigIconRes() {
        return this.bigIconRes;
    }

    public void setBigIconRes(int bigIconRes2) {
        this.bigIconRes = bigIconRes2;
    }

    public int getSmallIconRes() {
        return this.smallIconRes;
    }

    public void setSmallIconRes(int smallIconRes2) {
        this.smallIconRes = smallIconRes2;
    }

    public MenuInfo(int nameRes2, int bigIconRes2, int smallIconRes2) {
        this.nameRes = nameRes2;
        this.bigIconRes = bigIconRes2;
        this.smallIconRes = smallIconRes2;
    }
}
