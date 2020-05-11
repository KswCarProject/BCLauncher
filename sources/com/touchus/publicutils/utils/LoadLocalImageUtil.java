package com.touchus.publicutils.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Handler;
import android.os.Message;
import android.util.LruCache;
import com.touchus.publicutils.bean.MediaBean;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoadLocalImageUtil {
    public static int MUSIC_TYPE = 1;
    public static int VIDEO_TYPE = 2;
    private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);
    private static LoadLocalImageUtil instance = null;
    private LruCache<String, Bitmap> imageCache = new LruCache<String, Bitmap>(((int) Runtime.getRuntime().maxMemory()) / 4) {
        /* access modifiers changed from: protected */
        public int sizeOf(String key, Bitmap value) {
            return value.getByteCount();
        }
    };

    public interface ImageCallback {
        void imageLoaded(Bitmap bitmap, String str);
    }

    public static synchronized LoadLocalImageUtil getInstance() {
        LoadLocalImageUtil loadLocalImageUtil;
        synchronized (LoadLocalImageUtil.class) {
            if (instance == null) {
                instance = new LoadLocalImageUtil();
            }
            loadLocalImageUtil = instance;
        }
        return loadLocalImageUtil;
    }

    public void addVideoThumbToCache(String path, Bitmap bitmap) {
        if (bitmap != null && getVideoThumbToCache(path) == null) {
            this.imageCache.put(path, bitmap);
        }
    }

    public Bitmap getVideoThumbToCache(String path) {
        return this.imageCache.get(path);
    }

    public void loadDrawable(MediaBean mediaBean, int type, Context context, final String tag, final ImageCallback imageCallback) {
        final Handler handler = new Handler() {
            public void handleMessage(Message message) {
                imageCallback.imageLoaded((Bitmap) message.obj, tag);
            }
        };
        final String str = tag;
        final int i = type;
        final Context context2 = context;
        final MediaBean mediaBean2 = mediaBean;
        fixedThreadPool.execute(new Thread() {
            public void run() {
                Bitmap bitmip = LoadLocalImageUtil.this.getVideoThumbToCache(str);
                if (bitmip == null) {
                    if (i == LoadLocalImageUtil.MUSIC_TYPE) {
                        bitmip = LoadLocalImageUtil.getMusicPic(context2, mediaBean2);
                    } else {
                        bitmip = LoadLocalImageUtil.getVideoPic(mediaBean2);
                    }
                    LoadLocalImageUtil.this.addVideoThumbToCache(str, bitmip);
                }
                handler.sendMessage(handler.obtainMessage(0, bitmip));
            }
        });
    }

    /* access modifiers changed from: private */
    public static Bitmap getVideoPic(MediaBean mediaBean) {
        new ThumbnailUtils();
        return ThumbnailUtils.createVideoThumbnail(mediaBean.getData(), 3);
    }

    /* access modifiers changed from: private */
    public static Bitmap getMusicPic(Context context, MediaBean mediaBean) {
        return createAlbumArt(mediaBean.getData());
    }

    private static Bitmap createAlbumArt(String filePath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = decodeSampledBitmapFromResource(retriever.getEmbeddedPicture(), 150, 150);
            try {
                retriever.release();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                retriever.release();
            } catch (Exception e22) {
                e22.printStackTrace();
            }
        } catch (Throwable th) {
            try {
                retriever.release();
            } catch (Exception e23) {
                e23.printStackTrace();
            }
            throw th;
        }
        return bitmap;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            int halfHeight = height / 2;
            int halfWidth = width / 2;
            while (halfHeight / inSampleSize > reqHeight && halfWidth / inSampleSize > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    private static Bitmap decodeSampledBitmapFromResource(byte[] data, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }
}
