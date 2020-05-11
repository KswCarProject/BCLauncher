package com.touchus.benchilauncher.base;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;
import com.touchus.benchilauncher.LauncherApplication;
import java.lang.ref.WeakReference;

public abstract class BaseHandlerFragment extends Fragment {
    public MyHandler mMyHandler = new MyHandler(this);

    public abstract void handlerMsg(Message message);

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void onStart() {
        super.onStart();
        LauncherApplication.getInstance().registerHandler(this.mMyHandler);
    }

    public void onStop() {
        super.onStop();
        LauncherApplication.getInstance().unregisterHandler(this.mMyHandler);
    }

    public void onDestroyView() {
        super.onDestroyView();
    }

    public static class MyHandler extends Handler {
        private WeakReference<BaseHandlerFragment> target;

        public MyHandler(BaseHandlerFragment fragment) {
            this.target = new WeakReference<>(fragment);
        }

        public void handleMessage(Message msg) {
            if (this.target != null && this.target.get() != null) {
                ((BaseHandlerFragment) this.target.get()).handlerMsg(msg);
            }
        }
    }
}
