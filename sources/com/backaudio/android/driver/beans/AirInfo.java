package com.backaudio.android.driver.beans;

import android.os.Parcel;
import android.os.Parcelable;

public class AirInfo implements Parcelable {
    public static final Parcelable.Creator<AirInfo> CREATOR = new Parcelable.Creator<AirInfo>() {
        public AirInfo createFromParcel(Parcel in) {
            return new AirInfo(in);
        }

        public AirInfo[] newArray(int size) {
            return new AirInfo[size];
        }
    };
    private boolean iACOpen = false;
    private boolean iAirOpen = false;
    private boolean iAuto1 = false;
    private boolean iAuto2 = false;
    private boolean iBackWind = false;
    private boolean iDownWind = false;
    private boolean iDual = false;
    private boolean iFlatWind = false;
    private boolean iFront = false;
    private boolean iFrontWind = false;
    private boolean iHadChange = false;
    private boolean iInnerLoop = false;
    private boolean iMaxFrontWind = false;
    private boolean iRear = false;
    private boolean iSync = false;
    private boolean iWindDirAuto = false;
    private double leftTemp = 15.0d;
    private double level = 0.0d;
    private double rightTemp = 15.0d;

    public AirInfo() {
    }

    protected AirInfo(Parcel in) {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        boolean z6;
        boolean z7;
        boolean z8;
        boolean z9;
        boolean z10;
        boolean z11;
        boolean z12;
        boolean z13;
        boolean z14;
        boolean z15 = true;
        this.iAirOpen = in.readByte() != 0;
        if (in.readByte() != 0) {
            z = true;
        } else {
            z = false;
        }
        this.iACOpen = z;
        if (in.readByte() != 0) {
            z2 = true;
        } else {
            z2 = false;
        }
        this.iInnerLoop = z2;
        if (in.readByte() != 0) {
            z3 = true;
        } else {
            z3 = false;
        }
        this.iSync = z3;
        if (in.readByte() != 0) {
            z4 = true;
        } else {
            z4 = false;
        }
        this.iAuto1 = z4;
        if (in.readByte() != 0) {
            z5 = true;
        } else {
            z5 = false;
        }
        this.iAuto2 = z5;
        if (in.readByte() != 0) {
            z6 = true;
        } else {
            z6 = false;
        }
        this.iDual = z6;
        if (in.readByte() != 0) {
            z7 = true;
        } else {
            z7 = false;
        }
        this.iFront = z7;
        if (in.readByte() != 0) {
            z8 = true;
        } else {
            z8 = false;
        }
        this.iRear = z8;
        if (in.readByte() != 0) {
            z9 = true;
        } else {
            z9 = false;
        }
        this.iWindDirAuto = z9;
        if (in.readByte() != 0) {
            z10 = true;
        } else {
            z10 = false;
        }
        this.iFrontWind = z10;
        if (in.readByte() != 0) {
            z11 = true;
        } else {
            z11 = false;
        }
        this.iMaxFrontWind = z11;
        if (in.readByte() != 0) {
            z12 = true;
        } else {
            z12 = false;
        }
        this.iBackWind = z12;
        if (in.readByte() != 0) {
            z13 = true;
        } else {
            z13 = false;
        }
        this.iFlatWind = z13;
        if (in.readByte() != 0) {
            z14 = true;
        } else {
            z14 = false;
        }
        this.iDownWind = z14;
        this.iHadChange = in.readByte() == 0 ? false : z15;
        this.level = (double) in.readInt();
        this.leftTemp = (double) in.readInt();
        this.rightTemp = (double) in.readInt();
    }

    public void writeToParcel(Parcel dest, int flags) {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        int i13;
        int i14;
        int i15 = 1;
        dest.writeByte((byte) (this.iAirOpen ? 1 : 0));
        if (this.iACOpen) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeByte((byte) i);
        if (this.iInnerLoop) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        dest.writeByte((byte) i2);
        if (this.iSync) {
            i3 = 1;
        } else {
            i3 = 0;
        }
        dest.writeByte((byte) i3);
        if (this.iAuto1) {
            i4 = 1;
        } else {
            i4 = 0;
        }
        dest.writeByte((byte) i4);
        if (this.iAuto2) {
            i5 = 1;
        } else {
            i5 = 0;
        }
        dest.writeByte((byte) i5);
        if (this.iDual) {
            i6 = 1;
        } else {
            i6 = 0;
        }
        dest.writeByte((byte) i6);
        if (this.iFront) {
            i7 = 1;
        } else {
            i7 = 0;
        }
        dest.writeByte((byte) i7);
        if (this.iRear) {
            i8 = 1;
        } else {
            i8 = 0;
        }
        dest.writeByte((byte) i8);
        if (this.iWindDirAuto) {
            i9 = 1;
        } else {
            i9 = 0;
        }
        dest.writeByte((byte) i9);
        if (this.iFrontWind) {
            i10 = 1;
        } else {
            i10 = 0;
        }
        dest.writeByte((byte) i10);
        if (this.iMaxFrontWind) {
            i11 = 1;
        } else {
            i11 = 0;
        }
        dest.writeByte((byte) i11);
        if (this.iBackWind) {
            i12 = 1;
        } else {
            i12 = 0;
        }
        dest.writeByte((byte) i12);
        if (this.iFlatWind) {
            i13 = 1;
        } else {
            i13 = 0;
        }
        dest.writeByte((byte) i13);
        if (this.iDownWind) {
            i14 = 1;
        } else {
            i14 = 0;
        }
        dest.writeByte((byte) i14);
        if (!this.iHadChange) {
            i15 = 0;
        }
        dest.writeByte((byte) i15);
        dest.writeDouble(this.level);
        dest.writeDouble(this.leftTemp);
        dest.writeDouble(this.rightTemp);
    }

