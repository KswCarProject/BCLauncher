package com.touchus.benchilauncher.activity.main.right.Menu;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.backaudio.android.driver.Mainboard;
import com.touchus.benchilauncher.Launcher;
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.benchilauncher.R;
import com.touchus.benchilauncher.SysConst;
import com.touchus.benchilauncher.base.AbstraSlide;
import com.touchus.benchilauncher.base.BaseHandlerFragment;
import com.touchus.benchilauncher.views.MenuSlide;
import com.touchus.benchilauncher.views.MyMenuView;
import java.util.Locale;

public class MenuFragment extends BaseHandlerFragment {
    public static int mCurIndex;
    public static int mCurPage;
    ImageView mImgPoint0;
    ImageView mImgPoint1;
    ImageView mImgPoint2;
    Launcher mLaucher;
    LinearLayout mMenuContainer;
    MenuSlide mMenuSlide;
    LinearLayout.LayoutParams mParams;
    SlideRunnable mSlideRunnable = new SlideRunnable();
    SparseArray<View> mSpaArrBigMenu = new SparseArray<>();
    SparseArray<View> mSpaArrPoint = new SparseArray<>();
    SparseArray<View> mSpaArrSmallMenu = new SparseArray<>();
    /* access modifiers changed from: private */
    public int pagesize = 5;

