package com.touchus.benchilauncher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.touchus.benchilauncher.utils.ToastTool;
import com.touchus.publicutils.utils.MailManager;

public class PublibReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if (!MailManager.ACTION_FEEDBACK_MAIL.equals(intent.getAction())) {
            return;
        }
        if (intent.getBooleanExtra(MailManager.FEEDBACK_KEY, false)) {
            ToastTool.showShortToast(context, "send successful");
        } else {
            ToastTool.showShortToast(context, "send failed");
        }
    }
}
