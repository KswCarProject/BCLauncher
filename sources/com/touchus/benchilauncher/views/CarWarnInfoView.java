package com.touchus.benchilauncher.views;

import android.content.Context;
import android.support.v4.internal.view.SupportMenu;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.touchus.benchilauncher.R;

public class CarWarnInfoView extends LinearLayout {
    private ImageView infoImg;
    private TextView infoText;

    public CarWarnInfoView(Context context) {
        this(context, (AttributeSet) null);
    }

    public CarWarnInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.car_warninfo_item, this, true);
        this.infoImg = (ImageView) findViewById(R.id.info_img);
        this.infoText = (TextView) findViewById(R.id.info_text);
    }

    public void setCarInfo(boolean isWarning, int imgres, String value) {
        if (isWarning) {
            this.infoImg.setImageResource(imgres);
            this.infoText.setTextColor(SupportMenu.CATEGORY_MASK);
            this.infoText.setText(value);
            return;
        }
        this.infoImg.setImageResource(imgres);
        this.infoText.setTextColor(-16777216);
        this.infoText.setText(value);
    }
}
