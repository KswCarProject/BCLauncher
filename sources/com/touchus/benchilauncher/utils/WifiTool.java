package com.touchus.benchilauncher.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v4.view.MotionEventCompat;
import android.text.TextUtils;
import android.util.Log;
import com.touchus.benchilauncher.R;
import com.touchus.benchilauncher.bean.BeanWifi;
import java.util.ArrayList;
import java.util.List;

public class WifiTool {
    public static ArrayList<BeanWifi> getAllCanConectWifi(Context context) {
        WifiManager mWifiManager = (WifiManager) context.getSystemService("wifi");
        WifiInfo info = mWifiManager.getConnectionInfo();
        boolean wifiConnected = ((ConnectivityManager) context.getSystemService("connectivity")).getNetworkInfo(1).isConnected();
        mWifiManager.startScan();
        List<ScanResult> temp = mWifiManager.getScanResults();
        ArrayList<BeanWifi> list = new ArrayList<>();
        for (int i = 0; i < temp.size(); i++) {
            ScanResult result = temp.get(i);
            BeanWifi wifi = new BeanWifi();
            wifi.setName(result.SSID);
            boolean iHadPwd = iHadPwd(result.capabilities);
            wifi.setCapabilities(result.capabilities);
            wifi.setLevel(getSignLevel(result.level, iHadPwd));
            wifi.setPwd(iHadPwd);
            wifi.setState(getConnectState(info.getSSID(), result.SSID, wifiConnected));
            if (wifi.getState() == 1) {
                list.add(0, wifi);
            } else {
                list.add(wifi);
            }
        }
        for (int j = 0; j < list.size(); j++) {
            BeanWifi wifi2 = list.get(j);
            int k = j + 1;
            while (k < list.size() - 1) {
                BeanWifi tempWifi = list.get(k);
                if (tempWifi.getName().equals(wifi2.getName())) {
                    if (tempWifi.getLevel() > wifi2.getLevel()) {
                        wifi2 = tempWifi;
                        list.remove(j);
                        list.add(j, tempWifi);
                    }
                    list.remove(k);
                    k--;
                }
                k++;
            }
        }
        int lenght = list.size();
        if (lenght < 5) {
            for (int j2 = 5; j2 > lenght; j2--) {
                list.add(new BeanWifi());
            }
        }
        return list;
    }

    public static boolean connectToSpecifyWifi(Context context, WifiConfiguration config) {
        WifiManager mWifiManager = (WifiManager) context.getSystemService("wifi");
        boolean flag = mWifiManager.enableNetwork(mWifiManager.addNetwork(config), true);
        if (flag) {
            mWifiManager.saveConfiguration();
        }
        return flag;
    }

    public static void connectToHadSaveConfigWifi(Context context, int wifiId) {
        ((WifiManager) context.getSystemService("wifi")).enableNetwork(wifiId, true);
    }

    public static boolean iWifiState(Context context) {
        int temp = ((WifiManager) context.getSystemService("wifi")).getWifiState();
        if (temp == 1 || temp == 0 || temp == 4) {
            return false;
        }
        return true;
    }

    public static void setWifiState(Context context, boolean flag) {
        WifiManager mWiFiManager = (WifiManager) context.getSystemService("wifi");
        if (flag && iWifiApIsOpen(context)) {
            closeWifiAp(context);
        }
        mWiFiManager.setWifiEnabled(flag);
    }

    public static void disConnectWifi(Context context) {
        try {
            WifiManager mWifiManager = (WifiManager) context.getSystemService("wifi");
            mWifiManager.disableNetwork(mWifiManager.getConnectionInfo().getNetworkId());
        } catch (Exception e) {
        }
    }

    public static void removeWifiConfig(Context context, int networkId) {
        try {
            WifiManager mWifiManager = (WifiManager) context.getSystemService("wifi");
            mWifiManager.removeNetwork(networkId);
            mWifiManager.saveConfiguration();
        } catch (Exception e) {
        }
    }

    public static WifiConfiguration CreateWifiInfo(Context context, String ssid, String Password, String Type) {
        WifiManager mWifiManager = (WifiManager) context.getSystemService("wifi");
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + ssid + "\"";
        WifiConfiguration tempConfig = IsExsits(context, ssid);
        if (tempConfig != null) {
            mWifiManager.removeNetwork(tempConfig.networkId);
        }
        if (Type.equals("1")) {
            config.wepKeys[0] = "\"\"";
            config.allowedKeyManagement.set(0);
            config.wepTxKeyIndex = 0;
        }
        if (Type.equals("2")) {
            config.hiddenSSID = true;
            config.wepKeys[0] = "\"" + Password + "\"";
            config.allowedAuthAlgorithms.set(1);
            config.allowedGroupCiphers.set(3);
            config.allowedGroupCiphers.set(2);
            config.allowedGroupCiphers.set(0);
            config.allowedGroupCiphers.set(1);
            config.allowedKeyManagement.set(0);
            config.wepTxKeyIndex = 0;
        }
        if (Type.equals("3")) {
            config.preSharedKey = "\"" + Password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(0);
            config.allowedGroupCiphers.set(2);
            config.allowedKeyManagement.set(1);
            config.allowedPairwiseCiphers.set(1);
            config.allowedGroupCiphers.set(3);
            config.allowedPairwiseCiphers.set(2);
            config.status = 2;
        }
        return config;
    }

