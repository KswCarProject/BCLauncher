package com.touchus.benchilauncher.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import com.backaudio.android.driver.Mainboard;
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.benchilauncher.R;
import com.touchus.benchilauncher.SysConst;
import com.touchus.benchilauncher.utils.ToastTool;
import com.touchus.publicutils.sysconst.BenzModel;
import com.touchus.publicutils.utils.MailManager;
import com.touchus.publicutils.utils.TimeUtils;
import com.touchus.publicutils.utils.UtilTools;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class FeedbackDialog extends Dialog implements View.OnClickListener {
    private LauncherApplication app;
    private Button cancelBtn;
    private Context context;
    private boolean iInCancelState = true;
    public MHandler mHandler = new MHandler(this);
    private EditText phoneNum;
    private Button submitBtn;
    private EditText suggest;
    private String suggestStr;

    public FeedbackDialog(Context context2) {
        super(context2);
        this.context = context2;
    }

    public FeedbackDialog(Context context2, int themeResId) {
        super(context2, themeResId);
        this.context = context2;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_feedback);
        this.app = (LauncherApplication) this.context.getApplicationContext();
        this.suggest = (EditText) findViewById(R.id.suggest);
        this.phoneNum = (EditText) findViewById(R.id.phoneNum);
        this.submitBtn = (Button) findViewById(R.id.affirmBtn);
        this.cancelBtn = (Button) findViewById(R.id.cancelBtn);
        this.submitBtn.setOnClickListener(this);
        this.cancelBtn.setOnClickListener(this);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
        layoutParams.x = 400;
        layoutParams.y = 0;
        dialogWindow.setAttributes(layoutParams);
        this.submitBtn.setVisibility(0);
        this.cancelBtn.setVisibility(0);
        this.submitBtn.setEnabled(true);
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        this.app.registerHandler(this.mHandler);
        super.onStart();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        this.app.unregisterHandler(this.mHandler);
        super.onStop();
    }

    public void dismiss() {
        this.app.currentDialog3 = null;
        super.dismiss();
    }

    public void onClick(View v) {
        if (v.getId() == R.id.cancelBtn) {
            dismiss();
            return;
        }
        this.suggestStr = this.suggest.getText().toString();
        if (TextUtils.isEmpty(this.suggestStr)) {
            this.suggest.setError(this.context.getString(R.string.setting_feedback_error));
        } else if (UtilTools.isNetworkConnected(this.context)) {
            submit();
        } else {
            ToastTool.showShortToast(this.context, this.context.getString(R.string.net_error));
        }
    }

    private void submit() {
        String path = this.context.getApplicationContext().getFilesDir() + File.separator;
        Log.e("", "path = " + path);
        File[] files = new File(path).listFiles();
        List<String> pathList = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            Log.e("", "files[i].getAbsolutePath() = " + files[i].getAbsolutePath());
            pathList.add(files[i].getAbsolutePath());
        }
        String path2 = "/data/anr" + File.separator;
        Log.e("", "path = " + path2);
        File[] files2 = new File(path2).listFiles();
        for (int i2 = 0; i2 < files2.length; i2++) {
            Log.e("", "files[i].getAbsolutePath() = " + files2[i2].getAbsolutePath());
            if (!files2[i2].isDirectory()) {
                pathList.add(files2[i2].getAbsolutePath());
            }
        }
        if (pathList.size() != 0) {
            MailManager.getInstance(this.context).sendMailWithMultiFile(String.valueOf(BenzModel.benzName()) + "奔驰反馈收集" + TimeUtils.getSimpleDate(), "反馈意见：" + this.suggestStr + "\n联系号码：" + this.phoneNum.getText().toString() + "地址：" + this.app.address, pathList);
            dismiss();
        }
    }

    static class MHandler extends Handler {
        private WeakReference<FeedbackDialog> target;

        public MHandler(FeedbackDialog activity) {
            this.target = new WeakReference<>(activity);
        }

        public void handleMessage(Message msg) {
            if (this.target.get() != null) {
                ((FeedbackDialog) this.target.get()).handlerMsgUSB(msg);
            }
        }
    }

    public void handlerMsgUSB(Message msg) {
        if (msg.what == 6001) {
            byte idriverCode = msg.getData().getByte(SysConst.IDRIVER_ENUM);
            if (idriverCode == Mainboard.EIdriverEnum.TURN_RIGHT.getCode() || idriverCode == Mainboard.EIdriverEnum.RIGHT.getCode()) {
                this.iInCancelState = true;
            } else if (idriverCode == Mainboard.EIdriverEnum.TURN_LEFT.getCode() || idriverCode == Mainboard.EIdriverEnum.LEFT.getCode()) {
                this.iInCancelState = false;
            } else if (idriverCode == Mainboard.EIdriverEnum.PRESS.getCode()) {
                if (this.iInCancelState) {
                    dismiss();
                } else {
                    submit();
                }
            } else if (idriverCode == Mainboard.EIdriverEnum.BACK.getCode()) {
                dismiss();
            } else if (idriverCode == Mainboard.EIdriverEnum.HOME.getCode() || idriverCode == Mainboard.EIdriverEnum.STAR_BTN.getCode()) {
                dismiss();
            }
        }
    }
}
