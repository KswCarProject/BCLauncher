package com.touchus.benchilauncher.views;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.touchus.benchilauncher.R;
import com.touchus.benchilauncher.inface.OnItemClickListener;
import com.touchus.benchilauncher.inface.OnItemSelectedListener;
import com.touchus.benchilauncher.inface.OnLoopViewTouchListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import javax.mail.internet.HeaderTokenizer;

public class LoopRotarySwitchView extends RelativeLayout {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$touchus$benchilauncher$views$LoopRotarySwitchView$AutoScrollDirection = null;
    private static final int LoopR = 200;
    private static final float RADIO_DEFAULT_CHILD_DIMENSION = 0.25f;
    private static final int horizontal = 1;
    private static final int vertical = 0;
    private float RADIO_DEFAULT_CENTERITEM_DIMENSION;
    /* access modifiers changed from: private */
    public float angle;
    /* access modifiers changed from: private */
    public AutoScrollDirection autoRotatinDirection;
    private boolean autoRotation;
    protected boolean clickRotation;
    private float distance;
    /* access modifiers changed from: private */
    public boolean isCanClickListener;
    private boolean isFirst;
    private float last_angle;
    private float limitX;
    LoopRotarySwitchViewHandler loopHandler;
    /* access modifiers changed from: private */
    public int loopRotationX;
    /* access modifiers changed from: private */
    public int loopRotationZ;
    private Context mContext;
    private GestureDetector mGestureDetector;
    private int mOrientation;
    private float multiple;
    /* access modifiers changed from: private */
    public OnItemClickListener onItemClickListener;
    /* access modifiers changed from: private */
    public OnItemSelectedListener onItemSelectedListener;
    private OnLoopViewTouchListener onLoopViewTouchListener;
    /* access modifiers changed from: private */
    public float r;
    private ValueAnimator rAnimation;
    private ValueAnimator restAnimator;
    /* access modifiers changed from: private */
    public int selectItem;
    /* access modifiers changed from: private */
    public int size;
    private List<TextView> textViews;
    private String[] texts;
    /* access modifiers changed from: private */
    public boolean touching;
    /* access modifiers changed from: private */
    public List<View> views;
    private float x;
    private ValueAnimator xAnimation;
    private ValueAnimator zAnimation;

