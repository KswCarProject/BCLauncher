package com.touchus.benchilauncher.activity.main.right.call;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.ViewDragHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.backaudio.android.driver.Mainboard;
import com.backaudio.android.driver.bluetooth.EVirtualButton;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.EPhoneStatus;
import com.sun.mail.iap.Response;
import com.touchus.benchilauncher.Launcher;
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.benchilauncher.R;
import com.touchus.benchilauncher.SysConst;
import com.touchus.benchilauncher.base.BaseFragment;
import com.touchus.benchilauncher.service.BluetoothService;
import com.touchus.benchilauncher.utils.ToastTool;
import com.touchus.benchilauncher.views.CallPhoneKeyDialog;
import com.touchus.benchilauncher.views.FloatSystemCallDialog;
import com.touchus.benchilauncher.views.MenuSlide;
import com.touchus.benchilauncher.views.MyCustomDialog;
import com.touchus.publicutils.utils.UtilTools;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Marker;

public class CallPhoneFragment extends BaseFragment implements View.OnClickListener {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$backaudio$android$driver$bluetooth$bc8mpprotocol$EPhoneStatus = null;
    public static final int DOWN_AREA = 2;
    public static final int UP_AREA = 1;
    public static int mSelectArea = 1;
    public static int mSelectedIndexInDown = 0;
    private String TAG = getClass().getSimpleName();
    /* access modifiers changed from: private */
    public LauncherApplication app;
    /* access modifiers changed from: private */
    public Button bt_callphone_xia_bluetooth;
    /* access modifiers changed from: private */
    public Button bt_callphone_xia_connect;
    private Button bt_callphone_xia_record;
    private CallPhoneKeyDialog callPhoneKeyDialog;
    private View fragmentView;
    boolean isAlreadyOut = false;
    private boolean isLogToggle = true;
    private boolean isMargin = false;
    boolean isSendNum = false;
    private ImageButton iv_number0;
    private ImageButton iv_number1;
    private ImageButton iv_number2;
    private ImageButton iv_number3;
    private ImageButton iv_number4;
    private ImageButton iv_number5;
    private ImageButton iv_number6;
    private ImageButton iv_number7;
    private ImageButton iv_number8;
    private ImageButton iv_number9;
    private ImageButton iv_number_clear;
    private ImageButton iv_number_end;
    private ImageButton iv_number_jia;
    private ImageButton iv_number_jin;
    private ImageButton iv_number_send;
    private ImageButton iv_number_xing;
    private LinearLayout ll_callphone_number;
    private Launcher mContext;
    private List<Button> mDownBtns = new ArrayList();
    private BluetoothHandler mHandler = new BluetoothHandler(this);
    private int mSelectedIndexInUp = 0;
    private List<ImageButton> mUpBtns = new ArrayList();
    /* access modifiers changed from: private */
    public MyCustomDialog myCustomDialog;
    private ImageView soundswitch;
    private TextView tv_call_connect_state;
    private TextView tv_callphone_bluetooth_state;
    private ImageView tv_callphone_bluetooth_stateimg;
    private TextView tv_callphone_number_details;