    public static MenuFragment newInstance(int pagesize2) {
        MenuFragment f = new MenuFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("pagesize", pagesize2);
        f.setArguments(bundle);
        return f;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_right_menu, container, false);
        this.mMenuSlide = (MenuSlide) view.findViewById(R.id.mainright_menu_slide);
        this.mMenuContainer = (LinearLayout) view.findViewById(R.id.mainright_menu_container);
        this.mImgPoint0 = (ImageView) view.findViewById(R.id.mainright_menu_point0);
        this.mImgPoint1 = (ImageView) view.findViewById(R.id.mainright_menu_point1);
        this.mImgPoint2 = (ImageView) view.findViewById(R.id.mainright_menu_point2);
        this.mLaucher = (Launcher) getActivity();
        init();
        Log.e("", "onCreateView");
        this.mLaucher.mMenuShow = true;
        return view;
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.mLaucher.mMenuShow = false;
    }

    public void onStart() {
        super.onStart();
    }

    public void handlerMsg(Message msg) {
        switch (msg.what) {
            case SysConst.IDRVIER_STATE:
                byte mIDRIVERENUM = msg.getData().getByte(SysConst.IDRIVER_ENUM);
                if (mIDRIVERENUM == Mainboard.EIdriverEnum.TURN_RIGHT.getCode() || mIDRIVERENUM == Mainboard.EIdriverEnum.RIGHT.getCode()) {
                    next();
                    return;
                } else if (mIDRIVERENUM == Mainboard.EIdriverEnum.TURN_LEFT.getCode() || mIDRIVERENUM == Mainboard.EIdriverEnum.LEFT.getCode()) {
                    pre();
                    return;
                } else if (mIDRIVERENUM == Mainboard.EIdriverEnum.PRESS.getCode()) {
                    this.mLaucher.goTo(mCurIndex);
                    return;
                } else {
                    return;
                }
            default:
                return;
        }
    }

    public synchronized void select(int index) {
        if (index < this.pagesize) {
            mCurPage = 0;
        } else if (index < this.pagesize * 2) {
            mCurPage = 1;
        } else if (index < this.pagesize * 3) {
            mCurPage = 2;
        }
        showBig(index);
        mCurIndex = index;
        selectPoint(mCurPage);
    }

    public void showSmall(int index) {
        this.mSpaArrBigMenu.get(index).setVisibility(8);
        this.mSpaArrSmallMenu.get(index).setVisibility(0);
    }

    public void showBig(int index) {
        for (int i = 0; i < this.mSpaArrBigMenu.size(); i++) {
            if (i == index) {
                this.mSpaArrBigMenu.get(i).setVisibility(0);
                this.mSpaArrSmallMenu.get(i).setVisibility(8);
            } else {
                this.mSpaArrBigMenu.get(i).setVisibility(8);
                this.mSpaArrSmallMenu.get(i).setVisibility(0);
            }
        }
    }

    public void selectPoint(int index) {
        for (int i = 0; i < 2; i++) {
            this.mSpaArrPoint.get(i).setSelected(false);
        }
        this.mSpaArrPoint.get(index).setSelected(true);
    }

    public void slideMenu(int page) {
        this.mMenuSlide.slide2Page(page, 500);
    }

    public void pre() {
        if (mCurIndex != 0) {
            select(mCurIndex - 1);
            this.mMyHandler.postDelayed(this.mSlideRunnable, 400);
        }
    }

    public void next() {
        if (mCurIndex != 9) {
            select(mCurIndex + 1);
            this.mMyHandler.postDelayed(this.mSlideRunnable, 400);
        }
    }

    public void press() {
        this.mLaucher.goTo(mCurIndex);
    }

    class SlideRunnable implements Runnable {
        SlideRunnable() {
        }

        public void run() {
            MenuFragment.this.slideMenu(MenuFragment.mCurPage);
        }
    }

    private void init() {
        this.pagesize = getArguments().getInt("pagesize");
        for (int i = 0; i < SysConst.MENU_LIST.length; i++) {
            MyMenuView imgV = new MyMenuView(getActivity());
            imgV.setImageBitmap(SysConst.MENU_LIST[i], SysConst.MENU_SMALL_ICON[i], false);
            addView(imgV, 0);
            MyMenuView imgV2 = new MyMenuView(getActivity());
            imgV2.setImageBitmap(SysConst.MENU_LIST[i], SysConst.MENU_BIG_ICON[i], true);
            addView(imgV2, 1);
        }
        this.mSpaArrPoint.append(0, this.mImgPoint0);
        this.mSpaArrPoint.append(1, this.mImgPoint1);
        for (int i2 = 0; i2 < 10; i2++) {
            this.mSpaArrSmallMenu.append(i2, this.mMenuContainer.getChildAt(i2 * 2));
            this.mSpaArrSmallMenu.get(i2).setVisibility(0);
            final int finalI = i2;
            this.mMenuContainer.getChildAt(i2 * 2).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    MenuFragment.this.select(finalI);
                    MenuFragment.this.mLaucher.goTo(finalI);
                }
            });
            this.mSpaArrBigMenu.append(i2, this.mMenuContainer.getChildAt((i2 * 2) + 1));
            this.mSpaArrBigMenu.get(i2).setVisibility(8);
            final int finalI1 = i2;
            this.mSpaArrBigMenu.get(i2).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    MenuFragment.this.mLaucher.goTo(finalI1);
                }
            });
        }
        selectMenu(mCurIndex);
        this.mMenuSlide.setPagesize(this.pagesize);
        this.mMenuSlide.setOnSlideListener(new AbstraSlide.OnSlipeListener() {
            public void lesten(int desc) {
                switch (desc) {
                    case 3:
                        if (MenuFragment.this.pagesize != 7) {
                            MenuFragment.this.selectDelay(MenuFragment.this.pagesize);
                            return;
                        } else if (MenuFragment.mCurIndex < 3) {
                            MenuFragment.this.selectDelay(3);
                            return;
                        } else {
                            return;
                        }
                    case 4:
                        if (MenuFragment.this.pagesize != 7) {
                            MenuFragment.this.selectDelay(MenuFragment.this.pagesize - 1);
                            return;
                        } else if (MenuFragment.mCurIndex > 6) {
                            MenuFragment.this.selectDelay(6);
                            return;
                        } else {
                            return;
                        }
                    default:
                        return;
                }
            }
        });
    }

    public void selectMenu(int index) {
        if (index < this.pagesize) {
            mCurPage = 0;
        } else if (index < this.pagesize * 2) {
            mCurPage = 1;
        } else if (index < this.pagesize * 3) {
            mCurPage = 2;
        }
        this.mMenuSlide.slide2Page(mCurPage, 0);
        select(index);
    }

    public void selectDelay(final int index) {
        this.mMyHandler.postDelayed(new Runnable() {
            public void run() {
                MenuFragment.this.select(index);
            }
        }, 400);
    }

    public void addView(MyMenuView imgV, int state) {
        if (this.pagesize == 5) {
            if (state == 0) {
                this.mParams = new LinearLayout.LayoutParams(138, 148);
                this.mParams.setMargins(0, 163, 0, 0);
            } else {
                this.mParams = new LinearLayout.LayoutParams(230, 250);
                this.mParams.setMargins(0, 119, 0, 0);
            }
        } else if (state == 0) {
            this.mParams = new LinearLayout.LayoutParams(138, 148);
            this.mParams.setMargins(14, 163, 14, 0);
        } else {
            this.mParams = new LinearLayout.LayoutParams(230, 250);
            this.mParams.setMargins(14, 119, 14, 0);
        }
        if (SysConst.UI_TYPE == 1) {
            if (state == 0) {
                this.mParams = new LinearLayout.LayoutParams(145, 145);
                this.mParams.setMargins(0, 165, 0, 0);
            } else {
                this.mParams = new LinearLayout.LayoutParams(202, 260);
                this.mParams.setMargins(0, 139, 0, 0);
            }
        }
        this.mMenuContainer.addView(imgV, this.mParams);
    }

    public Bitmap createBmp(String s, int rID) {
        int offsetX;
        int index;
        Bitmap imgMarker = BitmapFactory.decodeResource(LauncherApplication.mContext.getResources(), rID);
        int width = imgMarker.getWidth();
        int height = imgMarker.getHeight();
        Bitmap imgTemp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(imgTemp);
        Paint paint = new Paint();
        paint.setDither(true);
        paint.setFilterBitmap(true);
        canvas.drawBitmap(imgMarker, new Rect(0, 0, width, height), new Rect(0, 0, width, height), paint);
        Paint textPaint = new Paint(257);
        textPaint.setTextSkewX(-0.18f);
        textPaint.setTextSize(30.0f);
        textPaint.setTypeface(Typeface.DEFAULT);
        textPaint.setColor(-1);
        Locale local = getActivity().getResources().getConfiguration().locale;
        if (local.getCountry().equals("UK") || local.getCountry().equals("US")) {
            int[] earr = {100, 90, 80, 60, 60, 60, 40};
            if (s.length() - 3 >= earr.length) {
                index = earr.length - 1;
            } else {
                index = s.length() - 3;
            }
            offsetX = earr[index];
        } else {
            offsetX = new int[]{90, 75, 60, 70}[s.length() - 2];
        }
        canvas.drawText(s, (float) offsetX, 195.0f, textPaint);
        return imgTemp;
    }

    public Bitmap createBmp1(String s, int rID) {
        int offsetX;
        int index;
        Bitmap imgMarker = BitmapFactory.decodeResource(LauncherApplication.mContext.getResources(), rID);
        int width = imgMarker.getWidth();
        int height = imgMarker.getHeight();
        Bitmap imgTemp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(imgTemp);
        Paint paint = new Paint();
        paint.setDither(true);
        paint.setFilterBitmap(true);
        canvas.drawBitmap(imgMarker, new Rect(0, 0, width, height), new Rect(0, 0, width, height), paint);
        Paint textPaint = new Paint(257);
        textPaint.setTextSkewX(-0.18f);
        textPaint.setTextSize(17.76f);
        textPaint.setTypeface(Typeface.DEFAULT);
        textPaint.setColor(-1);
        Locale local = getActivity().getResources().getConfiguration().locale;
        if (local.getCountry().equals("UK") || local.getCountry().equals("US")) {
            int[] earr = {95, 85, 75, 60, 60, 60, 40};
            if (s.length() - 3 >= earr.length) {
                index = earr.length - 1;
            } else {
                index = s.length() - 3;
            }
            offsetX = earr[index];
        } else {
            offsetX = new int[]{90, 75, 60, 70}[s.length() - 2];
        }
        canvas.drawText(s, ((float) offsetX) * 0.592f, 115.44f, textPaint);
        return imgTemp;
    }
}
