package cn.kuwo.autosdk;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Process;

public class v implements Parcelable {
    public static final Parcelable.Creator f = new w();
    public final String a;
    public final int b;
    public final int c;
    public final int d;
    public final String e;

    private v(Parcel parcel) {
        this.a = parcel.readString();
        this.b = parcel.readInt();
        this.c = parcel.readInt();
        this.d = parcel.readInt();
        this.e = parcel.readString();
    }

    /* synthetic */ v(Parcel parcel, v vVar) {
        this(parcel);
    }

    private v(String str) {
        String[] split = str.split("\\s+");
        this.a = split[0];
        this.b = Process.getUidForName(this.a);
        this.c = Integer.parseInt(split[1]);
        this.d = Integer.parseInt(split[2]);
        if (split.length == 16) {
            this.e = split[13];
        } else {
            this.e = split[14];
        }
    }

    /* synthetic */ v(String str, v vVar) {
        this(str);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.a);
        parcel.writeInt(this.b);
        parcel.writeInt(this.c);
        parcel.writeInt(this.d);
        parcel.writeString(this.e);
    }
}
