package com.touchus.benchilauncher.bean;

public class SmsInfo {
    public static int TYPE_AD = 2;
    public static int TYPE_ALL = 4;
    public static int TYPE_LOCATE = 1;
    public static int TYPE_SMS = 3;
    private String data1;
    private String data2;
    private boolean hadRead = true;
    private String id;
    private boolean inEdit = false;
    private String name;
    private String sender;
    private long time;
    private int type;

    public String getId() {
        return this.id;
    }

    public void setId(String id2) {
        this.id = id2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getSender() {
        return this.sender;
    }

    public void setSender(String sender2) {
        this.sender = sender2;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time2) {
        this.time = time2;
    }

    public boolean isHadRead() {
        return this.hadRead;
    }

    public void setHadRead(boolean hadRead2) {
        this.hadRead = hadRead2;
    }

    public boolean isInEdit() {
        return this.inEdit;
    }

    public void setInEdit(boolean inEdit2) {
        this.inEdit = inEdit2;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type2) {
        this.type = type2;
    }

    public String getData1() {
        return this.data1;
    }

    public void setData1(String data12) {
        this.data1 = data12;
    }

    public String getData2() {
        return this.data2;
    }

    public void setData2(String data22) {
        this.data2 = data22;
    }
}
