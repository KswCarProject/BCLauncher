package com.touchus.benchilauncher;

import android.os.Build;
import java.util.ArrayList;
import java.util.List;

public class ProjectConfig {
    public static String APPSoft;
    public static String CANSoft = "can_app.bin";
    public static String CONFIG_URL;
    public static List<String> FactoryPwd = new ArrayList();
    public static String MCUSoft = "BENZ_MCU.BIN";
    public static String projectName;
    public static String systemSoft;

    static {
        CONFIG_URL = "http://smartpie-update.gz.bcebos.com/c200_en_app/%1$s.sh";
        projectName = "TYPE：XHD_";
        systemSoft = "c200_en-ota-user.smartpie.zip";
        APPSoft = "C200_EN_BCLauncher.apk";
        if (Build.MODEL.equals("c200")) {
            CONFIG_URL = "http://smartpie-update.gz.bcebos.com/c200_app/%1$s.sh";
            projectName = "TYPE：XHD_";
            systemSoft = "c200-ota-user.smartpie.zip";
            APPSoft = "c200_BCLauncher.apk";
            FactoryPwd.add("111111");
        } else if (Build.MODEL.equals("c200_en")) {
            CONFIG_URL = "http://smartpie-update.gz.bcebos.com/c200_en_app/%1$s.sh";
            projectName = "TYPE：XHD_";
            systemSoft = "c200_en-ota-user.smartpie.zip";
            APPSoft = "C200_EN_BCLauncher.apk";
            FactoryPwd.add("111111");
        } else if (Build.MODEL.equals("c200_jly")) {
            CONFIG_URL = "http://smartpie-update.gz.bcebos.com/c200_jly_app/%1$s.sh";
            projectName = "TYPE：JLY_";
            systemSoft = "c200_jly-ota-user.smartpie.zip";
            APPSoft = "JLY_BCLauncher.apk";
            FactoryPwd.add("0207");
        } else if (Build.MODEL.equals("c200_jly_tw")) {
            CONFIG_URL = "http://smartpie-update.gz.bcebos.com/c200_jly_tw_app/%1$s.sh";
            projectName = "TYPE：JLY_";
            systemSoft = "c200_jly_tw-ota-user.smartpie.zip";
            APPSoft = "JLY_TW_BCLauncher.apk";
            FactoryPwd.add("0207");
        } else if (Build.MODEL.equals("c200_psr")) {
            CONFIG_URL = "http://smartpie-update.gz.bcebos.com/c200_psr_app/%1$s.sh";
            projectName = "TYPE：PSR_";
            systemSoft = "c200_psr-ota-user.smartpie.zip";
            APPSoft = "PSR_BCLauncher.apk";
            FactoryPwd.add("8888");
        } else if (Build.MODEL.equals("c200_hy")) {
            CONFIG_URL = "http://smartpie-update.gz.bcebos.com/c200_hy_app/%1$s.sh";
            projectName = "TYPE：HY_";
            systemSoft = "c200_hy-ota-user.smartpie.zip";
            APPSoft = "HY_BCLauncher.apk";
            FactoryPwd.add("123456");
        } else if (Build.MODEL.equals("c200_hy_en")) {
            CONFIG_URL = "http://smartpie-update.gz.bcebos.com/c200_hy_en_app/%1$s.sh";
            projectName = "TYPE：HY_";
            systemSoft = "c200_hy_en-ota-user.smartpie.zip";
            APPSoft = "HY_EN_BCLauncher.apk";
            FactoryPwd.add("123456");
        } else if (Build.MODEL.equals("c200_zlh")) {
            CONFIG_URL = "http://smartpie-update.gz.bcebos.com/c200_zlh_app/%1$s.sh";
            projectName = "TYPE：ZLH_";
            systemSoft = "c200_zlh-ota-user.smartpie.zip";
            APPSoft = "ZLH_BCLauncher.apk";
            FactoryPwd.add("8888");
        } else if (Build.MODEL.equals("c200_zlh_en")) {
            CONFIG_URL = "http://smartpie-update.gz.bcebos.com/c200_zlh_en_app/%1$s.sh";
            projectName = "TYPE：ZLH_";
            systemSoft = "c200_zlh_en-ota-user.smartpie.zip";
            APPSoft = "ZLH_EN_BCLauncher.apk";
            FactoryPwd.add("8888");
        } else if (Build.MODEL.equals("benz")) {
            CONFIG_URL = "http://smartpie-update.gz.bcebos.com/c200_ay_app/%1$s.sh";
            projectName = "TYPE：AY_";
            systemSoft = "benz-ota-user.smartpie.zip";
            APPSoft = "AY_BCLauncher.apk";
            FactoryPwd.add("3368");
        } else if (Build.MODEL.equals("c200_lsx")) {
            CONFIG_URL = "http://smartpie-update.gz.bcebos.com/c200_lsx_app/%1$s.sh";
            projectName = "TYPE：LSX_";
            systemSoft = "c200_lsx-ota-user.smartpie.zip";
            APPSoft = "LSX_BCLauncher.apk";
            FactoryPwd.add("111111");
        } else if (Build.MODEL.equals("benz_hy")) {
            CONFIG_URL = "http://smartpie-update.gz.bcebos.com/benz_hy_app/%1$s.sh";
            projectName = "TYPE：HY_";
            systemSoft = "benz_hy-ota-user.smartpie.zip";
            APPSoft = "BCLauncher_HY.apk";
            FactoryPwd.add("8888");
        } else if (Build.MODEL.equals("c200_ks")) {
            CONFIG_URL = "http://smartpie-update.gz.bcebos.com/c200_ks_app/%1$s.sh";
            projectName = "TYPE：KSUN_";
            systemSoft = "c200_ks-ota-user.smartpie.zip";
            APPSoft = "KS_BCLauncher.apk";
            FactoryPwd.add("8888");
        } else if (Build.MODEL.equals("c200_ks_en")) {
            CONFIG_URL = "http://smartpie-update.gz.bcebos.com/c200_ks_en_app/%1$s.sh";
            projectName = "TYPE：KSUN_";
            systemSoft = "c200_ks_en-ota-user.smartpie.zip";
            APPSoft = "KS_EN_BCLauncher.apk";
            FactoryPwd.add("8888");
        } else {
            CONFIG_URL = "http://smartpie-update.gz.bcebos.com/c200_en_app/%1$s.sh";
            projectName = "TYPE：ZHP_";
            systemSoft = "c200-ota-user.smartpie.zip";
            APPSoft = "c200_BCLauncher.apk";
        }
        FactoryPwd.add("2109");
    }
}