    public enum AutoScrollDirection {
        left,
        right
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$touchus$benchilauncher$views$LoopRotarySwitchView$AutoScrollDirection() {
        int[] iArr = $SWITCH_TABLE$com$touchus$benchilauncher$views$LoopRotarySwitchView$AutoScrollDirection;
        if (iArr == null) {
            iArr = new int[AutoScrollDirection.values().length];
            try {
                iArr[AutoScrollDirection.left.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[AutoScrollDirection.right.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            $SWITCH_TABLE$com$touchus$benchilauncher$views$LoopRotarySwitchView$AutoScrollDirection = iArr;
        }
        return iArr;
    }

    public LoopRotarySwitchView(Context context) {
        this(context, (AttributeSet) null);
    }

    public LoopRotarySwitchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoopRotarySwitchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mOrientation = 1;
        this.restAnimator = null;
        this.rAnimation = null;
        this.zAnimation = null;
        this.xAnimation = null;
        this.loopRotationX = 0;
        this.loopRotationZ = 0;
        this.mGestureDetector = null;
        this.selectItem = 0;
        this.size = 5;
        this.r = 200.0f;
        this.multiple = 2.0f;
        this.distance = this.multiple * this.r;
        this.angle = 0.0f;
        this.last_angle = 0.0f;
        this.clickRotation = true;
        this.autoRotation = false;
        this.touching = false;
        this.isFirst = false;
        this.autoRotatinDirection = AutoScrollDirection.right;
        this.views = new ArrayList();
        this.onItemSelectedListener = null;
        this.onLoopViewTouchListener = null;
        this.onItemClickListener = null;
        this.isCanClickListener = true;
        this.limitX = 30.0f;
        this.RADIO_DEFAULT_CENTERITEM_DIMENSION = 0.5f;
        this.loopHandler = new LoopRotarySwitchViewHandler(3000) {
            private static /* synthetic */ int[] $SWITCH_TABLE$com$touchus$benchilauncher$views$LoopRotarySwitchView$AutoScrollDirection;

            static /* synthetic */ int[] $SWITCH_TABLE$com$touchus$benchilauncher$views$LoopRotarySwitchView$AutoScrollDirection() {
                int[] iArr = $SWITCH_TABLE$com$touchus$benchilauncher$views$LoopRotarySwitchView$AutoScrollDirection;
                if (iArr == null) {
                    iArr = new int[AutoScrollDirection.values().length];
                    try {
                        iArr[AutoScrollDirection.left.ordinal()] = 1;
                    } catch (NoSuchFieldError e) {
                    }
                    try {
                        iArr[AutoScrollDirection.right.ordinal()] = 2;
                    } catch (NoSuchFieldError e2) {
                    }
                    $SWITCH_TABLE$com$touchus$benchilauncher$views$LoopRotarySwitchView$AutoScrollDirection = iArr;
                }
                return iArr;
            }

            public void doScroll() {
                try {
                    if (LoopRotarySwitchView.this.size != 0) {
                        int perAngle = 0;
                        switch ($SWITCH_TABLE$com$touchus$benchilauncher$views$LoopRotarySwitchView$AutoScrollDirection()[LoopRotarySwitchView.this.autoRotatinDirection.ordinal()]) {
                            case 1:
                                perAngle = 360 / LoopRotarySwitchView.this.size;
                                break;
                            case 2:
                                perAngle = -360 / LoopRotarySwitchView.this.size;
                                break;
                        }
                        if (LoopRotarySwitchView.this.angle == 360.0f) {
                            LoopRotarySwitchView.this.angle = 0.0f;
                        }
                        LoopRotarySwitchView.this.AnimRotationTo(LoopRotarySwitchView.this.angle + ((float) perAngle), (Runnable) null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        this.textViews = new ArrayList();
        this.mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LoopRotarySwitchView);
        this.mOrientation = typedArray.getInt(0, 1);
        this.autoRotation = typedArray.getBoolean(1, false);
        this.r = typedArray.getDimension(2, 200.0f);
        int direction = typedArray.getInt(3, 0);
        typedArray.recycle();
        this.mGestureDetector = new GestureDetector(context, getGeomeryController());
        if (this.mOrientation == 1) {
            this.loopRotationZ = 0;
        } else {
            this.loopRotationZ = 90;
        }
        if (direction == 0) {
            this.autoRotatinDirection = AutoScrollDirection.left;
        } else {
            this.autoRotatinDirection = AutoScrollDirection.right;
        }
        this.loopHandler.setLoop(this.autoRotation);
    }

    public void doRotain() {
        if (this.clickRotation) {
            this.clickRotation = false;
            try {
                if (this.size != 0) {
                    int perAngle = 0;
                    switch ($SWITCH_TABLE$com$touchus$benchilauncher$views$LoopRotarySwitchView$AutoScrollDirection()[this.autoRotatinDirection.ordinal()]) {
                        case 1:
                            perAngle = 360 / this.size;
                            break;
                        case 2:
                            perAngle = -360 / this.size;
                            break;
                    }
                    if (this.angle == 360.0f) {
                        this.angle = 0.0f;
                    }
                    AnimRotationTo(this.angle + ((float) perAngle), (Runnable) null);
                }
            } catch (Exception e) {
                e.printStackTrace();
                this.clickRotation = true;
            }
        }
    }

    private <T> void sortList(List<View> list) {
        Comparator comparator = new SortComparator(this, (SortComparator) null);
        Object[] array = list.toArray(new Object[list.size()]);
        Arrays.sort(array, comparator);
        int i = 0;
        ListIterator<View> listIterator = list.listIterator();
        while (listIterator.hasNext()) {
            listIterator.next();
            listIterator.set(array[i]);
            i++;
        }
        for (int j = 0; j < list.size(); j++) {
            list.get(j).bringToFront();
        }
    }

    private class SortComparator implements Comparator<View> {
        private SortComparator() {
        }

        /* synthetic */ SortComparator(LoopRotarySwitchView loopRotarySwitchView, SortComparator sortComparator) {
            this();
        }

        public int compare(View lhs, View rhs) {
            try {
                return (int) ((lhs.getScaleX() * 1000.0f) - (rhs.getScaleX() * 1000.0f));
            } catch (Exception e) {
                return 0;
            }
        }
    }

    public GestureDetector.SimpleOnGestureListener getGeomeryController() {
        return new GestureDetector.SimpleOnGestureListener() {
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                LoopRotarySwitchView loopRotarySwitchView = LoopRotarySwitchView.this;
                loopRotarySwitchView.angle = (float) (((double) loopRotarySwitchView.angle) + (Math.cos(Math.toRadians((double) LoopRotarySwitchView.this.loopRotationZ)) * ((double) (distanceX / ((float) LoopRotarySwitchView.this.views.size())))) + (Math.sin(Math.toRadians((double) LoopRotarySwitchView.this.loopRotationZ)) * ((double) (distanceY / ((float) LoopRotarySwitchView.this.views.size())))));
                LoopRotarySwitchView.this.initView();
                return true;
            }
        };
    }

    /* access modifiers changed from: private */
    public void initView() {
        for (int i = 0; i < this.views.size(); i++) {
            double radians = (double) ((this.angle + 180.0f) - ((float) ((i * 360) / this.size)));
            float x0 = ((float) Math.sin(Math.toRadians(radians))) * this.r;
            float scale0 = (this.distance - (((float) Math.cos(Math.toRadians(radians))) * this.r)) / (this.distance + this.r);
            this.views.get(i).setScaleX(scale0);
            this.views.get(i).setScaleY(scale0);
            float rotationX_y = ((float) Math.sin(Math.toRadians(((double) this.loopRotationX) * Math.cos(Math.toRadians(radians))))) * this.r;
            float rotationZ_y = (-((float) Math.sin(Math.toRadians((double) (-this.loopRotationZ))))) * x0;
            this.views.get(i).setTranslationX(x0 + ((((float) Math.cos(Math.toRadians((double) (-this.loopRotationZ)))) * x0) - x0));
            this.views.get(i).setTranslationY(rotationX_y + rotationZ_y);
        }
        List<View> arrayViewList = new ArrayList<>();
        arrayViewList.clear();
        for (int i2 = 0; i2 < this.views.size(); i2++) {
            arrayViewList.add(this.views.get(i2));
        }
        sortList(arrayViewList);
        postInvalidate();
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initView();
        if (this.autoRotation) {
            this.loopHandler.sendEmptyMessageDelayed(1000, this.loopHandler.loopTime);
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r2, int b) {
        super.onLayout(changed, l, t, r2, b);
        if (changed && !this.isFirst) {
            this.isFirst = true;
            checkChildView();
            if (this.onItemSelectedListener != null) {
                this.isCanClickListener = true;
                this.onItemSelectedListener.selected(this.selectItem, this.views.get(this.selectItem));
            }
            RAnimation();
        }
    }

    public void RAnimation() {
        RAnimation(1.0f, 200.0f);
    }

    public void RAnimation(boolean fromZeroToLoopR) {
        if (fromZeroToLoopR) {
            RAnimation(1.0f, 200.0f);
        } else {
            RAnimation(200.0f, 1.0f);
        }
    }

    public void RAnimation(float from, float to) {
        this.rAnimation = ValueAnimator.ofFloat(new float[]{from, to});
        this.rAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                LoopRotarySwitchView.this.r = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                LoopRotarySwitchView.this.initView();
            }
        });
        this.rAnimation.setInterpolator(new DecelerateInterpolator());
        this.rAnimation.setDuration(2000);
        this.rAnimation.start();
    }

    public void setChildViewText(String[] texts2) {
        this.texts = texts2;
    }

    public List<TextView> getTextViews() {
        return this.textViews;
    }

    public void setTextViewText() {
        int count = getChildCount();
        if (this.texts != null && this.texts.length != 0 && this.textViews.size() != 0) {
            for (int i = 0; i < count; i++) {
                if (this.texts != null && this.texts.length == count) {
                    if (this.texts.length != 0) {
                        switch (i) {
                            case 0:
                                this.textViews.get(i).setText(String.valueOf(this.texts[i]));
                                break;
                            case 1:
                                this.textViews.get(i).setText(String.valueOf(this.texts[i]));
                                break;
                            case 2:
                                this.textViews.get(i).setText(String.valueOf(this.texts[i]));
                                break;
                            case 3:
                                this.textViews.get(i).setText(String.valueOf(this.texts[i]));
                                break;
                            case 4:
                                this.textViews.get(i).setText(String.valueOf(this.texts[i]));
                                break;
                        }
                    } else {
                        return;
                    }
                }
            }
        }
    }

    public void removeview(View v) {
        this.views.remove(v);
    }

    public void checkChildView() {
        for (int i = 0; i < this.views.size(); i++) {
            this.views.remove(i);
        }
        this.textViews.clear();
        int count = getChildCount();
        this.size = count;
        for (int i2 = 0; i2 < count; i2++) {
            View view = getChildAt(i2);
            view.setPivotY(165.0f);
            view.setPivotX(100.0f);
            if (this.texts != null && this.texts.length == count) {
                TextView textView = (TextView) view.findViewById(R.id.loopView_tv);
                textView.setText(String.valueOf(this.texts[i2]));
                this.textViews.add(textView);
            }
            final int position = i2;
            this.views.add(view);
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (position != LoopRotarySwitchView.this.calculateItemPosition(LoopRotarySwitchView.this.calculateItem())) {
                        LoopRotarySwitchView.this.setSelectItem(position);
                    } else if (LoopRotarySwitchView.this.isCanClickListener && LoopRotarySwitchView.this.onItemClickListener != null) {
                        LoopRotarySwitchView.this.onItemClickListener.onItemClick(position, (View) LoopRotarySwitchView.this.views.get(position));
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public int calculateItemPosition(int position) {
        switch (calculateItem()) {
            case HeaderTokenizer.Token.EOF /*-4*/:
                return 1;
            case HeaderTokenizer.Token.COMMENT /*-3*/:
                return 2;
            case -2:
                return 3;
            case -1:
                return 4;
            default:
                return position;
        }
    }

    public void restPosition() {
        float finall;
        if (this.size != 0) {
            float part = (float) (360 / this.size);
            if (this.angle < 0.0f) {
                part = -part;
            }
            float minvalue = ((float) ((int) (this.angle / part))) * part;
            float maxvalue = (((float) ((int) (this.angle / part))) * part) + part;
            if (this.angle >= 0.0f) {
                if (this.angle - this.last_angle > 0.0f) {
                    finall = maxvalue;
                } else {
                    finall = minvalue;
                }
            } else if (this.angle - this.last_angle < 0.0f) {
                finall = maxvalue;
            } else {
                finall = minvalue;
            }
            AnimRotationTo(finall, (Runnable) null);
        }
    }

    /* access modifiers changed from: private */
    public void AnimRotationTo(float finall, final Runnable complete) {
        if (this.angle != finall) {
            this.restAnimator = ValueAnimator.ofFloat(new float[]{this.angle, finall});
            this.restAnimator.setInterpolator(new DecelerateInterpolator());
            this.restAnimator.setDuration(300);
            this.restAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (!LoopRotarySwitchView.this.touching) {
                        LoopRotarySwitchView.this.angle = ((Float) animation.getAnimatedValue()).floatValue();
                        LoopRotarySwitchView.this.initView();
                    }
                }
            });
            this.restAnimator.addListener(new Animator.AnimatorListener() {
                public void onAnimationStart(Animator animation) {
                }

                public void onAnimationEnd(Animator animation) {
                    if (!LoopRotarySwitchView.this.touching) {
                        LoopRotarySwitchView.this.selectItem = LoopRotarySwitchView.this.calculateItem();
                        if (LoopRotarySwitchView.this.selectItem < 0) {
                            LoopRotarySwitchView.this.selectItem = LoopRotarySwitchView.this.size + LoopRotarySwitchView.this.selectItem;
                        }
                        if (LoopRotarySwitchView.this.onItemSelectedListener != null) {
                            if (LoopRotarySwitchView.this.views.size() != 0) {
                                LoopRotarySwitchView.this.onItemSelectedListener.selected(LoopRotarySwitchView.this.selectItem, (View) LoopRotarySwitchView.this.views.get(LoopRotarySwitchView.this.selectItem));
                            } else {
                                return;
                            }
                        }
                    }
                    LoopRotarySwitchView.this.clickRotation = true;
                }

                public void onAnimationCancel(Animator animation) {
                }

                public void onAnimationRepeat(Animator animation) {
                }
            });
            if (complete != null) {
                this.restAnimator.addListener(new Animator.AnimatorListener() {
                    public void onAnimationStart(Animator animation) {
                    }

                    public void onAnimationEnd(Animator animation) {
                        complete.run();
                    }

                    public void onAnimationCancel(Animator animation) {
                    }

                    public void onAnimationRepeat(Animator animation) {
                    }
                });
            }
            this.restAnimator.start();
        }
    }

    /* access modifiers changed from: private */
    public int calculateItem() {
        return ((int) (this.angle / ((float) (360 / this.size)))) % this.size;
    }

    private boolean onTouch(MotionEvent event) {
        if (event.getAction() == 0) {
            this.last_angle = this.angle;
            this.touching = true;
        }
        if (this.mGestureDetector.onTouchEvent(event)) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        if (event.getAction() == 1 || event.getAction() == 3) {
            this.touching = false;
            restPosition();
        }
        return true;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (this.onLoopViewTouchListener != null) {
            this.onLoopViewTouchListener.onTouch(event);
        }
        isCanClickListener(event);
        return true;
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        onTouch(ev);
        if (this.onLoopViewTouchListener != null) {
            this.onLoopViewTouchListener.onTouch(ev);
        }
        isCanClickListener(ev);
        return super.dispatchTouchEvent(ev);
    }

    public void isCanClickListener(MotionEvent event) {
        switch (event.getAction()) {
            case 0:
                this.x = event.getX();
                if (this.autoRotation) {
                    this.loopHandler.removeMessages(1000);
                    return;
                }
                return;
            case 1:
            case 3:
                if (this.autoRotation) {
                    this.loopHandler.sendEmptyMessageDelayed(1000, this.loopHandler.loopTime);
                }
                if (event.getX() - this.x > this.limitX || this.x - event.getX() > this.limitX) {
                    this.isCanClickListener = false;
                    return;
                } else {
                    this.isCanClickListener = true;
                    return;
                }
            default:
                return;
        }
    }

    public List<View> getViews() {
        return this.views;
    }

    public float getAngle() {
        return this.angle;
    }

    public void setAngle(float angle2) {
        this.angle = angle2;
    }

    public float getDistance() {
        return this.distance;
    }

    public void setDistance(float distance2) {
        this.distance = distance2;
    }

    public float getR() {
        return this.r;
    }

    public int getSelectItem() {
        return this.selectItem;
    }

    public void setSize(int size1) {
        this.size = size1;
    }

    public void setSelectItemm(int selectItem2) {
        float finall;
        if (selectItem2 >= 0) {
            float jiaodu = this.angle + ((float) ((360 / this.size) * selectItem2));
            float part = (float) (360 / this.size);
            if (jiaodu < 0.0f) {
                part = -part;
            }
            float minvalue = ((float) ((int) (jiaodu / part))) * part;
            float maxvalue = ((float) ((int) (jiaodu / part))) * part;
            if (jiaodu >= 0.0f) {
                if (jiaodu - this.last_angle > 0.0f) {
                    finall = maxvalue;
                } else {
                    finall = minvalue;
                }
            } else if (jiaodu - this.last_angle < 0.0f) {
                finall = maxvalue;
            } else {
                finall = minvalue;
            }
            if (this.size > 0) {
                AnimRotationTo(finall, (Runnable) null);
            }
        }
    }

    public void setSelectItem(int selectItem2) {
        float jiaodu;
        float finall;
        if (selectItem2 != getSelectItem() && selectItem2 >= 0) {
            if (getSelectItem() == 0) {
                if (selectItem2 == this.views.size() - 1) {
                    jiaodu = this.angle - ((float) (360 / this.size));
                    Log.d("iii", new StringBuilder(String.valueOf(this.size)).toString());
                } else {
                    Log.d("iii", new StringBuilder(String.valueOf(this.size)).toString());
                    jiaodu = this.angle + ((float) (360 / this.size));
                }
            } else if (getSelectItem() == this.views.size() - 1) {
                if (selectItem2 == 0) {
                    jiaodu = this.angle + ((float) (360 / this.size));
                } else {
                    jiaodu = this.angle - ((float) (360 / this.size));
                }
            } else if (selectItem2 > getSelectItem()) {
                jiaodu = this.angle + ((float) (360 / this.size));
            } else {
                jiaodu = this.angle - ((float) (360 / this.size));
            }
            float part = (float) (360 / this.size);
            if (jiaodu < 0.0f) {
                part = -part;
            }
            float minvalue = ((float) ((int) (jiaodu / part))) * part;
            float maxvalue = ((float) ((int) (jiaodu / part))) * part;
            if (jiaodu >= 0.0f) {
                if (jiaodu - this.last_angle > 0.0f) {
                    finall = maxvalue;
                } else {
                    finall = minvalue;
                }
            } else if (jiaodu - this.last_angle < 0.0f) {
                finall = maxvalue;
            } else {
                finall = minvalue;
            }
            if (this.size > 0) {
                AnimRotationTo(finall, (Runnable) null);
            }
        }
    }

    public LoopRotarySwitchView setR(float r2) {
        this.r = r2;
        this.distance = this.multiple * r2;
        return this;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener2) {
        this.onItemSelectedListener = onItemSelectedListener2;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener2) {
        this.onItemClickListener = onItemClickListener2;
    }

    public void setOnLoopViewTouchListener(OnLoopViewTouchListener onLoopViewTouchListener2) {
        this.onLoopViewTouchListener = onLoopViewTouchListener2;
    }

    public LoopRotarySwitchView setAutoRotation(boolean autoRotation2) {
        this.autoRotation = autoRotation2;
        this.loopHandler.setLoop(autoRotation2);
        return this;
    }

    public long getAutoRotationTime() {
        return this.loopHandler.loopTime;
    }

    public LoopRotarySwitchView setAutoRotationTime(long autoRotationTime) {
        this.loopHandler.setLoopTime(autoRotationTime);
        return this;
    }

    public boolean isAutoRotation() {
        return this.autoRotation;
    }

    public LoopRotarySwitchView setMultiple(float mMultiple) {
        this.multiple = mMultiple;
        return this;
    }

    public LoopRotarySwitchView setAutoScrollDirection(AutoScrollDirection mAutoScrollDirection) {
        this.autoRotatinDirection = mAutoScrollDirection;
        return this;
    }

    public void createXAnimation(int from, int to, boolean start) {
        if (this.xAnimation != null && this.xAnimation.isRunning()) {
            this.xAnimation.cancel();
        }
        this.xAnimation = ValueAnimator.ofInt(new int[]{from, to});
        this.xAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                LoopRotarySwitchView.this.loopRotationX = ((Integer) animation.getAnimatedValue()).intValue();
                LoopRotarySwitchView.this.initView();
            }
        });
        this.xAnimation.setInterpolator(new DecelerateInterpolator());
        this.xAnimation.setDuration(2000);
        if (start) {
            this.xAnimation.start();
        }
    }