    static /* synthetic */ int[] $SWITCH_TABLE$com$backaudio$android$driver$bluetooth$bc8mpprotocol$EPhoneStatus() {
        int[] iArr = $SWITCH_TABLE$com$backaudio$android$driver$bluetooth$bc8mpprotocol$EPhoneStatus;
        if (iArr == null) {
            iArr = new int[EPhoneStatus.values().length];
            try {
                iArr[EPhoneStatus.CALLING_OUT.ordinal()] = 5;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[EPhoneStatus.CONNECTED.ordinal()] = 3;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[EPhoneStatus.CONNECTING.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[EPhoneStatus.INCOMING_CALL.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[EPhoneStatus.INITIALIZING.ordinal()] = 8;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[EPhoneStatus.MULTI_TALKING.ordinal()] = 7;
            } catch (NoSuchFieldError e6) {
            }
            try {
                iArr[EPhoneStatus.TALKING.ordinal()] = 6;
            } catch (NoSuchFieldError e7) {
            }
            try {
                iArr[EPhoneStatus.UNCONNECT.ordinal()] = 1;
            } catch (NoSuchFieldError e8) {
            }
            $SWITCH_TABLE$com$backaudio$android$driver$bluetooth$bc8mpprotocol$EPhoneStatus = iArr;
        }
        return iArr;
    }

    public boolean onBack() {
        if (this.myCustomDialog == null || !this.myCustomDialog.isShowing()) {
            return false;
        }
        this.myCustomDialog.dismiss();
        return true;
    }

    static class BluetoothHandler extends Handler {
        private WeakReference<CallPhoneFragment> target;

        public BluetoothHandler(CallPhoneFragment instance) {
            this.target = new WeakReference<>(instance);
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (this.target.get() != null) {
                ((CallPhoneFragment) this.target.get()).handlerMsg(msg);
            }
        }
    }

    public void initDataFromLauncher() {
        if (getActivity() == null) {
        }
    }

    /* access modifiers changed from: private */
    public void handlerMsg(Message msg) {
        Bundle bundle = msg.getData();
        switch (msg.what) {
            case BluetoothService.DEVICE_SWITCH /*1410*/:
                if (this.app.btservice.iSoundInPhone) {
                    this.soundswitch.setImageResource(R.drawable.bt_selector_phonetolocal);
                    return;
                } else {
                    this.soundswitch.setImageResource(R.drawable.bt_selector_localtophone);
                    return;
                }
            case BluetoothService.CALL_TALKING /*1411*/:
                phoneConnectingBtnState();
                return;
            case BluetoothService.UPDATE_PHONENUM /*1415*/:
                if (BluetoothService.bluetoothStatus == EPhoneStatus.CALLING_OUT) {
                    handlerPhoneNowCallout();
                    return;
                } else if (BluetoothService.bluetoothStatus == EPhoneStatus.INCOMING_CALL) {
                    handlerPhoneNowCallin();
                    return;
                } else {
                    return;
                }
            case BluetoothService.HANGUP_PHONE /*1416*/:
                if (this.app.isPhoneNumFromRecord) {
                    this.app.isPhoneNumFromRecord = false;
                }
                handlerHangup();
                return;
            case SysConst.IDRVIER_STATE:
                handlerIdriver(bundle);
                return;
            case SysConst.BLUETOOTH_PHONE_STATE:
                handlerCallConnState();
                return;
            default:
                return;
        }
    }

    private void handlerIdriver(Bundle bundle) {
        byte mIDRIVERENUM = bundle.getByte(SysConst.IDRIVER_ENUM);
        if (mIDRIVERENUM == Mainboard.EIdriverEnum.TURN_RIGHT.getCode()) {
            operateTurnRight();
        } else if (mIDRIVERENUM == Mainboard.EIdriverEnum.TURN_LEFT.getCode()) {
            operateTurnLeft();
        } else if (mIDRIVERENUM == Mainboard.EIdriverEnum.RIGHT.getCode()) {
            operateRight();
        } else if (mIDRIVERENUM == Mainboard.EIdriverEnum.LEFT.getCode()) {
            operateLeft();
        } else if (mIDRIVERENUM == Mainboard.EIdriverEnum.PRESS.getCode()) {
            operatePress();
        } else if (mIDRIVERENUM == Mainboard.EIdriverEnum.UP.getCode()) {
            operateUp();
        } else if (mIDRIVERENUM == Mainboard.EIdriverEnum.DOWN.getCode()) {
            operateDown();
        } else if (mIDRIVERENUM == Mainboard.EIdriverEnum.PICK_UP.getCode()) {
            if (BluetoothService.bluetoothStatus == EPhoneStatus.INCOMING_CALL) {
                upListenSelectBtn(0);
            } else if (BluetoothService.bluetoothStatus == EPhoneStatus.CONNECTED) {
                upListenSelectBtn(0);
            }
        } else if (mIDRIVERENUM != Mainboard.EIdriverEnum.HANG_UP.getCode()) {
        } else {
            if (BluetoothService.bluetoothStatus == EPhoneStatus.TALKING || BluetoothService.bluetoothStatus == EPhoneStatus.CALLING_OUT || BluetoothService.bluetoothStatus == EPhoneStatus.INCOMING_CALL) {
                upListenSelectBtn(15);
            } else if (BluetoothService.bluetoothStatus == EPhoneStatus.CONNECTED) {
                clickUpBtn(15);
            }
        }
    }

    public void operatePress() {
        if (mSelectArea == 1) {
            clickUpBtn(this.mSelectedIndexInUp);
        } else if (mSelectArea == 2) {
            clickDownBtn(mSelectedIndexInDown);
        }
    }

    private void clickDownBtn(int index) {
        if (!UtilTools.isFastDoubleClick()) {
            switch (index) {
                case 0:
                    if (this.myCustomDialog == null) {
                        if (LauncherApplication.isBlueConnectState) {
                            this.app.btservice.disconnectCurDevice();
                        }
                        showBluetoothDialog(this.mContext.getString(R.string.string_callphone_shouquan), this.app.btservice.getDeviceName(), (String) null, (String) null, true);
                        return;
                    }
                    return;
                case 1:
                    this.mContext.changeRightTo(new CallRecordFragment());
                    return;
                case 2:
                    this.mContext.changeRightTo(new CallAddressBookFragment());
                    return;
                default:
                    return;
            }
        }
    }

    private void upClick2AddChar(String str) {
        LauncherApplication launcherApplication = this.app;
        launcherApplication.phoneNumber = String.valueOf(launcherApplication.phoneNumber) + str;
        if (BluetoothService.bluetoothStatus == EPhoneStatus.TALKING) {
            phoneConnectingBtnState();
        } else {
            btConnetAndHavePhoneNumBtnState();
        }
        this.tv_callphone_number_details.setText(this.app.phoneNumber);
    }

    public void clickUpBtn(int index) {
        Log.e("bluetooth", new StringBuilder(String.valueOf(index)).toString());
        if (this.mUpBtns.get(index).isEnabled()) {
            switch (index) {
                case 0:
                    if (this.app.phoneNumber.length() < 1) {
                        ToastTool.showBigShortToast((Context) this.mContext, (int) R.string.string_callphone_phoneNum_null);
                        return;
                    } else if (BluetoothService.bluetoothStatus == EPhoneStatus.INCOMING_CALL) {
                        this.app.btservice.answerCalling();
                        return;
                    } else {
                        this.app.btservice.CallOut(this.app.phoneNumber);
                        afterSendBeforeConnectBtnState();
                        return;
                    }
                case 1:
                    upClick2AddChar("0");
                    this.app.btservice.pressVirutalButton(EVirtualButton.ZERO);
                    return;
                case 2:
                    upClick2AddChar("1");
                    this.app.btservice.pressVirutalButton(EVirtualButton.ONE);
                    return;
                case 3:
                    upClick2AddChar("2");
                    this.app.btservice.pressVirutalButton(EVirtualButton.TWO);
                    return;
                case 4:
                    upClick2AddChar("3");
                    this.app.btservice.pressVirutalButton(EVirtualButton.THREE);
                    return;
                case 5:
                    upClick2AddChar("4");
                    this.app.btservice.pressVirutalButton(EVirtualButton.FOUR);
                    return;
                case 6:
                    upClick2AddChar("5");
                    this.app.btservice.pressVirutalButton(EVirtualButton.FIVE);
                    return;
                case 7:
                    upClick2AddChar("6");
                    this.app.btservice.pressVirutalButton(EVirtualButton.SIX);
                    btConnetAndHavePhoneNumBtnState();
                    return;
                case 8:
                    upClick2AddChar("7");
                    this.app.btservice.pressVirutalButton(EVirtualButton.SEVEN);
                    return;
                case 9:
                    upClick2AddChar("8");
                    this.app.btservice.pressVirutalButton(EVirtualButton.EIGHT);
                    return;
                case 10:
                    upClick2AddChar("9");
                    this.app.btservice.pressVirutalButton(EVirtualButton.NINE);
                    return;
                case MenuSlide.STATE_UP222 /*11*/:
                    upClick2AddChar(Marker.ANY_MARKER);
                    this.app.btservice.pressVirutalButton(EVirtualButton.ASTERISK);
                    return;
                case Response.BAD:
                    upClick2AddChar("#");
                    this.app.btservice.pressVirutalButton(EVirtualButton.WELL);
                    return;
                case 13:
                    upClick2AddChar(Marker.ANY_NON_NULL_MARKER);
                    return;
                case 14:
                    this.app.phoneNumber = clearNumber();
                    if (this.app.phoneNumber.length() == 0) {
                        btConnetAndNoPhoneNumBtnState();
                    } else {
                        btConnetAndHavePhoneNumBtnState();
                    }
                    this.tv_callphone_number_details.setText(this.app.phoneNumber);
                    return;
                case ViewDragHelper.EDGE_ALL:
                    this.app.phoneNumber = "";
                    this.tv_callphone_number_details.setText(this.app.phoneNumber);
                    btConnetAndNoPhoneNumBtnState();
                    if (this.app.isPhoneNumFromRecord) {
                        this.app.isPhoneNumFromRecord = false;
                    }
                    this.app.btservice.cutdownCurrentCalling();
                    return;
                default:
                    return;
            }
        }
    }

    public void operateUp() {
        if (mSelectArea != 1 && upHaveBtnCanSelect(this.mSelectedIndexInUp)) {
            mSelectArea = 1;
            upSelectBtn(this.mSelectedIndexInUp);
        }
    }

    public void operateDown() {
        if (mSelectArea != 2 && downHaveBtnCanSelect(mSelectedIndexInDown)) {
            mSelectArea = 2;
            downSelectBtn(mSelectedIndexInDown);
        }
    }

    public boolean upHaveBtnCanSelect(int index) {
        for (int i = index; i < this.mUpBtns.size(); i++) {
            if (this.mUpBtns.get(i).isEnabled()) {
                this.mSelectedIndexInUp = i;
                return true;
            }
        }
        for (int i2 = index - 1; i2 > -1; i2--) {
            if (this.mUpBtns.get(i2).isEnabled()) {
                this.mSelectedIndexInUp = i2;
                return true;
            }
        }
        return false;
    }

    public boolean downHaveBtnCanSelect(int index) {
        for (int i = index; i < this.mDownBtns.size(); i++) {
            if (this.mDownBtns.get(i).isEnabled()) {
                mSelectedIndexInDown = i;
                return true;
            }
        }
        for (int i2 = index - 1; i2 > -1; i2--) {
            if (this.mDownBtns.get(i2).isEnabled()) {
                mSelectedIndexInDown = i2;
                return true;
            }
        }
        return false;
    }

    public void operateLeft() {
        if (mSelectArea == 1) {
            leftInUp();
        } else {
            leftInDown();
        }
    }

    public void operateRight() {
        if (mSelectArea == 1) {
            rightInUp();
        } else {
            rightInDown();
        }
    }

    public void operateTurnLeft() {
        operateLeft();
    }

    public void operateTurnRight() {
        operateRight();
    }

    private void leftInDown() {
        int tmpIndex = mSelectedIndexInDown;
        while (tmpIndex > 0) {
            tmpIndex--;
            if (this.mDownBtns.get(tmpIndex).isEnabled()) {
                mSelectedIndexInDown = tmpIndex;
                downSelectBtn(mSelectedIndexInDown);
                return;
            }
        }
    }

    private void leftInUp() {
        int tmpIndex = this.mSelectedIndexInUp;
        while (tmpIndex > 0) {
            tmpIndex--;
            if (this.mUpBtns.get(tmpIndex).isEnabled()) {
                this.mSelectedIndexInUp = tmpIndex;
                upSelectBtn(this.mSelectedIndexInUp);
                return;
            }
        }
    }

    private void rightInDown() {
        int tmpIndex = mSelectedIndexInDown;
        while (tmpIndex < this.mDownBtns.size() - 1) {
            tmpIndex++;
            if (this.mDownBtns.get(tmpIndex).isEnabled()) {
                mSelectedIndexInDown = tmpIndex;
                downSelectBtn(mSelectedIndexInDown);
                return;
            }
        }
    }

    private void rightInUp() {
        int tmpIndex = this.mSelectedIndexInUp;
        while (tmpIndex < this.mUpBtns.size() - 1) {
            tmpIndex++;
            if (this.mUpBtns.get(tmpIndex).isEnabled()) {
                this.mSelectedIndexInUp = tmpIndex;
                upSelectBtn(this.mSelectedIndexInUp);
                return;
            }
        }
    }

    private void upSelectBtn(int index) {
        mSelectArea = 1;
        downAllDisselect();
        upAllDisselect();
        this.mUpBtns.get(index).setSelected(true);
        if (index < 1) {
            this.isMargin = false;
        } else {
            this.isMargin = true;
        }
        setMaringL();
        this.mSelectedIndexInUp = index;
    }

    private void upAllDisselect() {
        for (int i = 0; i < this.mUpBtns.size(); i++) {
            this.mUpBtns.get(i).setSelected(false);
        }
    }

    private void downSelectBtn(int index) {
        mSelectArea = 2;
        upAllDisselect();
        downAllDisselect();
        this.mDownBtns.get(index).setSelected(true);
        this.isMargin = false;
        setMaringL();
        mSelectedIndexInDown = index;
    }

    private void downAllDisselect() {
        for (int i = 0; i < this.mDownBtns.size(); i++) {
            this.mDownBtns.get(i).setSelected(false);
        }
    }

    private void handlerPhoneNowCallout() {
        if (this.app.phoneNumber == null) {
            this.app.phoneNumber = "";
        }
        afterSendBeforeConnectBtnState();
        upSelectBtn(15);
    }

    private void handlerPhoneNowCallin() {
        if (this.app.phoneNumber == null) {
            this.app.phoneNumber = "";
        }
        answerBeforeConnectBtnState();
        upSelectBtn(0);
    }

    private void handlerHangup() {
        this.app.phoneNumber = "";
        this.tv_callphone_number_details.setText(this.app.phoneNumber);
        if (LauncherApplication.isBlueConnectState) {
            btConnetAndNoPhoneNumBtnState();
        } else {
            btUnconnectBtnState();
        }
    }

    private void handlerCallConnState() {
        if (LauncherApplication.isBlueConnectState) {
            btConnetAndNoPhoneNumBtnState();
            if (this.myCustomDialog != null && this.myCustomDialog.isShowing()) {
                this.myCustomDialog.dismiss();
            }
        } else {
            btUnconnectBtnState();
        }
        this.soundswitch.setVisibility(4);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = (Launcher) getActivity();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.app = (LauncherApplication) getActivity().getApplication();
        this.fragmentView = inflater.inflate(R.layout.fragment_call_phone, (ViewGroup) null);
        initView();
        return this.fragmentView;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void onResume() {
        super.onResume();
        this.app.istop = true;
        if (!FloatSystemCallDialog.getInstance().isDestory() && FloatSystemCallDialog.getInstance().getShowStatus() != FloatSystemCallDialog.FloatShowST.HIDEN) {
            FloatSystemCallDialog.getInstance().hiden();
        }
        initEvent();
        this.mUpBtns.add(this.iv_number_send);
        this.mUpBtns.add(this.iv_number0);
        this.mUpBtns.add(this.iv_number1);
        this.mUpBtns.add(this.iv_number2);
        this.mUpBtns.add(this.iv_number3);
        this.mUpBtns.add(this.iv_number4);
        this.mUpBtns.add(this.iv_number5);
        this.mUpBtns.add(this.iv_number6);
        this.mUpBtns.add(this.iv_number7);
        this.mUpBtns.add(this.iv_number8);
        this.mUpBtns.add(this.iv_number9);
        this.mUpBtns.add(this.iv_number_xing);
        this.mUpBtns.add(this.iv_number_jin);
        this.mUpBtns.add(this.iv_number_jia);
        this.mUpBtns.add(this.iv_number_clear);
        this.mUpBtns.add(this.iv_number_end);
        this.mDownBtns.add(this.bt_callphone_xia_connect);
        this.mDownBtns.add(this.bt_callphone_xia_record);
        this.mDownBtns.add(this.bt_callphone_xia_bluetooth);
        initStae();
        this.app.registerHandler(this.mHandler);
    }

    public void onPause() {
        super.onPause();
        this.app.unregisterHandler(this.mHandler);
        this.app.istop = false;
        if (this.app.btservice != null && ((BluetoothService.talkingflag || this.app.btservice.showDialogflag) && !this.app.topIsBlueMainFragment())) {
            this.app.btservice.enterSystemFloatCallView();
        }
        if (this.myCustomDialog != null && this.myCustomDialog.isShowing()) {
            this.myCustomDialog.dismiss();
        }
        this.app.service.removeLoadingFloatView();
    }

    private void initView() {
        this.iv_number0 = (ImageButton) this.fragmentView.findViewById(R.id.bt_selector_num_0);
        this.iv_number1 = (ImageButton) this.fragmentView.findViewById(R.id.bt_selector_num_1);
        this.iv_number2 = (ImageButton) this.fragmentView.findViewById(R.id.bt_selector_num_2);
        this.iv_number3 = (ImageButton) this.fragmentView.findViewById(R.id.bt_selector_num_3);
        this.iv_number4 = (ImageButton) this.fragmentView.findViewById(R.id.bt_selector_num_4);
        this.iv_number5 = (ImageButton) this.fragmentView.findViewById(R.id.bt_selector_num_5);
        this.iv_number6 = (ImageButton) this.fragmentView.findViewById(R.id.bt_selector_num_6);
        this.iv_number7 = (ImageButton) this.fragmentView.findViewById(R.id.bt_selector_num_7);
        this.iv_number8 = (ImageButton) this.fragmentView.findViewById(R.id.bt_selector_num_8);
        this.iv_number9 = (ImageButton) this.fragmentView.findViewById(R.id.bt_selector_num_9);
        this.iv_number_send = (ImageButton) this.fragmentView.findViewById(R.id.bt_selector_num_send);
        this.iv_number_xing = (ImageButton) this.fragmentView.findViewById(R.id.bt_selector_num_xing);
        this.iv_number_jin = (ImageButton) this.fragmentView.findViewById(R.id.bt_selector_num_jin);
        this.iv_number_jia = (ImageButton) this.fragmentView.findViewById(R.id.bt_selector_num_jia);
        this.iv_number_clear = (ImageButton) this.fragmentView.findViewById(R.id.bt_selector_num_clear);
        this.iv_number_end = (ImageButton) this.fragmentView.findViewById(R.id.bt_selector_num_end);
        this.ll_callphone_number = (LinearLayout) this.fragmentView.findViewById(R.id.ll_callphone_number);
        this.bt_callphone_xia_connect = (Button) this.fragmentView.findViewById(R.id.bt_callphone_xia_connect);
        this.bt_callphone_xia_record = (Button) this.fragmentView.findViewById(R.id.bt_callphone_xia_record);
        this.bt_callphone_xia_bluetooth = (Button) this.fragmentView.findViewById(R.id.bt_callphone_xia_bluetooth);
        this.tv_callphone_number_details = (TextView) this.fragmentView.findViewById(R.id.tv_callphone_number_details);
        this.tv_callphone_bluetooth_state = (TextView) this.fragmentView.findViewById(R.id.tv_callphone_bluetooth_state);
        this.tv_callphone_bluetooth_stateimg = (ImageView) this.fragmentView.findViewById(R.id.tv_callphone_bluetooth_stateimg);
        this.soundswitch = (ImageView) this.fragmentView.findViewById(R.id.soundswitch);
        this.tv_call_connect_state = (TextView) this.fragmentView.findViewById(R.id.tv_callphone_connect_state);
    }

    private void initStae() {
        switch ($SWITCH_TABLE$com$backaudio$android$driver$bluetooth$bc8mpprotocol$EPhoneStatus()[BluetoothService.bluetoothStatus.ordinal()]) {
            case 1:
                btUnconnectBtnState();
                return;
            case 3:
                if (this.app.isComeFromRecord) {
                    this.isAlreadyOut = true;
                    this.app.phoneName = this.app.btservice.getbookName(this.app.phoneNumber);
                    afterSendBeforeConnectBtnState();
                    this.app.isComeFromRecord = false;
                    if (this.app.isPhoneNumFromRecord && !TextUtils.isEmpty(this.app.recorPhoneNumber)) {
                        sendPhoneNumFromRecord();
                    }
                } else {
                    this.app.phoneNumber = "";
                    this.tv_callphone_number_details.setText(this.app.phoneNumber);
                    btConnetAndNoPhoneNumBtnState();
                }
                this.app.phoneNumber.equals("");
                return;
            case 4:
                this.tv_callphone_number_details.setText(this.app.phoneNumber);
                handlerPhoneNowCallin();
                return;
            case 5:
                this.tv_callphone_number_details.setText(this.app.phoneNumber);
                afterSendBeforeConnectBtnState();
                return;
            case 6:
                this.tv_callphone_number_details.setText(this.app.phoneNumber);
                phoneConnectingBtnState();
                return;
            default:
                return;
        }
    }

    private void sendPhoneNumFromRecord() {
        Log.e("sendPhone", ":--\tsendPhoneNumFromRecord");
        if (!this.isSendNum) {
            this.isSendNum = true;
            this.tv_callphone_number_details.setText(this.app.recorPhoneNumber);
            this.app.btservice.CallOut(this.app.recorPhoneNumber);
        }
    }

    private void initEvent() {
        this.iv_number_send.setOnClickListener(this);
        this.iv_number0.setOnClickListener(this);
        this.iv_number1.setOnClickListener(this);
        this.iv_number2.setOnClickListener(this);
        this.iv_number3.setOnClickListener(this);
        this.iv_number4.setOnClickListener(this);
        this.iv_number5.setOnClickListener(this);
        this.iv_number6.setOnClickListener(this);
        this.iv_number7.setOnClickListener(this);
        this.iv_number8.setOnClickListener(this);
        this.iv_number9.setOnClickListener(this);
        this.iv_number_xing.setOnClickListener(this);
        this.iv_number_jin.setOnClickListener(this);
        this.iv_number_jia.setOnClickListener(this);
        this.iv_number_clear.setOnClickListener(this);
        this.iv_number_end.setOnClickListener(this);
        this.soundswitch.setOnClickListener(this);
        this.bt_callphone_xia_connect.setOnClickListener(this);
        this.bt_callphone_xia_record.setOnClickListener(this);
        this.bt_callphone_xia_bluetooth.setOnClickListener(this);
    }

    public void onDestroy() {
        super.onDestroy();
    }

    private void setUpBtnUnclickable(int position) {
        this.mUpBtns.get(position).setClickable(false);
        this.mUpBtns.get(position).setAlpha(0.3f);
        this.mUpBtns.get(position).setEnabled(false);
        this.mUpBtns.get(position).setSelected(false);
    }

    private void setUpBtnClickable(int position) {
        this.mUpBtns.get(position).setClickable(true);
        this.mUpBtns.get(position).setAlpha(1.0f);
        this.mUpBtns.get(position).setEnabled(true);
    }

    private void setAllUpBtnClickable() {
        for (int i = 0; i < this.mUpBtns.size(); i++) {
            setUpBtnClickable(i);
        }
    }

    private void setAllUpBtnUnclickable() {
        for (int i = 0; i < this.mUpBtns.size(); i++) {
            setUpBtnUnclickable(i);
        }
    }

    private void setDownBtnUnclickable(int position) {
        this.mDownBtns.get(position).setClickable(false);
        this.mDownBtns.get(position).setAlpha(0.3f);
        this.mDownBtns.get(position).setEnabled(false);
    }

    private void setDownBtnClickable(int position) {
        this.mDownBtns.get(position).setClickable(true);
        this.mDownBtns.get(position).setAlpha(1.0f);
        this.mDownBtns.get(position).setEnabled(true);
    }

    private void setAllDownBtnClickable() {
        for (int i = 0; i < this.mDownBtns.size(); i++) {
            setDownBtnClickable(i);
        }
    }

    private void setAllDownBtnUnclickable() {
        for (int i = 0; i < this.mDownBtns.size(); i++) {
            setDownBtnUnclickable(i);
        }
    }

    private void autoChangeUpSelectBtn() {
        if (mSelectArea == 2 && downHaveBtnCanSelect(mSelectedIndexInDown)) {
            downSelectBtn(mSelectedIndexInDown);
        } else if (upHaveBtnCanSelect(this.mSelectedIndexInUp)) {
            upSelectBtn(this.mSelectedIndexInUp);
        }
    }

    private void btUnconnectBtnState() {
        this.tv_callphone_bluetooth_state.setText(this.mContext.getString(R.string.string_callphone_blue_no));
        this.tv_callphone_bluetooth_stateimg.setImageResource(R.drawable.bt_zhuangtai2);
        this.tv_call_connect_state.setText("");
        this.tv_callphone_number_details.setText("");
        this.bt_callphone_xia_connect.setText(R.string.string_setting_connect);
        this.soundswitch.setVisibility(4);
        this.app.phoneNumber = "";
        setAllUpBtnUnclickable();
        setAllDownBtnClickable();
        autoChangeUpSelectBtn();
    }

    private void btConnetAndNoPhoneNumBtnState() {
        this.tv_callphone_bluetooth_state.setText(String.valueOf(this.mContext.getString(R.string.string_callphone_blue_yes)) + " " + this.app.btservice.currentEquipName);
        this.tv_callphone_bluetooth_stateimg.setImageResource(R.drawable.bt_zhuangtai);
        this.tv_call_connect_state.setText("");
        this.tv_callphone_number_details.setText("");
        this.bt_callphone_xia_connect.setText(R.string.string_setting_disconnect);
        this.soundswitch.setVisibility(4);
        setAllUpBtnClickable();
        setUpBtnUnclickable(0);
        setUpBtnUnclickable(14);
        setUpBtnUnclickable(15);
        setAllDownBtnClickable();
        autoChangeUpSelectBtn();
        if (this.app.isPhoneNumFromRecord) {
            this.app.phoneNumber = this.app.recorPhoneNumber;
            afterSendBeforeConnectBtnState();
            this.tv_callphone_number_details.setText(this.app.phoneNumber);
            if (this.isAlreadyOut) {
                sendPhoneNumFromRecord();
                this.isAlreadyOut = false;
                return;
            }
            return;
        }
        this.app.service.removeLoadingFloatView();
    }

    private void btConnetAndHavePhoneNumBtnState() {
        this.tv_callphone_bluetooth_state.setText(String.valueOf(this.mContext.getString(R.string.string_callphone_blue_yes)) + " " + this.app.btservice.currentEquipName);
        this.tv_callphone_bluetooth_stateimg.setImageResource(R.drawable.bt_zhuangtai);
        this.tv_call_connect_state.setText("");
        this.soundswitch.setVisibility(4);
        setAllUpBtnClickable();
        setAllDownBtnUnclickable();
        autoChangeUpSelectBtn();
    }

    private void afterSendBeforeConnectBtnState() {
        this.tv_callphone_bluetooth_state.setText(String.valueOf(this.mContext.getString(R.string.string_callphone_blue_yes)) + " " + this.app.btservice.currentEquipName);
        this.tv_callphone_bluetooth_stateimg.setImageResource(R.drawable.bt_zhuangtai);
        this.tv_callphone_number_details.setText(getName());
        this.tv_call_connect_state.setText(this.mContext.getString(R.string.string_callphone_now_call_out));
        this.soundswitch.setVisibility(0);
        setAllUpBtnUnclickable();
        setUpBtnClickable(15);
        setAllDownBtnUnclickable();
        autoChangeUpSelectBtn();
    }

    private String getName() {
        return TextUtils.isEmpty(this.app.phoneName) ? this.app.phoneNumber : this.app.phoneName;
    }

    private void answerBeforeConnectBtnState() {
        this.tv_callphone_bluetooth_state.setText(String.valueOf(this.mContext.getString(R.string.string_callphone_blue_yes)) + " " + this.app.btservice.currentEquipName);
        this.tv_callphone_bluetooth_stateimg.setImageResource(R.drawable.bt_zhuangtai);
        this.tv_callphone_number_details.setText(getName());
        this.tv_call_connect_state.setText(this.mContext.getString(R.string.string_callphone_now_call_in));
        this.soundswitch.setVisibility(0);
        setAllUpBtnUnclickable();
        setUpBtnClickable(0);
        setUpBtnClickable(15);
        setAllDownBtnUnclickable();
        autoChangeUpSelectBtn();
    }

    private void phoneConnectingBtnState() {
        this.tv_callphone_bluetooth_state.setText(String.valueOf(this.mContext.getString(R.string.string_callphone_blue_yes)) + " " + this.app.btservice.currentEquipName);
        this.tv_callphone_bluetooth_stateimg.setImageResource(R.drawable.bt_zhuangtai);
        this.tv_callphone_number_details.setText(getName());
        this.tv_call_connect_state.setText(this.mContext.getString(R.string.string_callphone_tonghuazhong));
        this.soundswitch.setVisibility(0);
        setAllUpBtnClickable();
        setUpBtnUnclickable(0);
        setUpBtnUnclickable(14);
        setAllDownBtnUnclickable();
        autoChangeUpSelectBtn();
    }

    private void setMaringL() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.ll_callphone_number.getLayoutParams();
        if (this.isMargin) {
            layoutParams.setMargins(123, 0, 0, 0);
        } else {
            layoutParams.setMargins(170, 0, 0, 0);
        }
        this.ll_callphone_number.setLayoutParams(layoutParams);
    }

    private String clearNumber() {
        if (this.app.phoneNumber.length() > 0) {
            return this.app.phoneNumber.substring(0, this.app.phoneNumber.length() - 1);
        }
        return "";
    }

    private void showBluetoothDialog(String Messsage, String nextMessage, String btnPosiText, String btnNegaText, final boolean flag) {
        final MyCustomDialog.Builder builder = new MyCustomDialog.Builder(this.mContext);
        builder.setMessage(Messsage);
        builder.setNextMessage(nextMessage);
        builder.setPositiveButton(btnPosiText, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (flag) {
                    CallPhoneFragment.this.app.btservice.disconnectCurDevice();
                } else {
                    CallPhoneFragment.this.bluetoothState();
                }
            }
        });
        builder.setNegativeButton(btnNegaText, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (flag) {
                    CallPhoneFragment.this.bt_callphone_xia_connect.setSelected(false);
                } else {
                    CallPhoneFragment.this.bt_callphone_xia_bluetooth.setSelected(false);
                }
            }
        });
        this.myCustomDialog = builder.create();
        if (!LauncherApplication.isBlueConnectState && flag) {
            this.app.btservice.enterPairingMode();
        }
        this.myCustomDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
                builder.unRegisterHandler();
                CallPhoneFragment.this.myCustomDialog = null;
            }
        });
        setDialogLocation(this.myCustomDialog);
        this.myCustomDialog.show();
    }

    private void showNumberKeyDialog() {
        if (this.callPhoneKeyDialog == null) {
            this.callPhoneKeyDialog = new CallPhoneKeyDialog(getActivity(), R.style.dialogStyle1);
        }
        this.callPhoneKeyDialog.parent = this;
        this.callPhoneKeyDialog.show();
    }

    private void dismissNumberKeyDialog() {
        if (this.callPhoneKeyDialog != null && this.callPhoneKeyDialog.isShowing()) {
            this.callPhoneKeyDialog.dismiss();
        }
        this.callPhoneKeyDialog = null;
    }

    private void setDialogLocation(Dialog myCustomDialog2) {
        Window dialogWindow = myCustomDialog2.getWindow();
        WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
        layoutParams.x = 256;
        layoutParams.y = -30;
        dialogWindow.setAttributes(layoutParams);
    }

    public void connectBluetooth() {
        this.app.btservice.enterPairingMode();
    }

    /* access modifiers changed from: private */
    public void bluetoothState() {
    }

    public void upListenSelectBtn(int index) {
        upSelectBtn(index);
        clickUpBtn(index);
    }

    private void downListenSelect(int index) {
        downSelectBtn(index);
        clickDownBtn(index);
    }

    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.soundswitch:
                swithSound();
                return;
            case R.id.bt_selector_num_send:
                upListenSelectBtn(0);
                return;
            case R.id.bt_selector_num_0:
                upListenSelectBtn(1);
                return;
            case R.id.bt_selector_num_1:
                upListenSelectBtn(2);
                return;
            case R.id.bt_selector_num_2:
                upListenSelectBtn(3);
                return;
            case R.id.bt_selector_num_3:
                upListenSelectBtn(4);
                return;
            case R.id.bt_selector_num_4:
                upListenSelectBtn(5);
                return;
            case R.id.bt_selector_num_5:
                upListenSelectBtn(6);
                return;
            case R.id.bt_selector_num_6:
                upListenSelectBtn(7);
                return;
            case R.id.bt_selector_num_7:
                upListenSelectBtn(8);
                return;
            case R.id.bt_selector_num_8:
                upListenSelectBtn(9);
                return;
            case R.id.bt_selector_num_9:
                upListenSelectBtn(10);
                return;
            case R.id.bt_selector_num_xing:
                upListenSelectBtn(11);
                return;
            case R.id.bt_selector_num_jin:
                upListenSelectBtn(12);
                return;
            case R.id.bt_selector_num_jia:
                upListenSelectBtn(13);
                return;
            case R.id.bt_selector_num_clear:
                upListenSelectBtn(14);
                return;
            case R.id.bt_selector_num_end:
                upListenSelectBtn(15);
                return;
            case R.id.bt_callphone_xia_connect:
                downListenSelect(0);
                return;
            case R.id.bt_callphone_xia_record:
                downListenSelect(1);
                return;
            case R.id.bt_callphone_xia_bluetooth:
                downListenSelect(2);
                return;
            default:
                return;
        }
    }

    private void swithSound() {
        this.app.btservice.switchDevice();
        if (this.app.btservice.iSoundInPhone) {
            this.soundswitch.setImageResource(R.drawable.bt_selector_phonetolocal);
        } else {
            this.soundswitch.setImageResource(R.drawable.bt_selector_localtophone);
        }
    }
}
