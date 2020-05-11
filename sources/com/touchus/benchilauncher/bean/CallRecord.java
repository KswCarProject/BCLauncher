package com.touchus.benchilauncher.bean;

import java.io.Serializable;

public class CallRecord implements Serializable {
    private static final long serialVersionUID = 1;
    private String callName;
    private int callState;
    private String callTime;
    private String callphoneNumber;

    public String getCallTime() {
        return this.callTime;
    }

    public void setCallTime(String callTime2) {
        this.callTime = callTime2;
    }

    public String getCallphoneNumber() {
        return this.callphoneNumber;
    }

    public void setCallphoneNumber(String callphoneNumber2) {
        this.callphoneNumber = callphoneNumber2;
    }

    public String getCallName() {
        return this.callName;
    }

    public void setCallName(String callName2) {
        this.callName = callName2;
    }

    public int getCallState() {
        return this.callState;
    }

    public void setCallState(int callState2) {
        this.callState = callState2;
    }
}