    public int describeContents() {
        return 0;
    }

    public boolean isiAirOpen() {
        return this.iAirOpen;
    }

    public void setiAirOpen(boolean iAirOpen2) {
        this.iAirOpen = iAirOpen2;
    }

    public boolean isiACOpen() {
        return this.iACOpen;
    }

    public void setiACOpen(boolean iACOpen2) {
        this.iACOpen = iACOpen2;
    }

    public boolean isiInnerLoop() {
        return this.iInnerLoop;
    }

    public void setiInnerLoop(boolean iInnerLoop2) {
        this.iInnerLoop = iInnerLoop2;
    }

    public void setiSync(boolean iSync2) {
        this.iSync = iSync2;
    }

    public boolean isiSync() {
        return this.iSync;
    }

    public boolean isiFrontWind() {
        return this.iFrontWind;
    }

    public void setiFrontWind(boolean iFrontWind2) {
        this.iFrontWind = iFrontWind2;
    }

    public boolean isiWindDirAuto() {
        return this.iWindDirAuto;
    }

    public void setiWindDirAuto(boolean iWindDirAuto2) {
        this.iWindDirAuto = iWindDirAuto2;
    }

    public boolean isiMaxFrontWind() {
        return this.iMaxFrontWind;
    }

    public void setiMaxFrontWind(boolean iMaxFrontWind2) {
        this.iMaxFrontWind = iMaxFrontWind2;
    }

    public boolean isiBackWind() {
        return this.iBackWind;
    }

    public void setiBackWind(boolean iBackWind2) {
        this.iBackWind = iBackWind2;
    }

    public boolean isiFlatWind() {
        return this.iFlatWind;
    }

    public void setiFlatWind(boolean iFlatWind2) {
        this.iFlatWind = iFlatWind2;
    }

    public boolean isiDownWind() {
        return this.iDownWind;
    }

    public void setiDownWind(boolean iDownWind2) {
        this.iDownWind = iDownWind2;
    }

    public boolean isiHadChange() {
        return this.iHadChange;
    }

    public void setiHadChange(boolean iHadChange2) {
        this.iHadChange = iHadChange2;
    }

    public double getLevel() {
        return this.level;
    }

    public void setLevel(double level2) {
        this.level = level2;
    }

    public double getLeftTemp() {
        return this.leftTemp;
    }

    public void setLeftTemp(double leftTemp2) {
        this.leftTemp = leftTemp2;
    }

    public double getRightTemp() {
        return this.rightTemp;
    }

    public void setRightTemp(double rightTemp2) {
        this.rightTemp = rightTemp2;
    }

    public boolean isiAuto1() {
        return this.iAuto1;
    }

    public void setiAuto1(boolean iAuto12) {
        this.iAuto1 = iAuto12;
    }

    public boolean isiAuto2() {
        return this.iAuto2;
    }

    public void setiAuto2(boolean iAuto22) {
        this.iAuto2 = iAuto22;
    }

    public boolean isiDual() {
        return this.iDual;
    }

    public void setiDual(boolean iDual2) {
        this.iDual = iDual2;
    }

    public boolean isiFront() {
        return this.iFront;
    }

    public void setiFront(boolean iFront2) {
        this.iFront = iFront2;
    }

    public boolean isiRear() {
        return this.iRear;
    }

    public void setiRear(boolean iRear2) {
        this.iRear = iRear2;
    }

    public String toString() {
        return "AirInfo [iAirOpen=" + this.iAirOpen + ", iACOpen=" + this.iACOpen + ", iInnerLoop=" + this.iInnerLoop + ", iSync=" + this.iSync + ", iAuto1=" + this.iAuto1 + ", iAuto2=" + this.iAuto2 + ", iDual=" + this.iDual + ", iFront=" + this.iFront + ", iRear=" + this.iRear + ", iWindDirAuto=" + this.iWindDirAuto + ", iFrontWind=" + this.iFrontWind + ", iMaxFrontWind=" + this.iMaxFrontWind + ", iBackWind=" + this.iBackWind + ", iFlatWind=" + this.iFlatWind + ", iDownWind=" + this.iDownWind + ", iHadChange=" + this.iHadChange + ", level=" + this.level + ", leftTemp=" + this.leftTemp + ", rightTemp=" + this.rightTemp + "]";
    }
}
