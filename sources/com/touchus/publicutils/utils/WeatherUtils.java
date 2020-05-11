package com.touchus.publicutils.utils;

import android.content.Context;
import android.text.TextUtils;
import com.touchus.publicutils.R;
import java.util.Calendar;

public class WeatherUtils {
    public static int getResId(Context context, String weather) {
        String weather2;
        if (TextUtils.isEmpty(weather)) {
            weather = "weather_yin";
        }
        Calendar c = Calendar.getInstance();
        boolean isday = c.get(11) > 6 && c.get(11) < 18;
        if (weather.equals("晴")) {
            if (isday) {
                weather2 = "weather_qing_baitian";
            } else {
                weather2 = "weather_qing_yejian";
            }
        } else if (weather.contains("多云")) {
            if (isday) {
                weather2 = "weather_duoyun_baitian";
            } else {
                weather2 = "weather_duoyun_yejian";
            }
        } else if (weather.equals("阴")) {
            weather2 = "weather_yin";
        } else if (weather.equals("雾")) {
            weather2 = "weather_wu";
        } else if (weather.contains("大雨") || weather.contains("暴雨")) {
            weather2 = "weather_dayubaoyu";
        } else if (weather.contains("小雨") || weather.contains("中雨")) {
            weather2 = "weather_xiaoyuzhongyu";
        } else if (weather.contains("雷阵雨")) {
            weather2 = "weather_leizhenyu";
        } else if (weather.contains("阵雨")) {
            if (isday) {
                weather2 = "weather_zhenyu_baitian";
            } else {
                weather2 = "weather_zhenyu_yejian";
            }
        } else if (weather.contains("冻雨") || weather.contains("冰雹")) {
            weather2 = "weather_dongyu";
        } else if (weather.contains("雨夹雪")) {
            weather2 = "weather_yujiaxue";
        } else if (weather.contains("大雪") || weather.contains("暴雪")) {
            weather2 = "weather_daxuebaoxue";
        } else if (weather.contains("小雪") || weather.contains("中雪")) {
            weather2 = "weather_xiaoxuezhongxue";
        } else if (weather.contains("阵雪")) {
            if (isday) {
                weather2 = "weather_zhenxue_baitian";
            } else {
                weather2 = "weather_zhenxue_yejian";
            }
        } else if (weather.contains("浮尘") || weather.contains("扬沙") || weather.contains("霾")) {
            weather2 = "weather_mai";
        } else if (weather.contains("沙尘暴")) {
            weather2 = "weather_shachenbao";
        } else {
            weather2 = "weather_yin";
        }
        try {
            return context.getResources().getIdentifier(weather2, "drawable", context.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
            return R.drawable.weather_yin;
        }
    }
}