    public ValueAnimator createZAnimation(int from, int to, boolean start) {
        if (this.zAnimation != null && this.zAnimation.isRunning()) {
            this.zAnimation.cancel();
        }
        this.zAnimation = ValueAnimator.ofInt(new int[]{from, to});
        this.zAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                LoopRotarySwitchView.this.loopRotationZ = ((Integer) animation.getAnimatedValue()).intValue();
                LoopRotarySwitchView.this.initView();
            }
        });
        this.zAnimation.setInterpolator(new DecelerateInterpolator());
        this.zAnimation.setDuration(2000);
        if (start) {
            this.zAnimation.start();
        }
        return this.zAnimation;
    }

    public LoopRotarySwitchView setOrientation(int mOrientation2) {
        boolean z = true;
        if (mOrientation2 != 1) {
            z = false;
        }
        setHorizontal(z, false);
        return this;
    }

    public LoopRotarySwitchView setHorizontal(boolean horizontal2, boolean anim) {
        if (!anim) {
            if (horizontal2) {
                setLoopRotationZ(0);
            } else {
                setLoopRotationZ(90);
            }
            initView();
        } else if (horizontal2) {
            createZAnimation(getLoopRotationZ(), 0, true);
        } else {
            createZAnimation(getLoopRotationZ(), 90, true);
        }
        return this;
    }

    public LoopRotarySwitchView setLoopRotationX(int loopRotationX2) {
        this.loopRotationX = loopRotationX2;
        return this;
    }

    public LoopRotarySwitchView setLoopRotationZ(int loopRotationZ2) {
        this.loopRotationZ = loopRotationZ2;
        return this;
    }

    public int getLoopRotationX() {
        return this.loopRotationX;
    }

    public int getLoopRotationZ() {
        return this.loopRotationZ;
    }

    public ValueAnimator getRestAnimator() {
        return this.restAnimator;
    }

    public ValueAnimator getrAnimation() {
        return this.rAnimation;
    }

    public void setzAnimation(ValueAnimator zAnimation2) {
        this.zAnimation = zAnimation2;
    }

    public ValueAnimator getzAnimation() {
        return this.zAnimation;
    }

    public void setxAnimation(ValueAnimator xAnimation2) {
        this.xAnimation = xAnimation2;
    }

    public ValueAnimator getxAnimation() {
        return this.xAnimation;
    }
}
