package com.backaudio.android.driver.bluetooth;

import com.backaudio.android.driver.bluetooth.bc8mpprotocol.AnswerPhoneResult;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.CallOutResult;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.CallingOutProtocol;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.ConnectedDeviceProtocol;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.DeviceNameProtocol;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.DeviceRemovedProtocol;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.DeviceSwitchedProtocol;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.EnterPairingModeResult;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.HangUpPhoneResult;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.IncomingCallProtocol;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.MediaInfoProtocol;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.MediaPlayStatusProtocol;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.MediaStatusProtocol;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.PairingListProtocol;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.PhoneBookCtrlStatusProtocol;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.PhoneBookListProtocol;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.PhoneStatusProtocol;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.SetPlayStatusProtocol;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.VersionProtocol;

public interface IBluetoothEventHandler {
    void onAnswerPhone(AnswerPhoneResult answerPhoneResult);

    void onCallOut(CallOutResult callOutResult);

    void onConnectedDevice(ConnectedDeviceProtocol connectedDeviceProtocol);

    void onDeviceName(DeviceNameProtocol deviceNameProtocol);

    void onDeviceRemoved(DeviceRemovedProtocol deviceRemovedProtocol);

    void onFinishDownloadPhoneBook();

    void onHangUpPhone(HangUpPhoneResult hangUpPhoneResult);

    void onIncomingCall(IncomingCallProtocol incomingCallProtocol);

    void onMediaInfo(MediaInfoProtocol mediaInfoProtocol);

    void onMediaPlayStatus(MediaPlayStatusProtocol mediaPlayStatusProtocol);

    void onMediaStatus(MediaStatusProtocol mediaStatusProtocol);

    void onPairedDevice(String str, String str2);

    void onPairingList(PairingListProtocol pairingListProtocol);

    void onPairingModeEnd();

    void onPairingModeResult(EnterPairingModeResult enterPairingModeResult);

    void onPhoneBook(String str, String str2);

    void onPhoneBookCtrlStatus(PhoneBookCtrlStatusProtocol phoneBookCtrlStatusProtocol);

    void onPhoneBookList(PhoneBookListProtocol phoneBookListProtocol);

    void onPhoneCallingOut(CallingOutProtocol callingOutProtocol);

    void onPhoneStatus(PhoneStatusProtocol phoneStatusProtocol);

    void onSetPlayStatus(SetPlayStatusProtocol setPlayStatusProtocol);

    void onVersion(VersionProtocol versionProtocol);

    void ondeviceSwitchedProtocol(DeviceSwitchedProtocol deviceSwitchedProtocol);
}
