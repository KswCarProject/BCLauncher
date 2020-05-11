package com.touchus.benchilauncher.bean;

public class Equip {
    private String address;
    private int index;
    private String name;

    public Equip() {
    }

    public Equip(int index2, String name2, String address2) {
        this.index = index2;
        this.name = name2;
        this.address = address2;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index2) {
        this.index = index2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address2) {
        this.address = address2;
    }
}
