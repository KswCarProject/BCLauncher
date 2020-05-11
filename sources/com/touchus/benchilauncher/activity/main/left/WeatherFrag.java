package com.touchus.benchilauncher.activity.main.left;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.benchilauncher.R;
import com.touchus.publicutils.utils.UtilTools;
import com.touchus.publicutils.utils.WeatherUtils;
import java.lang.ref.WeakReference;
import java.util.Calendar;

public class WeatherFrag extends Fragment {
    /* access modifiers changed from: private */
    public LauncherApplication app;
    public TextView mCity;
    public TextView mDate;
    private Mhandler mHandler = new Mhandler(this);
    public TextView mHour;
    public TextView mMinute;
    public TextView mTemp;
    public TextView mTempDes;
    public ImageView mWeather;
    public TextView mWeek;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_left_weather, container, false);
        this.mWeather = (ImageView) view.findViewById(R.id.mainleft_weather);
        this.mCity = (TextView) view.findViewById(R.id.mainleft_weather_city);
        this.mTempDes = (TextView) view.findViewById(R.id.mainleft_weather_tempdesc);
        this.mTemp = (TextView) view.findViewById(R.id.mainleft_weather_temperature);
        this.mWeek = (TextView) view.findViewById(R.id.mainleft_weather_week);
        this.mDate = (TextView) view.findViewById(R.id.mainleft_weather_date);
        this.mHour = (TextView) view.findViewById(R.id.weather_hour);
        this.mMinute = (TextView) view.findViewById(R.id.weather_minute);
        this.mWeather.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (UtilTools.isFastDoubleClick() && WeatherFrag.this.app.service != null) {
                    WeatherFrag.this.app.service.regainWeather();
                }
            }
        });
        return view;
    }

    public void onStart() {
        this.app = (LauncherApplication) getActivity().getApplication();
        this.app.mOriginalViewHandler = this.mHandler;
        setAllWeatherInfo();
        setTimeInfo();
        this.app.registerHandler(this.mHandler);
        super.onStart();
    }

    public void onStop() {
        this.app.unregisterHandler(this.mHandler);
        super.onStop();
    }

    private void setAllWeatherInfo() {
        if (this.app != null) {
            this.mCity.setText(this.app.currentCityName);
            this.mTempDes.setText(this.app.weatherTempDes);
            this.mTemp.setText(this.app.weatherTemp);
            this.mWeather.setImageResource(WeatherUtils.getResId(getActivity(), this.app.weatherTempDes));
        }
    }

    private void setTimeInfo() {
        if (this.app.year > 0) {
            int year = this.app.year;
            int month = this.app.month;
            int day = this.app.day;
            int time = this.app.hour;
            int min = this.app.min;
            Calendar cal = Calendar.getInstance();
            cal.set(1, year);
            cal.set(2, month - 1);
            cal.set(5, day);
            String mWay = String.valueOf(cal.get(7));
            if ("1".equals(mWay)) {
                mWay = getString(R.string.name_tvivw_week0);
            } else if ("2".equals(mWay)) {
                mWay = getString(R.string.name_tvivw_week1);
            } else if ("3".equals(mWay)) {
                mWay = getString(R.string.name_tvivw_week2);
            } else if ("4".equals(mWay)) {
                mWay = getString(R.string.name_tvivw_week3);
            } else if ("5".equals(mWay)) {
                mWay = getString(R.string.name_tvivw_week4);
            } else if ("6".equals(mWay)) {
                mWay = getString(R.string.name_tvivw_week5);
            } else if ("7".equals(mWay)) {
                mWay = getString(R.string.name_tvivw_week6);
            }
            this.mWeek.setText(mWay);
            String tMonth = new StringBuilder().append(month).toString();
            if (month < 10) {
                tMonth = "0" + month;
            }
            String tDay = new StringBuilder().append(day).toString();
            if (day < 10) {
                tDay = "0" + day;
            }
            String tMin = new StringBuilder().append(min).toString();
            if (min < 10) {
                tMin = "0" + min;
            }
            String tTime = new StringBuilder().append(time).toString();
            if (time < 10) {
                tTime = "0" + time;
            }
            this.mDate.setText(String.valueOf(year) + getString(R.string.name_date_year) + tMonth + getString(R.string.name_date_month) + tDay + getString(R.string.name_date_day));
            this.mHour.setText(tTime);
            this.mMinute.setText(tMin);
        }
    }

    /* access modifiers changed from: private */
    public void handlerMsg(Message msg) {
        if (getActivity() != null) {
            if (msg.what == 1009) {
                setTimeInfo();
            } else if (msg.what == 1010) {
                setAllWeatherInfo();
            }
        }
    }

    static class Mhandler extends Handler {
        private WeakReference<WeatherFrag> target;

        public Mhandler(WeatherFrag activity) {
            this.target = new WeakReference<>(activity);
        }

        public void handleMessage(Message msg) {
            if (this.target.get() != null) {
                ((WeatherFrag) this.target.get()).handlerMsg(msg);
            }
        }
    }
}
