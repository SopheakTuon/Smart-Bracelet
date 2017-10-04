package com.example.android.bluetoothlegatt.ble_service.util;

/**
 * @author Sopheak Tuon
 * @created on 04-Oct-17
 */

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    private static final String LOG_TAG = DateUtils.class.getSimpleName();

    public static int currentTimeSeconds() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    public void getTimeByDate() {
        Date date = new Date();
        System.out.println(DateFormat.getDateInstance().format(date));
        System.out.println(DateFormat.getDateTimeInstance().format(date));
        System.out.println(DateFormat.getTimeInstance().format(date));
        System.out.println(DateFormat.getDateTimeInstance(0, 0).format(date));
        System.out.println(DateFormat.getDateTimeInstance(1, 1).format(date));
        System.out.println(DateFormat.getDateTimeInstance(3, 3).format(date));
        System.out.println(DateFormat.getDateTimeInstance(2, 2).format(date));
    }

    public static void showTimeByCalendar(Date theDate) {
        Calendar cal = Calendar.getInstance();
        if (theDate != null) {
            cal.setTime(theDate);
        }
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR) + (cal.get(Calendar.AM_PM) * 12);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        Log.i("", "dateTest, 现在的时间是：公元" + year + "年" + month + "月" + day + "日      " + hour + "时" + minute + "分" + second + "秒      " + dayOfMonth + "   月的第 " + dayOfMonth + " 天" + dayOfWeek + "   年的第 " + cal.get(Calendar.WEEK_OF_YEAR) + " 周");
    }

    public static String getFormart_HH_MM(long theSeconds) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(1000 * theSeconds);
        int hour = cal.get(Calendar.HOUR) + (cal.get(Calendar.AM_PM) * 12);
        int minute = cal.get(Calendar.MINUTE);
        String _h = "" + hour;
        if (hour < 10) {
            _h = "0" + _h;
        }
        String _m = "" + minute;
        if (minute < 10) {
            _m = "0" + _m;
        }
        String ret = "" + _h + ":" + _m;
        Log.e("", "ret = " + ret + "; test = " + getDateFromSec(theSeconds).toString());
        return ret;
    }

    public static int getMonthOfYear(Date theDate) {
        Calendar cal = Calendar.getInstance();
        if (theDate != null) {
            cal.setTime(theDate);
        }
        return cal.get(Calendar.MONTH) + 1;
    }

    public static int getDayOfWeek(Date theDate) {
        Calendar cal = Calendar.getInstance();
        if (theDate != null) {
            cal.setTime(theDate);
        }
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    public static int getDayOfYear(Date theDate) {
        Calendar cal = Calendar.getInstance();
        if (theDate != null) {
            cal.setTime(theDate);
        }
        return cal.get(Calendar.DAY_OF_YEAR);
    }

    public static int getWeekOfYear(String the_date) {
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(the_date));
            cal.add(Calendar.MONTH, -1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

    public static int getWeekOfYear(Date theDate) {
        Calendar cal = Calendar.getInstance();
        if (theDate != null) {
            cal.setTime(theDate);
        }
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

    private static Date getTodayNYR(Date theDate) {
        Calendar cal = Calendar.getInstance();
        if (theDate != null) {
            Log.e("", "getTodayNYR....theDate = " + theDate);
            cal.setTime(theDate);
        }
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        Date ret = parseDayString("" + year + "-" + month + "-" + cal.get(Calendar.DAY_OF_MONTH));
        Log.e("", "getTodayNYR....ret = " + ret);
        return ret;
    }

    public static String getTodayNYR2Str(Date theDate) {
        Calendar cal = Calendar.getInstance();
        if (theDate != null) {
            cal.setTime(theDate);
        }
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String _m = "" + month;
        if (month < 10) {
            _m = "0" + _m;
        }
        String _d = "" + day;
        if (day < 10) {
            _d = "0" + _d;
        }
        return "" + year + "-" + _m + "-" + _d;
    }

    public static Date getThedayBeginDate(Date theDate, long offset) {
        if (theDate == null) {
            theDate = getTodayNYR(null);
        }
        if (0 == offset) {
            return theDate;
        }
        return transferLongToDate(Long.valueOf(theDate.getTime() + ((((24 * offset) * 60) * 60) * 1000)));
    }

    public static Date getDateFromSec(long seconds) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(1000 * seconds);
        return cal.getTime();
    }

    private static Date transferLongToDate(Long millSec) {
        return new Date(millSec.longValue());
    }

    public static Date parseDayString(String dayString) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dayString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getNowTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    public static String getNowTimeToMill() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(Calendar.getInstance().getTime());
    }

    public static int getAge(String birthday) {
        Date the_date = null;
        try {
            the_date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(birthday);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);
        cal.setTime(the_date);
        cal.add(Calendar.MONTH, -1);
        return currentYear - cal.get(Calendar.YEAR);
    }

    public static String getZW(int monthOfYear) {
        String ret = "";
        switch (monthOfYear) {
            case 1:
                return "1";
            case 2:
                return "2";
            case 3:
                return "3";
            case 4:
                return "4";
            case 5:
                return "5";
            case 6:
                return Constants.VIA_SHARE_TYPE_INFO;
            case 7:
                return "7";
            case 8:
                return "8";
            case 9:
                return "9";
            case 10:
                return Constants.VIA_REPORT_TYPE_SHARE_TO_QQ;
            case 11:
                return Constants.VIA_REPORT_TYPE_SHARE_TO_QZONE;
            case 12:
                return Constants.VIA_REPORT_TYPE_SET_AVATAR;
            default:
                return ret;
        }
    }
}
