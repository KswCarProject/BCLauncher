package com.backaudio.android.driver.beans;

import android.os.Parcel;
import android.os.Parcelable;

public class CarBaseInfo implements Parcelable {
    public static final Parcelable.Creator<CarBaseInfo> CREATOR = new Parcelable.Creator<CarBaseInfo>() {
        public CarBaseInfo createFromParcel(Parcel in) {
            return new CarBaseInfo(in);
        }

        public CarBaseInfo[] newArray(int size) {
            return new CarBaseInfo[size];
        }
    };
    private boolean foglight = false;
    private boolean iAccOpen = false;
    private boolean iBack = false;
    private boolean iBrake = false;
    private boolean iDistantLightOpen = false;
    private boolean iFlash = false;
    private boolean iFootBrake = false;
    private boolean iFront = false;
    private boolean iInP = false;
    private boolean iInRevering = false;
    private boolean iLeftBackOpen = false;
    private boolean iLeftFrontOpen = false;
    private int iLightValue = 0;
    private boolean iNearLightOpen = false;
    private boolean iPowerOn = false;
    private boolean iRightBackOpen = false;
    private boolean iRightFrontOpen = false;
    private boolean iSafetyBelt = false;
    public boolean ichange = false;

    public CarBaseInfo() {
    }

    protected CarBaseInfo(Parcel in) {
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
        this.iDistantLightOpen = in.readByte() != 0;
        if (in.readByte() != 0) {
            z = true;
        } else {
            z = false;
        }
        this.iPowerOn = z;
        if (in.readByte() != 0) {
            z2 = true;
        } else {
            z2 = false;
        }
        this.iAccOpen = z2;
        if (in.readByte() != 0) {
            z3 = true;
        } else {
            z3 = false;
        }
        this.iBrake = z3;
        if (in.readByte() != 0) {
            z4 = true;
        } else {
            z4 = false;
        }
        this.iNearLightOpen = z4;
        if (in.readByte() != 0) {
            z5 = true;
        } else {
            z5 = false;
        }
        this.iInP = z5;
        if (in.readByte() != 0) {
            z6 = true;
        } else {
            z6 = false;
        }
        this.iInRevering = z6;
        if (in.readByte() != 0) {
            z7 = true;
        } else {
            z7 = false;
        }
        this.iRightBackOpen = z7;
        if (in.readByte() != 0) {
            z8 = true;
        } else {
            z8 = false;
        }
        this.iLeftBackOpen = z8;
        if (in.readByte() != 0) {
            z9 = true;
        } else {
            z9 = false;
        }
        this.iRightFrontOpen = z9;
        if (in.readByte() != 0) {
            z10 = true;
        } else {
            z10 = false;
        }
        this.iLeftFrontOpen = z10;
        if (in.readByte() != 0) {
            z11 = true;
        } else {
            z11 = false;
        }
        this.iFront = z11;
        if (in.readByte() != 0) {
            z12 = true;
        } else {
            z12 = false;
        }
        this.iBack = z12;
        if (in.readByte() != 0) {
            z13 = true;
        } else {
            z13 = false;
        }
        this.iFlash = z13;
        if (in.readByte() != 0) {
            z14 = true;
        } else {
            z14 = false;
        }
        this.iSafetyBelt = z14;
        this.iFootBrake = in.readByte() == 0 ? false : z15;
    }

    public boolean isiDistantLightOpen() {
        return this.iDistantLightOpen;
    }

    public void setiDistantLightOpen(boolean iDistantLightOpen2) {
        this.ichange |= this.iDistantLightOpen ^ iDistantLightOpen2;
        this.iDistantLightOpen = iDistantLightOpen2;
    }

    public boolean isiAccOpen() {
        return this.iAccOpen;
    }

    public void setiAccOpen(boolean iAccOpen2) {
        this.iAccOpen = iAccOpen2;
    }

    public boolean isiNearLightOpen() {
        return this.iNearLightOpen;
    }

    public void setiNearLightOpen(boolean iNearLightOpen2) {
        this.ichange = this.iNearLightOpen ^ iNearLightOpen2;
        this.iNearLightOpen = iNearLightOpen2;
    }

    public boolean isiInP() {
        return this.iInP;
    }

    public void setiInP(boolean iInP2) {
        this.iInP = iInP2;
    }

    public boolean isiInRevering() {
        return this.iInRevering;
    }

    public void setiInRevering(boolean iInRevering2) {
        this.iInRevering = iInRevering2;
    }

    public boolean isiRightBackOpen() {
        return this.iRightBackOpen;
    }

    public void setiRightBackOpen(boolean iRightBackOpen2) {
        this.iRightBackOpen = iRightBackOpen2;
    }

    public boolean isiLeftBackOpen() {
        return this.iLeftBackOpen;
    }

    public void setiLeftBackOpen(boolean iLeftBackOpen2) {
        this.iLeftBackOpen = iLeftBackOpen2;
    }

    public boolean isiRightFrontOpen() {
        return this.iRightFrontOpen;
    }

