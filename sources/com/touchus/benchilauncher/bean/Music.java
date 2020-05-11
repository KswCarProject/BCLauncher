package com.touchus.benchilauncher.bean;

public class Music {
    private String data;
    private String display_name;
    private int id;
    private String mime_type;
    private int size;
    private String title;

    public int getId() {
        return this.id;
    }

    public void setId(int id2) {
        this.id = id2;
    }

    public String getDisplay_name() {
        return this.display_name;
    }

    public void setDisplay_name(String display_name2) {
        this.display_name = display_name2;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int size2) {
        this.size = size2;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data2) {
        this.data = data2;
    }

    public String getMime_type() {
        return this.mime_type;
    }

    public void setMime_type(String mime_type2) {
        this.mime_type = mime_type2;
    }
}
