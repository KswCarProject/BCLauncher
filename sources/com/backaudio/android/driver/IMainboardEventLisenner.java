package com.backaudio.android.driver;

import com.backaudio.android.driver.Mainboard;
import com.backaudio.android.driver.beans.AirInfo;
import com.backaudio.android.driver.beans.CarBaseInfo;
import com.backaudio.android.driver.beans.CarRunInfo;
import com.touchus.publicutils.sysconst.BenzModel;
import java.util.List;

public interface IMainboardEventLisenner {
    void logcatCanbox(String str);

    void obtainBenzSize(int i);

    void obtainBenzType(BenzModel.EBenzTpye eBenzTpye);

    void obtainBrightness(int i);

    void obtainCoordinate(int i, int i2);

    void obtainDVState(boolean z);

    void obtainLanguageMediaSet(Mainboard.ELanguage eLanguage);

    void obtainReverseMediaSet(Mainboard.EReverserMediaSet eReverserMediaSet);

    void obtainReverseType(Mainboard.EReverseTpye eReverseTpye);

    void obtainStoreData(List<Byte> list);

    void obtainVersionDate(String str);

    void obtainVersionNumber(String str);

    void onAUXActivateStutas(Mainboard.EAUXStutas eAUXStutas);

    void onAirInfo(AirInfo airInfo);

    void onCanboxInfo(String str);

    void onCanboxUpgradeForGetDataByIndex(int i);

    void onCanboxUpgradeState(Mainboard.ECanboxUpgrade eCanboxUpgrade);

    void onCarBaseInfo(CarBaseInfo carBaseInfo);

    void onCarRunningInfo(CarRunInfo carRunInfo);

    void onEnterStandbyMode();

    void onHandleIdriver(Mainboard.EIdriverEnum eIdriverEnum, Mainboard.EBtnStateEnum eBtnStateEnum);

    void onHornSoundValue(int i, int i2, int i3, int i4);

    void onMcuUpgradeForGetDataByIndex(int i);

    void onMcuUpgradeState(Mainboard.EUpgrade eUpgrade);

    void onOriginalCarView(Mainboard.EControlSource eControlSource, boolean z);

    void onShowOrHideCarLayer(Mainboard.ECarLayer eCarLayer);

    void onTime(int i, int i2, int i3, int i4, int i5, int i6, int i7);

    void onWakeUp(Mainboard.ECarLayer eCarLayer);
}
