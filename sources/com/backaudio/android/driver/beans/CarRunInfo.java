package com.backaudio.android.driver.beans;

import android.os.Parcel;
import android.os.Parcelable;

public class CarRunInfo implements Parcelable {
    public static final Parcelable.Creator<CarRunInfo> CREATOR = new Parcelable.Creator<CarRunInfo>() {
        public CarRunInfo createFromParcel(Parcel in) {
            return new CarRunInfo(in);
        }

        public CarRunInfo[] newArray(int size) {
            return new CarRunInfo[size];
        }
    };
    private int averSpeed = 0;
    private int curSpeed = 0;
    private int mileage = 0;
    private double outsideTemp = 0.0d;
    private int revolutions = 0;
    private long totalMileage = 0;

    public CarRunInfo() {
    }

    protected CarRunInfo(Parcel in) {
        this.averSpeed = in.readInt();
        this.mileage = in.readInt();
        this.totalMileage = in.readLong();
        this.outsideTemp = in.readDouble();
        this.revolutions = in.readInt();
        this.curSpeed = in.readInt();
    }

    public int getAverSpeed() {
        return this.averSpeed;
    }

    public void setAverSpeed(int averSpeed2) {
        this.averSpeed = averSpeed2;
    }

    public int getMileage() {
        return this.mileage;
    }

    public void setMileage(int mileage2) {
        this.mileage = mileage2;
    }

    public long getTotalMileage() {
        return this.totalMileage;
    }

    public void setTotalMileage(long totalMileage2) {
        this.totalMileage = totalMileage2;
    }

    public double getOutsideTemp() {
        return this.outsideTemp;
    }

    public void setOutsideTemp(double outsideTemp2) {
        this.outsideTemp = outsideTemp2;
    }

    public int getRevolutions() {
        return this.revolutions;
    }

    public void setRevolutions(int revolutions2) {
        this.revolutions = revolutions2;
    }

    public int getCurSpeed() {
        return this.curSpeed;
    }

    public void setCurSpeed(int curSpeed2) {
        this.curSpeed = curSpeed2;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.averSpeed);
        dest.writeInt(this.mileage);
        dest.writeLong(this.totalMileage);
        dest.writeDouble(this.outsideTemp);
        dest.writeInt(this.revolutions);
        dest.writeInt(this.curSpeed);
    }

    public String toString() {
        return "CarRunInfo [averSpeed=" + this.averSpeed + ", mileage=" + this.mileage + ", totalMileage=" + this.totalMileage + ", outsideTemp=" + this.outsideTemp + ", revolutions=" + this.revolutions + ", curSpeed=" + this.curSpeed + "]";
    }
}