    public static String getCurrentWifiPwdType(String keyValue) {
        if (TextUtils.isEmpty(keyValue)) {
            return "3";
        }
        if (keyValue.equals("[ESS]") || keyValue.equals("[WPS][ESS]")) {
            return "1";
        }
        if (keyValue.equals("[WEP][ESS]")) {
            return "2";
        }
        return "3";
    }

    public static boolean isIp(Context context) {
        int ipAddress = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo().getIpAddress();
        if (ipAddress == 0 || intToIp(ipAddress).equals("0.0.0.0")) {
            return false;
        }
        return true;
    }

    public static WifiConfiguration getHadSaveWifiConfig(Context context, String ssid) {
        return IsExsits(context, ssid);
    }

    private static boolean iHadPwd(String pwdStr) {
        return pwdStr != null && !pwdStr.equals("[ESS]") && pwdStr.length() > 10;
    }

    private static int getConnectState(String ssid, String resultSsid, boolean wifiConnected) {
        Log.d("launcherlog", "ssid " + ssid + " ; resultSsid " + resultSsid);
        if (ssid == null || !ssid.equals("\"" + resultSsid + "\"")) {
            return 3;
        }
        if (wifiConnected) {
            return 1;
        }
        return 2;
    }

    private static int getSignLevel(int level, boolean iHadPwd) {
        int temp;
        int base = iHadPwd ? 4 : 0;
        if (level <= 0 && level >= -50) {
            temp = 4;
        } else if (level < -50 && level >= -70) {
            temp = 3;
        } else if (level >= -70 || level < -80) {
            temp = 1;
        } else {
            temp = 2;
        }
        return base + temp;
    }

    private static WifiConfiguration IsExsits(Context context, String SSID) {
        for (WifiConfiguration existingConfig : ((WifiManager) context.getSystemService("wifi")).getConfiguredNetworks()) {
            if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                return existingConfig;
            }
        }
        return null;
    }

    private static String intToIp(int i) {
        return String.valueOf(i & MotionEventCompat.ACTION_MASK) + "." + ((i >> 8) & MotionEventCompat.ACTION_MASK) + "." + ((i >> 16) & MotionEventCompat.ACTION_MASK) + "." + ((i >> 24) & MotionEventCompat.ACTION_MASK);
    }

    public static boolean iWifiApIsOpen(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
        return wifiManager.getWifiApState() == 13 || wifiManager.getWifiApState() == 12;
    }

    public static WifiConfiguration getCurrentWifiApConfig(Context context) {
        return ((WifiManager) context.getSystemService("wifi")).getWifiApConfiguration();
    }

    public static void closeWifiAp(Context context) {
        ((WifiManager) context.getSystemService("wifi")).setWifiApEnabled((WifiConfiguration) null, false);
    }

    public static void openWifiAp(Context context, WifiConfiguration apConfig) {
        WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
        wifiManager.setWifiEnabled(false);
        wifiManager.setWifiApEnabled(apConfig, true);
    }

    public static void updateWifiAp(Context context, String ssid, String key) {
        WifiConfiguration apConfig = getCurrentWifiApConfig(context);
        apConfig.allowedAuthAlgorithms.clear();
        apConfig.allowedGroupCiphers.clear();
        apConfig.allowedKeyManagement.clear();
        apConfig.allowedPairwiseCiphers.clear();
        apConfig.allowedProtocols.clear();
        apConfig.SSID = ssid;
        if (!TextUtils.isEmpty(key)) {
            apConfig.allowedKeyManagement.set(4);
            apConfig.allowedAuthAlgorithms.set(0);
            apConfig.preSharedKey = key;
        } else {
            apConfig.wepKeys[0] = "\"\"";
            apConfig.allowedKeyManagement.set(0);
            apConfig.wepTxKeyIndex = 0;
        }
        ((WifiManager) context.getSystemService("wifi")).setWifiApConfiguration(apConfig);
    }

    public static void setDefaultWifiAp(Context context) {
        updateWifiAp(context, context.getString(R.string.wifi_default_name), "11111111");
    }
}
