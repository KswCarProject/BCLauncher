package com.touchus.benchilauncher.bean;

public class BeanWifi {
    public static final int CONNECT = 1;
    public static final int CONNECTING = 2;
    public static final int UNCONNECT = 3;
    private String capabilities;
    private int level;
    private String name;
    private boolean pwd;
    private boolean saveInlocal;
    private int state;

    public boolean isSaveInlocal() {
        return this.saveInlocal;
    }

    public void setSaveInlocal(boolean saveInlocal2) {
        this.saveInlocal = saveInlocal2;
    }

    public String getCapabilities() {
        return this.capabilities;
    }

    public void setCapabilities(String capabilities2) {
        this.capabilities = capabilities2;
    }

    public boolean isPwd() {
        return this.pwd;
    }

    public void setPwd(boolean pwd2) {
        this.pwd = pwd2;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int state2) {
        this.state = state2;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level2) {
        this.level = level2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }
}
