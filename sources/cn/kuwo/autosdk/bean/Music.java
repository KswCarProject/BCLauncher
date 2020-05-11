package cn.kuwo.autosdk.bean;

import a_vcard.android.provider.Contacts;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.text.TextUtils;
import cn.kuwo.autosdk.bean.DownloadQuality;
import cn.kuwo.autosdk.q;
import cn.kuwo.autosdk.x;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class Music implements Serializable, Cloneable {
    private static final long serialVersionUID = 3985672550071260552L;
    public String album = "";
    public String artist = "";
    public long artistId;
    public boolean checked;
    public q createDate = new q();
    public DownloadQuality.Quality downQuality = DownloadQuality.Quality.Q_AUTO;
    public long downSize;
    public int duration;
    private boolean eq;
    public String fileFormat = "";
    public String filePath = "";
    public long fileSize;
    private boolean flac;
    public int hasKalaok;
    public boolean hasMv;
    public int hot;
    public LocalFileState localFileState = LocalFileState.NOT_CHECK;
    public String mvIconUrl = "";
    public String mvQuality = "";
    public String name = "";
    public boolean playFail;
    public String psrc;
    private Collection resourceCollection;
    public long rid;
    public String source = "";
    private long storageId;
    public String tag = "";
    public int trend;

    public enum LocalFileState {
        NOT_CHECK,
        EXIST,
        NOT_EXIST
    }

    public boolean Contain(Music music) {
        if (music.rid > 0 && this.rid > 0) {
            return music.rid == this.rid;
        }
        if (this.rid > 0) {
            return x.a(music.filePath, this.filePath);
        }
        if (music.rid == 0) {
            return x.a(music.filePath, this.filePath);
        }
        return false;
    }

    public boolean addResource(MusicQuality musicQuality, int i, MusicFormat musicFormat, int i2) {
        return addResource(new NetResource(musicQuality, i, musicFormat, i2));
    }

    public boolean addResource(NetResource netResource) {
        if (netResource == null) {
            return false;
        }
        if (this.resourceCollection == null) {
            this.resourceCollection = new ArrayList();
        }
        for (NetResource equals : this.resourceCollection) {
            if (equals.equals(netResource)) {
                return false;
            }
        }
        if (netResource.isEQ()) {
            this.eq = true;
        }
        if (netResource.isFLAC()) {
            this.flac = true;
        }
        return this.resourceCollection.add(netResource);
    }

    public Music clone() {
        try {
            Music music = (Music) super.clone();
            if (this.resourceCollection == null) {
                return music;
            }
            music.resourceCollection = new ArrayList();
            for (NetResource clone : this.resourceCollection) {
                music.resourceCollection.add(clone.clone());
            }
            return music;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean equalsEx(Music music) {
        return music.rid > 0 ? music.rid == this.rid : (this.filePath == null || music.filePath == null) ? this.filePath == null && music.filePath == null : this.filePath.equals(music.filePath);
    }

    public NetResource getBestResource() {
        if (this.resourceCollection == null) {
            return null;
        }
        NetResource netResource = null;
        for (NetResource netResource2 : this.resourceCollection) {
            if (netResource == null) {
                netResource = netResource2;
            } else if (netResource.bitrate < netResource2.bitrate) {
                netResource = netResource2;
            }
        }
        return netResource;
    }

    public NetResource getBestResource(MusicQuality musicQuality) {
        if (this.resourceCollection == null) {
            return null;
        }
        NetResource netResource = null;
        for (NetResource netResource2 : this.resourceCollection) {
            if (netResource2.quality.ordinal() <= musicQuality.ordinal() && (netResource == null || netResource.bitrate < netResource2.bitrate)) {
                netResource = netResource2;
            }
        }
        return netResource;
    }

    public boolean getInfoFromDatabase(Cursor cursor) {
        try {
            setStorageId(cursor.getLong(cursor.getColumnIndex("id")));
            this.rid = cursor.getLong(cursor.getColumnIndex("rid"));
            this.name = x.c(cursor.getString(cursor.getColumnIndex(Contacts.PeopleColumns.NAME)));
            this.artist = x.c(cursor.getString(cursor.getColumnIndex("artist")));
            this.artistId = cursor.getLong(cursor.getColumnIndex("artistid"));
            this.album = x.c(cursor.getString(cursor.getColumnIndex("album")));
            this.duration = cursor.getInt(cursor.getColumnIndex("duration"));
            this.hasMv = cursor.getInt(cursor.getColumnIndex("hasmv")) > 0;
            this.mvQuality = x.c(cursor.getString(cursor.getColumnIndex("mvquality")));
            this.hasKalaok = cursor.getInt(cursor.getColumnIndex("haskalaok"));
            this.downSize = (long) cursor.getInt(cursor.getColumnIndex("downsize"));
            this.downQuality = DownloadQuality.Quality.valueOf(x.c(cursor.getString(cursor.getColumnIndex("downquality"))));
            this.filePath = x.c(cursor.getString(cursor.getColumnIndex("filepath")));
            this.fileSize = cursor.getLong(cursor.getColumnIndex("filesize"));
            this.fileFormat = x.c(cursor.getString(cursor.getColumnIndex("fileformat")));
            if (cursor.getColumnIndex("resource") >= 0) {
                parseResourceStringFromDatabase(x.c(cursor.getString(cursor.getColumnIndex("resource"))));
            }
            if (cursor.getColumnIndex("createtime") >= 0) {
                String c = x.c(cursor.getString(cursor.getColumnIndex("createtime")));
                if (TextUtils.isEmpty(c)) {
                    this.createDate = new q();
                    return true;
                }
                this.createDate = new q(c);
                return true;
            }
            this.createDate = new q();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ContentValues getMusicContentValues(long j) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("rid", Long.valueOf(this.rid));
        contentValues.put("listid", Long.valueOf(j));
        contentValues.put(Contacts.PeopleColumns.NAME, x.c(this.name));
        contentValues.put("artist", x.c(this.artist));
        contentValues.put("artistid", Long.valueOf(this.artistId));
        contentValues.put("album", x.c(this.album));
        contentValues.put("duration", Integer.valueOf(this.duration));
        contentValues.put("hot", Integer.valueOf(this.hot));
        contentValues.put("source", x.c(this.source));
        contentValues.put("resource", x.c(getResourceStringForDatabase()));
        contentValues.put("hasmv", Integer.valueOf(this.hasMv ? 1 : 0));
        contentValues.put("mvquality", x.c(this.mvQuality));
        contentValues.put("haskalaok", Integer.valueOf(this.hasKalaok));
        contentValues.put("downsize", Long.valueOf(this.downSize));
        contentValues.put("downquality", this.downQuality == null ? "" : this.downQuality.toString());
        contentValues.put("filepath", x.c(this.filePath));
        contentValues.put("fileformat", x.c(this.fileFormat));
        contentValues.put("filesize", Long.valueOf(this.fileSize));
        contentValues.put("createtime", x.c(this.createDate.a()));
        return contentValues;
    }

    public NetResource getResource(MusicFormat musicFormat) {
        if (this.resourceCollection == null) {
            return null;
        }
        NetResource netResource = null;
        for (NetResource netResource2 : this.resourceCollection) {
            if (netResource2.format == musicFormat && (netResource == null || netResource.bitrate < netResource2.bitrate)) {
                netResource = netResource2;
            }
        }
        return netResource;
    }

    public NetResource getResource(MusicQuality musicQuality) {
        if (this.resourceCollection == null) {
            return null;
        }
        NetResource netResource = null;
        for (NetResource netResource2 : this.resourceCollection) {
            if (netResource2.quality == musicQuality && (netResource == null || netResource.bitrate < netResource2.bitrate)) {
                netResource = netResource2;
            }
        }
        return netResource;
    }

    public Collection getResourceCollection() {
        return this.resourceCollection;
    }

    public String getResourceStringForDatabase() {
        if (this.resourceCollection == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (NetResource netResource : this.resourceCollection) {
            sb.append(netResource.quality.getDiscribe()).append(".").append(netResource.bitrate).append(".");
            sb.append(netResource.format.getDiscribe()).append(".").append(netResource.size).append(";");
        }
        return sb.toString();
    }

    public long getStorageId() {
        return this.storageId;
    }

    public boolean hasHighMv() {
        if (!this.hasMv || this.mvQuality == null) {
            return false;
        }
        for (String equalsIgnoreCase : x.a(this.mvQuality, ';')) {
            if (equalsIgnoreCase.equalsIgnoreCase("MP4")) {
                return true;
            }
        }
        return false;
    }

    public boolean hasLowMv() {
        if (!this.hasMv || this.mvQuality == null) {
            return false;
        }
        for (String equalsIgnoreCase : x.a(this.mvQuality, ';')) {
            if (equalsIgnoreCase.equalsIgnoreCase("MP4L")) {
                return true;
            }
        }
        return false;
    }

    public int hashCodeEx() {
        if (this.rid > 0) {
            return (int) this.rid;
        }
        if (this.filePath == null) {
            return 0;
        }
        return this.filePath.hashCode();
    }

    public boolean isEQ() {
        return this.eq;
    }

    public boolean isFLAC() {
        return this.flac;
    }

    public boolean isLocalFile() {
        return this.rid <= 0;
    }

    public int parseResourceStringFromDatabase(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        int i = 0;
        for (String str2 : x.a(str, ';')) {
            if (!TextUtils.isEmpty(str2)) {
                String[] a = x.a(str2, '.');
                if (a.length == 4) {
                    try {
                        if (addResource(new NetResource(MusicQuality.getQualityFromDiscribe(a[0]), Integer.valueOf(a[1]).intValue(), MusicFormat.getFormatFromDiscribe(a[2]), Integer.valueOf(a[3]).intValue()))) {
                            i++;
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return i;
    }

    public int parseResourceStringFromQuku(String str) {
        int i;
        int i2;
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        String[] a = x.a(str, ';');
        int length = a.length;
        int i3 = 0;
        int i4 = 0;
        while (i3 < length) {
            String[] a2 = x.a(a[i3], ',');
            if (a2.length == 4) {
                String b = x.b(a2[0]);
                String b2 = x.b(a2[1]);
                String b3 = x.b(a2[2]);
                String b4 = x.b(a2[3]);
                MusicQuality qualityFromDiscribe4Quku = MusicQuality.getQualityFromDiscribe4Quku(b);
                int parseInt = x.a(b2) ? Integer.parseInt(b2) : 0;
                MusicFormat formatFromDiscribe4Quku = MusicFormat.getFormatFromDiscribe4Quku(b3);
                if (b4.toUpperCase().indexOf("KB") > 0) {
                    try {
                        i2 = (int) (Float.parseFloat(b4.replaceAll("(?i)kb", "")) * 1024.0f);
                    } catch (Exception e) {
                        e.printStackTrace();
                        i2 = 0;
                    }
                } else if (b4.toUpperCase().indexOf("MB") > 0) {
                    try {
                        i2 = (int) (Float.parseFloat(b4.replaceAll("(?i)mb", "")) * 1024.0f * 1024.0f);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        i2 = 0;
                    }
                } else {
                    if (b4.toUpperCase().indexOf("B") > 0) {
                        try {
                            i2 = (int) Float.parseFloat(b4.replaceAll("(?i)b", ""));
                        } catch (Exception e3) {
                            e3.printStackTrace();
                        }
                    }
                    i2 = 0;
                }
                if (addResource(new NetResource(qualityFromDiscribe4Quku, parseInt, formatFromDiscribe4Quku, i2))) {
                    i = i4 + 1;
                    i3++;
                    i4 = i;
                }
            }
            i = i4;
            i3++;
            i4 = i;
        }
        return i4;
    }

    public void setLocalFileExist(boolean z) {
        this.localFileState = z ? LocalFileState.EXIST : LocalFileState.NOT_EXIST;
    }

    public void setResourceCollection(Collection collection) {
        this.resourceCollection = collection;
    }

    public void setStorageId(long j) {
        if (0 <= j) {
            this.storageId = j;
        }
    }

    public String toDebugString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name:").append(this.name);
        sb.append(", Artist:").append(this.artist);
        sb.append(", Album:").append(this.album);
        sb.append(", Rid:").append(this.rid);
        sb.append(", Path:").append(this.filePath);
        return sb.toString();
    }

    public boolean vaild() {
        return this.rid > 0 || !TextUtils.isEmpty(this.filePath);
    }
}
