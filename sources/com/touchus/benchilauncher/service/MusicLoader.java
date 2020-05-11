package com.touchus.benchilauncher.service;

import a_vcard.android.provider.BaseColumns;
import a_vcard.android.provider.Contacts;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import com.touchus.publicutils.bean.MediaBean;
import java.util.ArrayList;

public class MusicLoader {
    private static final String TAG = "com.cwf.app.musicplay";
    private static ContentResolver contentResolver;
    private static ArrayList<MediaBean> musicList;
    private static MusicLoader musicLoader;
    private Uri contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    private String[] projection = {BaseColumns._ID, "_display_name", Contacts.OrganizationColumns.TITLE, "_data", "album", "artist", "duration", "_size"};
    private String sortOrder = "_size";

    public static MusicLoader instance(ContentResolver pContentResolver) {
        if (musicLoader == null) {
            contentResolver = pContentResolver;
            musicLoader = new MusicLoader();
        }
        return musicLoader;
    }

    private MusicLoader() {
    }

    private void queryMusicList() {
        Cursor cursor = contentResolver.query(this.contentUri, this.projection, (String) null, (String[]) null, (String) null);
        ArrayList arrayList = new ArrayList();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int displayNameCol = cursor.getColumnIndex("_display_name");
                int titleCol = cursor.getColumnIndex(Contacts.OrganizationColumns.TITLE);
                int idCol = cursor.getColumnIndex(BaseColumns._ID);
                int durationCol = cursor.getColumnIndex("duration");
                int sizeCol = cursor.getColumnIndex("_size");
                int dataCol = cursor.getColumnIndex("_data");
                do {
                    String title = cursor.getString(titleCol);
                    String displayName = cursor.getString(displayNameCol);
                    long id = cursor.getLong(idCol);
                    int duration = cursor.getInt(durationCol);
                    long size = cursor.getLong(sizeCol);
                    String data = cursor.getString(dataCol);
                    MediaBean musicInfo = new MediaBean();
                    musicInfo.setId(id);
                    musicInfo.setTitle(title);
                    musicInfo.setDisplay_name(displayName);
                    musicInfo.setDuration((long) duration);
                    musicInfo.setSize(size);
                    musicInfo.setData(data);
                    if (!arrayList.contains(data) && duration != 0) {
                        arrayList.add(data);
                        musicList.add(musicInfo);
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
    }

    public ArrayList<MediaBean> getMusicList() {
        musicList = new ArrayList<>();
        queryMusicList();
        return musicList;
    }

    public Uri getMusicUriById(long id) {
        return ContentUris.withAppendedId(this.contentUri, id);
    }
}
