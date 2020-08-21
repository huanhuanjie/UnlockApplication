package com.example.unlockapplication.util;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeUtil {

    SimpleDateFormat simpleDateFormat;

    public String getTime(){
        long currentDate = System.currentTimeMillis();
        simpleDateFormat = new SimpleDateFormat("HH:mm");
        String time = simpleDateFormat.format(currentDate);
        return time;
    }

    public String getDate(){
        long currentDate = System.currentTimeMillis();
        simpleDateFormat = new SimpleDateFormat("MM月dd日");
        String date = simpleDateFormat.format(currentDate);
        return date;
    }

    public String getWeek(){
        Calendar cal = Calendar.getInstance();
        int i = cal.get(Calendar.DAY_OF_WEEK);
        switch (i) {
            case 1:
                return "星期日";
            case 2:
                return "星期一";
            case 3:
                return "星期二";
            case 4:
                return "星期三";
            case 5:
                return "星期四";
            case 6:
                return "星期五";
            case 7:
                return "星期六";
            default:
                return "";
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String getLunarDate(){
        Calendar calendar = Calendar.getInstance();
        LunarUtil lunarUtil = new LunarUtil(calendar);
        String lunarDate = lunarUtil.toString();
        return lunarDate;
    }





}
