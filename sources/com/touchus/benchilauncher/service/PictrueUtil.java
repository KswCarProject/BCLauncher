package com.touchus.benchilauncher.service;

import a_vcard.android.provider.BaseColumns;
import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;
import com.touchus.publicutils.bean.MediaBean;
import java.util.ArrayList;
import java.util.List;

public class PictrueUtil {
    private static Context context;
    private static Cursor cursor;
    private static List<MediaBean> picList;

    public PictrueUtil(Context context2) {
        context = context2;
    }

    public List<MediaBean> getPicList() {
        picList = new ArrayList();
        getFilesBySystem();
        return picList;
    }

    private void getFilesBySystem() {
        if (MediaStore.Images.Media.EXTERNAL_CONTENT_URI != null && Environment.getExternalStorageState().equals("mounted")) {
            cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, (String[]) null, (String) null, (String[]) null, (String) null);
            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID));
                String name = cursor.getString(cursor.getColumnIndex("_display_name"));
                String string = cursor.getString(cursor.getColumnIndex("description"));
                String url = cursor.getString(cursor.getColumnIndex("_data"));
                MediaBean picBean = new MediaBean();
                picBean.setId(id);
                picBean.setTitle(name);
                picBean.setData(url);
                picList.add(picBean);
            }
            cursor.close();
        }
    }
}
