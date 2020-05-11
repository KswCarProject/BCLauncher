package com.touchus.benchilauncher.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.backaudio.android.driver.Mainboard;
import com.touchus.benchilauncher.Launcher;
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.benchilauncher.R;
import com.touchus.benchilauncher.SysConst;
import java.lang.ref.WeakReference;

public class MyCustomDialog extends Dialog {
    public MyCustomDialog(Context context) {
        super(context);
    }

    public MyCustomDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private LauncherApplication app;
        private LinearLayout button_layout;
        private View contentView;
        private Context context;
        /* access modifiers changed from: private */
        public MyCustomDialog dialog;
        private MyCustomHandler mHandler = new MyCustomHandler(this);
        private String message;
        private String messageNext;
        /* access modifiers changed from: private */
        public Button negativeButton;
        /* access modifiers changed from: private */
        public DialogInterface.OnClickListener negativeButtonClickListener;
        private String negativeButtonText;
        /* access modifiers changed from: private */
        public Button positiveButton;
        /* access modifiers changed from: private */
        public DialogInterface.OnClickListener positiveButtonClickListener;
        private String positiveButtonText;

        public Builder(Context context2) {
            this.context = context2;
            if (context2 instanceof Launcher) {
                this.app = (LauncherApplication) ((Launcher) context2).getApplication();
            }
            if (this.app != null) {
                this.app.registerHandler(this.mHandler);
            }
        }

        static class MyCustomHandler extends Handler {
            private WeakReference<Builder> target;

            public MyCustomHandler(Builder instance) {
                this.target = new WeakReference<>(instance);
            }

            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (this.target.get() != null && msg.what == 6001) {
                    ((Builder) this.target.get()).handlerMsg(msg);
                }
            }
        }

        /* access modifiers changed from: private */
        public void handlerMsg(Message msg) {
            Bundle bundle = msg.getData();
            if (bundle.getByte(SysConst.IDRIVER_ENUM) == Mainboard.EIdriverEnum.TURN_RIGHT.getCode() || bundle.getByte(SysConst.IDRIVER_ENUM) == Mainboard.EIdriverEnum.RIGHT.getCode()) {
                right();
            } else if (bundle.getByte(SysConst.IDRIVER_ENUM) == Mainboard.EIdriverEnum.TURN_LEFT.getCode() || bundle.getByte(SysConst.IDRIVER_ENUM) == Mainboard.EIdriverEnum.LEFT.getCode()) {
                left();
            } else if (bundle.getByte(SysConst.IDRIVER_ENUM) == Mainboard.EIdriverEnum.PRESS.getCode()) {
                press();
            }
        }

        private void left() {
            if (this.negativeButton.isSelected()) {
                this.negativeButton.setSelected(false);
                this.positiveButton.setSelected(true);
            }
        }

        private void right() {
            if (this.positiveButton.isSelected()) {
                this.negativeButton.setSelected(true);
                this.positiveButton.setSelected(false);
            }
        }

        private void press() {
            if (this.positiveButton.isSelected()) {
                if (this.positiveButtonClickListener != null) {
                    this.positiveButton.performClick();
                }
            } else if (this.negativeButton.isSelected()) {
                if (this.negativeButtonClickListener != null) {
                    this.negativeButton.performClick();
                }
            } else if (this.dialog != null) {
                this.dialog.dismiss();
            }
            unRegisterHandler();
        }

        public void unRegisterHandler() {
            if (this.app != null) {
                this.app.unregisterHandler(this.mHandler);
            }
        }

        public Builder setMessage(String message2) {
            this.message = message2;
            return this;
        }

        public Builder setMessage(int message2) {
            this.message = (String) this.context.getText(message2);
            return this;
        }

        public Builder setNextMessage(String messageNext2) {
            this.messageNext = messageNext2;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public Builder setPositiveButton(int positiveButtonText2, DialogInterface.OnClickListener listener) {
            this.positiveButtonText = (String) this.context.getText(positiveButtonText2);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText2, DialogInterface.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText2;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText2, DialogInterface.OnClickListener listener) {
            this.negativeButtonText = (String) this.context.getText(negativeButtonText2);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText2, DialogInterface.OnClickListener listener) {
            this.negativeButtonText = negativeButtonText2;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public MyCustomDialog create() {
            this.dialog = new MyCustomDialog(this.context, R.style.Dialog);
            View layout = ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(R.layout.dialog_normal_layout, (ViewGroup) null);
            this.dialog.addContentView(layout, new ViewGroup.LayoutParams(-1, -2));
            this.positiveButton = (Button) layout.findViewById(R.id.positiveButton);
            this.negativeButton = (Button) layout.findViewById(R.id.negativeButton);
            this.button_layout = (LinearLayout) layout.findViewById(R.id.button_layout);
            this.positiveButton.setSelected(true);
            if (this.positiveButtonText != null) {
                this.button_layout.setVisibility(0);
                this.positiveButton.setText(this.positiveButtonText);
                if (this.positiveButtonClickListener != null) {
                    this.positiveButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Builder.this.negativeButton.setSelected(false);
                            Builder.this.positiveButton.setSelected(true);
                            Builder.this.positiveButtonClickListener.onClick(Builder.this.dialog, -1);
                            Builder.this.unRegisterHandler();
                        }
                    });
                }
            } else {
                this.button_layout.setVisibility(8);
                this.positiveButton.setVisibility(8);
            }
            if (this.negativeButtonText != null) {
                this.negativeButton.setText(this.negativeButtonText);
                if (this.negativeButtonClickListener != null) {
                    this.negativeButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Builder.this.negativeButton.setSelected(true);
                            Builder.this.positiveButton.setSelected(false);
                            Builder.this.negativeButtonClickListener.onClick(Builder.this.dialog, -2);
                            Builder.this.unRegisterHandler();
                        }
                    });
                }
            } else {
                this.negativeButton.setVisibility(8);
            }
            if (this.message != null) {
                if (TextUtils.isEmpty(this.message)) {
                    ((TextView) layout.findViewById(R.id.message)).setVisibility(8);
                }
                ((TextView) layout.findViewById(R.id.message)).setText(this.message);
                if (this.messageNext != null) {
                    ((TextView) layout.findViewById(R.id.message_next)).setText(this.messageNext);
                }
            } else if (this.contentView != null) {
                ((LinearLayout) layout.findViewById(R.id.content)).removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.content)).addView(this.contentView, new ViewGroup.LayoutParams(-1, -1));
            }
            this.dialog.setContentView(layout);
            return this.dialog;
        }
    }
}
