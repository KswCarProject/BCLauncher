package com.touchus.benchilauncher.utils;

import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class ServerFinally implements Runnable {
    private static final String TAG = "ServerFinally";
    LocalSocket client;
    Handler handler;
    BufferedReader is;
    PrintWriter os;
    LocalServerSocket server;

    public ServerFinally(Handler handler2) {
        this.handler = handler2;
    }

    public void send(String data) {
        if (this.os != null) {
            this.os.println(data);
            this.os.flush();
        }
    }

    public void run() {
        Log.i(TAG, "Server=======打开服务=========");
        try {
            this.server = new LocalServerSocket("com.papago");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        while (true) {
            try {
                this.client = this.server.accept();
                Log.i(TAG, "Server=======客户端连接成功=========");
                Log.i(TAG, "===客户端ID为:" + this.client.getPeerCredentials().getUid());
                this.os = new PrintWriter(this.client.getOutputStream());
                Message msg = this.handler.obtainMessage();
                msg.what = 17;
                this.handler.sendMessage(msg);
            } catch (IOException e12) {
                e12.printStackTrace();
            }
            new Thread(new Runnable() {
                public void run() {
                    ServerFinally.this.getClient();
                }
            }).start();
        }
    }

    public void getClient() {
        String result = "";
        while (true) {
            try {
                this.is = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
                result = this.is.readLine();
                Log.i(TAG, "===result:" + result);
                Message msg = this.handler.obtainMessage();
                msg.obj = result;
                if (TextUtils.isEmpty(result)) {
                    msg.what = 18;
                    this.handler.sendMessage(msg);
                    return;
                }
                msg.what = 17;
                this.handler.sendMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();
                Log.i(TAG, "===IOException:" + result);
            }
        }
    }

    public void close() {
        try {
            if (this.os != null) {
                this.os.close();
            }
            if (this.is != null) {
                this.is.close();
            }
            if (this.client != null) {
                this.client.close();
            }
            if (this.server != null) {
                this.server.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
