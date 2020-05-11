package com.touchus.publicutils.media;

import a_vcard.android.provider.BaseColumns;
import a_vcard.android.provider.Contacts;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import com.touchus.publicutils.bean.MediaBean;
import com.touchus.publicutils.utils.VideoType;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VideoService {
    private static String TAG = "VideoService";
    private static Context context;
    private static Cursor cursor;
    private static List<MediaBean> mediaBeans;

    public VideoService() {
    }

    public VideoService(Context context2) {
        context = context2;
    }

    public List<MediaBean> getVideoList(int pageNow, int pageSize) {
        mediaBeans = new ArrayList();
        int i = (pageNow - 1) * pageSize;
        getFilesBySystem();
        return mediaBeans;
    }

    public List<MediaBean> getVideoList() {
        mediaBeans = new ArrayList();
        getFilesBySystem();
        return mediaBeans;
    }

    private void getFilesBySystem() {
        cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, new String[]{BaseColumns._ID, "_display_name", Contacts.OrganizationColumns.TITLE, "_size", "_data", "duration", "mime_type"}, (String) null, (String[]) null, (String) null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                MediaBean mediaBean = new MediaBean();
                mediaBean.setId(cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID)));
                mediaBean.setDisplay_name(cursor.getString(cursor.getColumnIndexOrThrow("_display_name")));
                mediaBean.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(Contacts.OrganizationColumns.TITLE)));
                mediaBean.setMime_type(cursor.getString(cursor.getColumnIndexOrThrow("mime_type")));
                mediaBean.setSize(cursor.getLong(cursor.getColumnIndexOrThrow("_size")));
                mediaBean.setData(cursor.getString(cursor.getColumnIndexOrThrow("_data")));
                mediaBeans.add(mediaBean);
            }
            cursor.close();
        }
    }

    public int getCount() {
        return context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, (String[]) null, (String) null, (String[]) null, (String) null).getCount();
    }

    public static MediaBean getVideo(int id) {
        return mediaBeans.get(id);
    }

    private void GetFiles(String path, boolean isIterative) {
        File[] files = new File(path).listFiles();
        for (File f : files) {
            if (f.isFile()) {
                if (VideoType.isVideo(f.getPath().substring(f.getPath().lastIndexOf(".")))) {
                    mediaBeans.add(new MediaBean());
                }
                if (!isIterative) {
                    return;
                }
            } else if (f.isDirectory() && f.getPath().indexOf("/.") == -1) {
                GetFiles(f.getPath(), isIterative);
            }
        }
    }
}
