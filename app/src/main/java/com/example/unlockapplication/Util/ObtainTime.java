package com.example.unlockapplication.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ObtainTime {

    long currentDate;
    SimpleDateFormat simpleDateFormat;

    public ObtainTime() {
        this.currentDate = System.currentTimeMillis();
    }

    public String getTime(){
        simpleDateFormat = new SimpleDateFormat("HH:mm");
        String time = simpleDateFormat.format(currentDate);
        return time;
    }

    public String getDate(){
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

    /*public String getLunarDate(){
        simpleDateFormat = new SimpleDateFormat("MM月dd日");
        String date = simpleDateFormat.format(currentDate);
        return date;
    }*/

}
