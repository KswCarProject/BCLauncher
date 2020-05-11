package com.touchus.publicutils.bean;

import android.graphics.Bitmap;

public class MediaBean {
    private Bitmap bitmap;
    private String data;
    private String display_name;
    private long duration;
    private long id;
    private String mime_type;
    private long size;
    private String thumbnail_path;
    private String title;

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public void setBitmap(Bitmap bitmap2) {
        this.bitmap = bitmap2;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id2) {
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

    public long getSize() {
        return this.size;
    }

    public void setSize(long size2) {
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

    public long getDuration() {
        return this.duration;
    }

    public void setDuration(long duration2) {
        this.duration = duration2;
    }

    public String getThumbnail_path() {
        return this.thumbnail_path;
    }

    public void setThumbnail_path(String thumbnail_path2) {
        this.thumbnail_path = thumbnail_path2;
    }

    public String toString() {
        return "MediaBean [id=" + this.id + ", display_name=" + this.display_name + ", title=" + this.title + ", size=" + this.size + ", duration=" + this.duration + ", data=" + this.data + ", mime_type=" + this.mime_type + ", bitmap=" + this.bitmap + ", thumbnail_path=" + this.thumbnail_path + "]";
    }
}
