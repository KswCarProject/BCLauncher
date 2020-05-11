package com.touchus.benchilauncher.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import com.touchus.benchilauncher.R;
import com.touchus.benchilauncher.activity.main.right.call.CallPhoneFragment;

public class CallPhoneKeyDialog extends Dialog implements View.OnClickListener {
    View lastview = null;
    private ImageView number0Img;
    private ImageView number1Img;
    private ImageView number2Img;
    private ImageView number3Img;
    private ImageView number4Img;
    private ImageView number5Img;
    private ImageView number6Img;
    private ImageView number7Img;
    private ImageView number8Img;
    private ImageView number9Img;
    private ImageView numberOtherImg;
    private ImageView numberStarImg;
    public CallPhoneFragment parent;

    public CallPhoneKeyDialog(Context context) {
        super(context);
    }

    public CallPhoneKeyDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_call_phone_keys);
        initView();
        initListener();
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
        dialogWindow.setGravity(19);
        layoutParams.x = 0;
        layoutParams.y = -50;
        dialogWindow.setAttributes(layoutParams);
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
    }

    public void onClick(View v) {
        if (this.parent != null) {
            if (!(this.lastview == null || this.lastview == v)) {
                this.lastview.setSelected(false);
            }
            this.lastview = v;
            v.setSelected(true);
            if (v.getId() == R.id.img_number_0) {
                this.parent.upListenSelectBtn(1);
            } else if (v.getId() == R.id.img_number_1) {
                this.parent.upListenSelectBtn(2);
            } else if (v.getId() == R.id.img_number_2) {
                this.parent.upListenSelectBtn(3);
            } else if (v.getId() == R.id.img_number_3) {
                this.parent.upListenSelectBtn(4);
            } else if (v.getId() == R.id.img_number_4) {
                this.parent.upListenSelectBtn(5);
            } else if (v.getId() == R.id.img_number_5) {
                this.parent.upListenSelectBtn(6);
            } else if (v.getId() == R.id.img_number_6) {
                this.parent.upListenSelectBtn(7);
            } else if (v.getId() == R.id.img_number_7) {
                this.parent.upListenSelectBtn(8);
            } else if (v.getId() == R.id.img_number_8) {
                this.parent.upListenSelectBtn(9);
            } else if (v.getId() == R.id.img_number_9) {
                this.parent.upListenSelectBtn(10);
            } else if (v.getId() == R.id.img_number_star) {
                this.parent.upListenSelectBtn(11);
            } else if (v.getId() == R.id.img_number_other) {
                this.parent.upListenSelectBtn(12);
            }
        }
    }

    private void initView() {
        this.number1Img = (ImageView) findViewById(R.id.img_number_1);
        this.number2Img = (ImageView) findViewById(R.id.img_number_2);
        this.number3Img = (ImageView) findViewById(R.id.img_number_3);
        this.number4Img = (ImageView) findViewById(R.id.img_number_4);
        this.number5Img = (ImageView) findViewById(R.id.img_number_5);
        this.number6Img = (ImageView) findViewById(R.id.img_number_6);
        this.number7Img = (ImageView) findViewById(R.id.img_number_7);
        this.number8Img = (ImageView) findViewById(R.id.img_number_8);
        this.number9Img = (ImageView) findViewById(R.id.img_number_9);
        this.number0Img = (ImageView) findViewById(R.id.img_number_0);
        this.numberStarImg = (ImageView) findViewById(R.id.img_number_star);
        this.numberOtherImg = (ImageView) findViewById(R.id.img_number_other);
    }

    private void initListener() {
        this.number0Img.setOnClickListener(this);
        this.number1Img.setOnClickListener(this);
        this.number2Img.setOnClickListener(this);
        this.number3Img.setOnClickListener(this);
        this.number4Img.setOnClickListener(this);
        this.number5Img.setOnClickListener(this);
        this.number6Img.setOnClickListener(this);
        this.number7Img.setOnClickListener(this);
        this.number8Img.setOnClickListener(this);
        this.number9Img.setOnClickListener(this);
        this.numberStarImg.setOnClickListener(this);
        this.numberOtherImg.setOnClickListener(this);
    }
}
