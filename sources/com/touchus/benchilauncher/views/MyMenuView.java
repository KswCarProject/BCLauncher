package com.touchus.benchilauncher.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.touchus.benchilauncher.R;
import com.touchus.benchilauncher.SysConst;
import org.apache.log4j.net.SyslogAppender;

public class MyMenuView extends RelativeLayout {
    private Context context;
    private Drawable iconDrawable;
    private boolean isBig;
    LinearLayout.LayoutParams layoutParams;
    private ImageView menuIcon;
    private String menuName;
    private TextView menuTv;

    public MyMenuView(Context context2) {
        this(context2, (AttributeSet) null);
    }

    public MyMenuView(Context context2, AttributeSet attrs) {
        super(context2, attrs);
        this.layoutParams = new LinearLayout.LayoutParams(-2, -2);
        this.context = context2;
        ((LayoutInflater) context2.getSystemService("layout_inflater")).inflate(R.layout.menu_view, this);
        this.menuIcon = (ImageView) findViewById(R.id.menu_icon);
        this.menuTv = (TextView) findViewById(R.id.menu_tv);
    }

    public void setImageBitmap(int menuName2, int icon, boolean iBig) {
        this.menuIcon.setImageResource(icon);
        this.menuTv.setText(menuName2);
        if (SysConst.UI_TYPE == 1) {
            if (iBig) {
                this.menuTv.setTextSize(30.0f);
                this.layoutParams.setMargins(0, 133, 0, 0);
                this.menuTv.setLayoutParams(this.layoutParams);
                return;
            }
            this.menuTv.setTextSize(18.0f);
            this.layoutParams.setMargins(0, 95, 0, 0);
            this.menuTv.setLayoutParams(this.layoutParams);
        } else if (iBig) {
            this.menuTv.setTextSize(30.0f);
            this.layoutParams.setMargins(0, SyslogAppender.LOG_LOCAL4, 0, 0);
            this.menuTv.setLayoutParams(this.layoutParams);
        } else {
            this.menuTv.setTextSize(18.0f);
            this.layoutParams.setMargins(0, 97, 0, 0);
            this.menuTv.setLayoutParams(this.layoutParams);
        }
    }
}
