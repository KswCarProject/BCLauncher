package com.touchus.benchilauncher.activity.main.right.call;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.backaudio.android.driver.Mainboard;
import com.touchus.benchilauncher.Launcher;
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.benchilauncher.R;
import com.touchus.benchilauncher.SysConst;
import com.touchus.benchilauncher.adapter.CallRecordAdapter;
import com.touchus.benchilauncher.base.BaseFragment;
import com.touchus.publicutils.bean.Person;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class CallRecordFragment extends BaseFragment {
    private String TAG = getClass().getSimpleName();
    /* access modifiers changed from: private */
    public LauncherApplication app;
    /* access modifiers changed from: private */
    public CallRecordAdapter callRecordAdapter;
    private View callRecordView;
    /* access modifiers changed from: private */
    public List<Person> callRecords = new ArrayList();
    private Context context;
    /* access modifiers changed from: private */
    public int currentPosition = 0;
    private boolean isLogToggle = true;
    private boolean isLongPress = false;
    /* access modifiers changed from: private */
    public Launcher launcher;
    private ListView listView_record;
    private RecordHandler mHandler = new RecordHandler(this);

    static class RecordHandler extends Handler {
        private WeakReference<CallRecordFragment> target;

        public RecordHandler(CallRecordFragment instance) {
            this.target = new WeakReference<>(instance);
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (this.target.get() != null) {
                ((CallRecordFragment) this.target.get()).handlerMsg(msg);
            }
        }
    }

    /* access modifiers changed from: private */
    public void handlerMsg(Message msg) {
        if (6001 == msg.what) {
            handlerMsgIdr(msg);
        }
    }

    private void handlerMsgIdr(Message msg) {
        Bundle bundle = msg.getData();
        if (bundle.getByte(SysConst.IDRIVER_STATE_ENUM) == Mainboard.EBtnStateEnum.BTN_LONG_PRESS.getCode()) {
            this.isLongPress = true;
        } else if (bundle.getByte(SysConst.IDRIVER_STATE_ENUM) == Mainboard.EBtnStateEnum.BTN_DOWN.getCode()) {
            this.isLongPress = false;
            pressUpDown(bundle);
        } else if (bundle.getByte(SysConst.IDRIVER_STATE_ENUM) == Mainboard.EBtnStateEnum.BTN_UP.getCode()) {
            this.isLongPress = false;
        } else {
            this.isLongPress = false;
        }
    }

    public void pressUpDown(Bundle bundle) {
        if (this.callRecords.size() != 0) {
            if (bundle.getByte(SysConst.IDRIVER_ENUM) == Mainboard.EIdriverEnum.UP.getCode() || bundle.getByte(SysConst.IDRIVER_ENUM) == Mainboard.EIdriverEnum.TURN_LEFT.getCode()) {
                this.currentPosition--;
                if (this.currentPosition == -1) {
                    this.currentPosition = 0;
                    this.isLongPress = false;
                }
                if (this.currentPosition < this.listView_record.getFirstVisiblePosition() + 1) {
                    this.listView_record.smoothScrollToPositionFromTop(this.currentPosition - 5, 0);
                }
            } else if (bundle.getByte(SysConst.IDRIVER_ENUM) == Mainboard.EIdriverEnum.DOWN.getCode() || bundle.getByte(SysConst.IDRIVER_ENUM) == Mainboard.EIdriverEnum.TURN_RIGHT.getCode()) {
                this.currentPosition++;
                if (this.currentPosition == this.callRecordAdapter.getCount()) {
                    this.currentPosition = this.callRecordAdapter.getCount() - 1;
                    this.isLongPress = false;
                }
                if (this.currentPosition > this.listView_record.getLastVisiblePosition() - 1) {
                    this.listView_record.smoothScrollToPositionFromTop(this.currentPosition, 0);
                }
            } else if (bundle.getByte(SysConst.IDRIVER_ENUM) != Mainboard.EIdriverEnum.PRESS.getCode()) {
                return;
            } else {
                if (LauncherApplication.isBlueConnectState) {
                    String s = this.callRecords.get(this.currentPosition).getPhone();
                    this.app.isComeFromRecord = true;
                    this.app.isPhoneNumFromRecord = true;
                    this.app.recorPhoneNumber = s;
                    this.launcher.changeRightTo(new CallPhoneFragment());
                }
            }
            this.callRecordAdapter.setSelectItem(this.currentPosition);
            this.callRecordAdapter.notifyDataSetChanged();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = getActivity();
        this.launcher = (Launcher) getActivity();
        this.app = (LauncherApplication) getActivity().getApplication();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.callRecordView = inflater.inflate(R.layout.fragment_call_record, (ViewGroup) null);
        initView();
        initData();
        initEvent();
        return this.callRecordView;
    }

    public void onStart() {
        this.app.registerHandler(this.mHandler);
        super.onStart();
    }

    public void onStop() {
        this.app.unregisterHandler(this.mHandler);
        super.onStop();
    }

    private void initView() {
        this.listView_record = (ListView) this.callRecordView.findViewById(R.id.listView_call_record);
    }

    private void initData() {
        this.callRecordAdapter = new CallRecordAdapter(this.context);
        this.callRecords.addAll(this.app.btservice.getHistoryList());
        this.callRecordAdapter.setData(this.callRecords);
        this.listView_record.setAdapter(this.callRecordAdapter);
        this.listView_record.setEmptyView(this.callRecordView.findViewById(R.id.norecord));
    }

    private void initEvent() {
        this.listView_record.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                CallRecordFragment.this.callRecordAdapter.setSelectItem(position);
                CallRecordFragment.this.currentPosition = position;
                CallRecordFragment.this.callRecordAdapter.notifyDataSetChanged();
                if (LauncherApplication.isBlueConnectState) {
                    CallRecordFragment.this.app.isComeFromRecord = true;
                    CallRecordFragment.this.app.isPhoneNumFromRecord = true;
                    CallRecordFragment.this.app.recorPhoneNumber = ((Person) CallRecordFragment.this.callRecords.get(position)).getPhone();
                    CallRecordFragment.this.launcher.changeRightTo(new CallPhoneFragment());
                }
            }
        });
    }

    public boolean onBack() {
        return false;
    }

    public void onDestroy() {
        super.onDestroy();
    }
}