    public void setiRightFrontOpen(boolean iRightFrontOpen2) {
        this.iRightFrontOpen = iRightFrontOpen2;
    }

    public boolean isiLeftFrontOpen() {
        return this.iLeftFrontOpen;
    }

    public void setiLeftFrontOpen(boolean iLeftFrontOpen2) {
        this.iLeftFrontOpen = iLeftFrontOpen2;
    }

    public boolean isiFront() {
        return this.iFront;
    }

    public void setiFront(boolean iFront2) {
        this.iFront = iFront2;
    }

    public boolean isiBack() {
        return this.iBack;
    }

    public void setiBack(boolean iBack2) {
        this.iBack = iBack2;
    }

    public int describeContents() {
        return 0;
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
        dest.writeByte((byte) (this.iDistantLightOpen ? 1 : 0));
        if (this.iPowerOn) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeByte((byte) i);
        if (this.iAccOpen) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        dest.writeByte((byte) i2);
        if (this.iBrake) {
            i3 = 1;
        } else {
            i3 = 0;
        }
        dest.writeByte((byte) i3);
        if (this.iNearLightOpen) {
            i4 = 1;
        } else {
            i4 = 0;
        }
        dest.writeByte((byte) i4);
        if (this.iInP) {
            i5 = 1;
        } else {
            i5 = 0;
        }
        dest.writeByte((byte) i5);
        if (this.iInRevering) {
            i6 = 1;
        } else {
            i6 = 0;
        }
        dest.writeByte((byte) i6);
        if (this.iRightBackOpen) {
            i7 = 1;
        } else {
            i7 = 0;
        }
        dest.writeByte((byte) i7);
        if (this.iLeftBackOpen) {
            i8 = 1;
        } else {
            i8 = 0;
        }
        dest.writeByte((byte) i8);
        if (this.iRightFrontOpen) {
            i9 = 1;
        } else {
            i9 = 0;
        }
        dest.writeByte((byte) i9);
        if (this.iLeftFrontOpen) {
            i10 = 1;
        } else {
            i10 = 0;
        }
        dest.writeByte((byte) i10);
        if (this.iFront) {
            i11 = 1;
        } else {
            i11 = 0;
        }
        dest.writeByte((byte) i11);
        if (this.iBack) {
            i12 = 1;
        } else {
            i12 = 0;
        }
        dest.writeByte((byte) i12);
        if (this.iFlash) {
            i13 = 1;
        } else {
            i13 = 0;
        }
        dest.writeByte((byte) i13);
        if (this.iSafetyBelt) {
            i14 = 1;
        } else {
            i14 = 0;
        }
        dest.writeByte((byte) i14);
        if (!this.iFootBrake) {
            i15 = 0;
        }
        dest.writeByte((byte) i15);
    }

    public String toString() {
        return "CarBaseInfo [iDistantLightOpen=" + this.iDistantLightOpen + ", iPowerOn=" + this.iPowerOn + ", iAccOpen=" + this.iAccOpen + ", iBrake=" + this.iBrake + ", iNearLightOpen=" + this.iNearLightOpen + ", iInP=" + this.iInP + ", iInRevering=" + this.iInRevering + ", iRightBackOpen=" + this.iRightBackOpen + ", iLeftBackOpen=" + this.iLeftBackOpen + ", iRightFrontOpen=" + this.iRightFrontOpen + ", iLeftFrontOpen=" + this.iLeftFrontOpen + ", iFront=" + this.iFront + ", iBack=" + this.iBack + ", iFlash=" + this.iFlash + ", iSafetyBelt=" + this.iSafetyBelt + ", iFootBrake=" + this.iFootBrake + "]";
    }

    public boolean isiBrake() {
        return this.iBrake;
    }

    public void setiBrake(boolean iBrake2) {
        this.ichange |= this.iBrake ^ iBrake2;
        this.iBrake = iBrake2;
    }

    public int getiLightValue() {
        return this.iLightValue;
    }

    public void setiLightValue(int iLightValue2) {
        this.iLightValue = iLightValue2;
    }

    public boolean isiPowerOn() {
        return this.iPowerOn;
    }

    public void setiPowerOn(boolean iPowerOn2) {
        this.iPowerOn = iPowerOn2;
    }

    public boolean isFoglight() {
        return this.foglight;
    }

    public void setFoglight(boolean foglight2) {
        this.ichange |= this.foglight ^ foglight2;
        this.foglight = foglight2;
    }

    public boolean isiFlash() {
        return this.iFlash;
    }

    public void setiFlash(boolean iFlash2) {
        this.iFlash = iFlash2;
    }

    public boolean isiSafetyBelt() {
        return this.iSafetyBelt;
    }

    public void setiSafetyBelt(boolean iSafetyBelt2) {
        this.iSafetyBelt = iSafetyBelt2;
    }

    public boolean isiFootBrake() {
        return this.iFootBrake;
    }

    public void setiFootBrake(boolean iFootBrake2) {
        this.ichange |= this.iFootBrake ^ iFootBrake2;
        this.iFootBrake = iFootBrake2;
    }
}
