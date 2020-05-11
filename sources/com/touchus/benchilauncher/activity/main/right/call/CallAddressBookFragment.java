package com.touchus.benchilauncher.activity.main.right.call;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.backaudio.android.driver.Mainboard;
import com.touchus.benchilauncher.Launcher;
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.benchilauncher.R;
import com.touchus.benchilauncher.SysConst;
import com.touchus.benchilauncher.adapter.CallRecordAdapter;
import com.touchus.benchilauncher.base.BaseFragment;
import com.touchus.benchilauncher.service.BluetoothService;
import com.touchus.publicutils.bean.Person;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class CallAddressBookFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
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
    private SwipeRefreshLayout layout;
    private ListView listView_record;
    private RecordHandler mHandler = new RecordHandler(this);
    private TextView noDataTv;

    static class RecordHandler extends Handler {
        private WeakReference<CallAddressBookFragment> target;

        public RecordHandler(CallAddressBookFragment instance) {
            this.target = new WeakReference<>(instance);
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (this.target.get() != null) {
                ((CallAddressBookFragment) this.target.get()).handlerMsg(msg);
            }
        }
    }

    /* access modifiers changed from: private */
    public void handlerMsg(Message msg) {
        switch (msg.what) {
            case BluetoothService.BOOK_LIST_START_LOAD /*1407*/:
                this.app.service.createLoadingFloatView(getString(R.string.bluetooth_telbook_synchronize));
                this.callRecords.clear();
                return;
            case BluetoothService.BOOK_LIST_CANCEL_LOAD /*1408*/:
                this.app.service.removeLoadingFloatView();
                return;
            case BluetoothService.DRIVER_BOOK_LIST /*1409*/:
                this.app.service.removeLoadingFloatView();
                this.callRecords.addAll(this.app.btservice.getBookList());
                if (this.callRecords.size() == 0) {
                    this.noDataTv.setVisibility(0);
                } else {
                    this.noDataTv.setVisibility(8);
                }
                this.callRecordAdapter.setData(this.callRecords);
                this.callRecordAdapter.notifyDataSetChanged();
                this.callRecordAdapter.notifyDataSetInvalidated();
                this.layout.setRefreshing(false);
                return;
            case SysConst.IDRVIER_STATE:
                handlerMsgIdr(msg);
                return;
            default:
                return;
        }
    }

    private void handlerMsgIdr(Message msg) {
        Bundle bundle = msg.getData();
        if (bundle.getByte(SysConst.IDRIVER_STATE_ENUM) == Mainboard.EBtnStateEnum.BTN_LONG_PRESS.getCode()) {
            this.isLongPress = true;
        } else if (bundle.getByte(SysConst.IDRIVER_STATE_ENUM) == Mainboard.EBtnStateEnum.BTN_DOWN.getCode()) {
            this.isLongPress = false;
            if (this.callRecords.size() > 0) {
                pressUpDown(bundle);
            }
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
                    String phonenum = this.callRecords.get(this.currentPosition).getPhone();
                    this.app.isComeFromRecord = true;
                    this.app.isPhoneNumFromRecord = true;
                    this.app.recorPhoneNumber = phonenum;
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
        this.callRecordView = inflater.inflate(R.layout.fragment_call_address, (ViewGroup) null);
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
        this.layout = (SwipeRefreshLayout) this.callRecordView.findViewById(R.id.layout);
        this.noDataTv = (TextView) this.callRecordView.findViewById(R.id.norecord);
        this.noDataTv.setText(getText(R.string.no_call_linkman));
    }

    private void initData() {
        this.callRecordAdapter = new CallRecordAdapter(this.context, 1);
        if (!LauncherApplication.isBlueConnectState || this.app.btservice.getBookList().size() != 0) {
            this.callRecords.addAll(this.app.btservice.getBookList());
        } else {
            this.app.btservice.downloadflag = 0;
            this.app.btservice.lastindex = 0;
            this.app.btservice.downloadType = 0;
            this.app.btservice.tryToDownloadPhoneBook();
        }
        this.callRecordAdapter.setData(this.callRecords);
        this.listView_record.setAdapter(this.callRecordAdapter);
        if (this.callRecords.size() == 0) {
            this.noDataTv.setVisibility(0);
        } else {
            this.noDataTv.setVisibility(8);
        }
    }

    private void initEvent() {
        this.listView_record.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                CallAddressBookFragment.this.callRecordAdapter.setSelectItem(position);
                CallAddressBookFragment.this.currentPosition = position;
                CallAddressBookFragment.this.callRecordAdapter.notifyDataSetChanged();
                if (LauncherApplication.isBlueConnectState) {
                    CallAddressBookFragment.this.app.isComeFromRecord = true;
                    CallAddressBookFragment.this.app.isPhoneNumFromRecord = true;
                    CallAddressBookFragment.this.app.recorPhoneNumber = ((Person) CallAddressBookFragment.this.callRecords.get(position)).getPhone();
                    CallAddressBookFragment.this.launcher.changeRightTo(new CallPhoneFragment());
                }
            }
        });
        this.layout.setOnRefreshListener(this);
    }

    public boolean onBack() {
        return false;
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onRefresh() {
        if (LauncherApplication.isBlueConnectState) {
            this.app.btservice.downloadflag = 0;
            this.app.btservice.lastindex = 0;
            this.app.btservice.downloadType = 0;
            this.app.btservice.tryToDownloadPhoneBook();
        }
    }
}
