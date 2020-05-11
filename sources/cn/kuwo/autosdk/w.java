package cn.kuwo.autosdk;

import android.os.Parcel;
import android.os.Parcelable;

class w implements Parcelable.Creator {
    w() {
    }

    /* renamed from: a */
    public v createFromParcel(Parcel parcel) {
        return new v(parcel, (v) null);
    }

    /* renamed from: a */
    public v[] newArray(int i) {
        return new v[i];
    }
}
