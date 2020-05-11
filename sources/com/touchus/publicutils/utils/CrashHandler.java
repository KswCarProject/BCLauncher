package com.touchus.publicutils.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.util.Log;
import com.touchus.publicutils.sysconst.BenzModel;
import com.touchus.publicutils.sysconst.PubSysConst;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.spi.Configurator;

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static CrashHandler INSTANCE = null;
    public static final String TAG = "CrashHandler";
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    private Map<String, String> infos = new HashMap();
    private String mAddress;
    private Context mContext;
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    public static CrashHandler getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new CrashHandler(context);
        }
        return INSTANCE;
    }

    public CrashHandler(Context context) {
        this.mContext = context;
        this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public void setAddress(String address) {
        this.mAddress = address;
    }

    public void uncaughtException(Thread thread, Throwable ex) {
        this.mContext.sendBroadcast(new Intent("SHOW_NAVIGATION_BAR"));
        boolean iUserDeal = handleException(ex);
        if (PubSysConst.DEBUG) {
            this.mDefaultHandler.uncaughtException(thread, ex);
        } else if (iUserDeal || this.mDefaultHandler == null) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Log.e(TAG, "error : ", e);
            }
            Process.killProcess(Process.myPid());
            System.exit(1);
        } else {
            this.mDefaultHandler.uncaughtException(thread, ex);
        }
    }

    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        collectDeviceInfo(this.mContext);
        String filePath = saveCrashInfo2File(getCrashInfo(ex));
        if ("c200".equalsIgnoreCase(Build.MODEL) || "e200_13".equalsIgnoreCase(Build.MODEL) || "gla".equalsIgnoreCase(Build.MODEL)) {
            MailManager.getInstance(this.mContext).sendMailWithFile(String.valueOf(BenzModel.benzName()) + "奔溃日志", TimeUtils.getSimpleDate(), filePath);
        } else {
            MailManager.getInstance(this.mContext).sendMailWithFile(String.valueOf(Build.MODEL) + "奔溃日志", TimeUtils.getSimpleDate(), filePath);
        }
        return true;
    }

    public void collectDeviceInfo(Context ctx) {
        try {
            PackageInfo pi = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 1);
            if (pi != null) {
                String versionName = pi.versionName == null ? Configurator.NULL : pi.versionName;
                String versionCode = new StringBuilder(String.valueOf(pi.versionCode)).toString();
                this.infos.put("versionName", versionName);
                this.infos.put("versionCode", versionCode);
                this.infos.put("IMEI", UtilTools.getIMEI(this.mContext));
                this.infos.put("address", this.mAddress);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info", e);
        }
        for (Field field : Build.class.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                this.infos.put(field.getName(), field.get((Object) null).toString());
                Log.d(TAG, String.valueOf(field.getName()) + " : " + field.get((Object) null));
            } catch (Exception e2) {
                Log.e(TAG, "an error occured when collect crash info", e2);
            }
        }
    }

    private String saveCrashInfo2File(String crashInfo) {
        try {
            String fileName = "crash-" + this.formatter.format(new Date()) + "-" + System.currentTimeMillis() + ".log";
            String picPath = String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath()) + "/unibroad/crash/";
            if (Environment.getExternalStorageState().equals("mounted")) {
                File dir = new File(picPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(String.valueOf(picPath) + fileName);
                fos.write(crashInfo.toString().getBytes());
                fos.close();
            }
            return String.valueOf(picPath) + fileName;
        } catch (Exception ex) {
            Log.e(TAG, "an error occured while writing file...", ex);
            return null;
        }
    }

    private String getCrashInfo(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        sb.append("DATA=" + this.formatter.format(new Date()) + "\n");
        for (Map.Entry<String, String> entry : this.infos.entrySet()) {
            sb.append(String.valueOf(entry.getKey()) + "=" + entry.getValue() + "\n");
        }
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        for (Throwable cause = ex.getCause(); cause != null; cause = cause.getCause()) {
            cause.printStackTrace(printWriter);
        }
        printWriter.close();
        sb.append(writer.toString());
        return sb.toString();
    }
}
