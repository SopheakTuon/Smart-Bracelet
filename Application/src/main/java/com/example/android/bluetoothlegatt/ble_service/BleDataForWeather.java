package com.example.android.bluetoothlegatt.ble_service;

/**
 * @author Sopheak Tuon
 * @created on 04-Oct-17
 */

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class BleDataForWeather extends BleBaseDataManage {
    private static BleDataForWeather bleDataForWeather = null;
    public static final byte fromDevice = (byte) -113;
    public static final byte fromDeviceNew = (byte) -79;
    public static final byte toDevice = (byte) 15;
    public static final byte toDeviceNew = (byte) 49;
    private final int SEND_WEATHER_DATA_TO_DEVICE = 0;
    private boolean isBack = false;
    private int sendCount = 0;
    private DataSendCallback weatherCallbackListener = null;
    private Handler weatherHandler = new C14641();

    class C14641 extends Handler {
        C14641() {
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (BleDataForWeather.this.isBack) {
                        BleDataForWeather.this.closeSend(this, msg);
                        return;
                    } else if (BleDataForWeather.this.sendCount < 4) {
                        BleDataForWeather.this.sendWeatherDataToDevice((byte) msg.arg1, msg.getData().getString("weatherData"));
                        BleDataForWeather.this.continueSend(this, msg);
                        return;
                    } else {
                        BleDataForWeather.this.closeSend(this, msg);
                        return;
                    }
                default:
                    return;
            }
        }
    }

    private BleDataForWeather() {
    }

    public static BleDataForWeather getIntance() {
        if (bleDataForWeather == null) {
            synchronized (BleDataForWeather.class) {
                if (bleDataForWeather == null) {
                    bleDataForWeather = new BleDataForWeather();
                }
            }
        }
        return bleDataForWeather;
    }

    private void continueSend(Handler handler, Message msg) {
        Message mzg = handler.obtainMessage();
        mzg.what = msg.what;
        mzg.setData(msg.getData());
        handler.sendMessageDelayed(mzg, 1000);
        this.sendCount++;
    }

    private void closeSend(Handler handler, Message msg) {
        handler.removeMessages(0);
        this.isBack = false;
        this.sendCount = 0;
    }

    public void sendWeather(byte byt, String city, String time, int weather, String weatherS, String temp, int zyx, int windFL, int windDrie, String aqi, String cuTemp) {
        String dataAll = getAllDataString(byt, city, time, weather, weatherS, temp, zyx, windFL, windDrie, aqi, cuTemp);
        sendWeatherDataToDevice(byt, dataAll);
        Message msg = this.weatherHandler.obtainMessage();
        msg.what = 0;
        msg.arg1 = byt;
        Bundle bundle = new Bundle();
        bundle.putString("weatherData", dataAll);
        msg.setData(bundle);
        this.weatherHandler.sendMessageDelayed(msg, 1000);
    }

    private String getAllDataString(byte byt, String city, String time, int weather, String weatherS, String temp, int zyx, int windFL, int windDrie, String aqi, String cuTemp) {
        byte[] times = getTimeBytes(time);
        byte[] citys = getCityBytes(city);
        byte weath = (byte) weather;
        byte[] temps = getTemp(temp, cuTemp);
        byte ults = (byte) zyx;
        byte winds = (byte) windFL;
        byte dires = (byte) windDrie;
        byte aqis = getAqiBytes(aqi);
        StringBuffer alldata = new StringBuffer();
        if (byt == toDeviceNew) {
            alldata.append("01");
        }
        alldata.append("00");
        alldata.append("0" + times.length);
        alldata.append(FormatUtils.bytesToHexString(times));
        alldata.append("01");
        String cityS = "";
        if (citys.length < 16) {
            cityS = cityS + "0";
        }
        alldata.append(cityS + Integer.toHexString(citys.length));
        alldata.append(FormatUtils.bytesToHexString(citys));
        alldata.append("02");
        alldata.append("01");
        alldata.append(FormatUtils.byteToHexStringNo0X(weath));
        alldata.append("03");
        alldata.append(getWeatherString(weatherS)[0]);
        alldata.append(getWeatherString(weatherS)[1]);
        alldata.append("04");
        alldata.append("0" + temps.length);
        alldata.append(FormatUtils.bytesToHexString(temps));
        alldata.append("05");
        if (ults == (byte) 0) {
            alldata.append("00");
        } else {
            alldata.append("01");
            alldata.append(FormatUtils.byteToHexStringNo0X(ults));
        }
        alldata.append("06");
        alldata.append("01");
        alldata.append(FormatUtils.byteToHexStringNo0X(winds));
        alldata.append("07");
        alldata.append("01");
        alldata.append(FormatUtils.byteToHexStringNo0X(dires));
        alldata.append("08");
        if (aqis == (byte) 0) {
            alldata.append("00");
        } else {
            alldata.append("01");
            alldata.append(FormatUtils.byteToHexStringNo0X(aqis));
        }
        return alldata.toString();
    }

    private String[] getWeatherString(String weatherS) {
        String[] weatherInfo = new String[2];
        String weatherStringLength = "";
        String weatherString = "";
        int weatherLength = 0;
        try {
            byte[] weatherByte = weatherS.getBytes("utf-8");
            if (weatherByte.length > 48) {
                if (Locale.getDefault().getCountry().equals("zh")) {
                    weatherByte = weatherS.substring(0, 17).getBytes("utf-8");
                } else {
                    weatherByte = weatherS.substring(0, 49).getBytes("utf-8");
                }
            }
            weatherLength = weatherByte.length;
            weatherString = FormatUtils.bytesToHexString(weatherByte);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (weatherLength < 16) {
            weatherStringLength = weatherStringLength + "0";
        }
        weatherInfo[0] = weatherStringLength + Integer.toHexString(weatherLength);
        weatherInfo[1] = weatherString;
        return weatherInfo;
    }

    private byte getAqiBytes(String aqi) {
        return (byte) (Integer.valueOf(aqi).intValue() & 255);
    }

    private byte[] getCityBytes(String city) {
        if (city.length() > 8) {
            city = city.substring(0, 8);
        }
        try {
            return city.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void sendWeatherDataToDevice(byte byt, String allData) {
        setMessageDataByString(byt, allData.toString(), true);
    }

    public void dealWeatherBack(byte[] data) {
        this.isBack = true;
        if (this.weatherCallbackListener != null) {
            this.weatherCallbackListener.sendSuccess(data);
        }
    }

    private byte[] getTimeBytes(String time) {
        Date date;
        if (time.length() > 10) {
            byte[] timeData = new byte[4];
            date = null;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar calend = Calendar.getInstance(TimeZone.getDefault());
            calend.setTime(date);
            int hour = calend.get(Calendar.HOUR_OF_DAY);
            int day = calend.get(Calendar.DAY_OF_MONTH);
            int month = calend.get(Calendar.MONTH) + 1;
            int year = calend.get(Calendar.YEAR) - 2000;
            timeData[0] = (byte) hour;
            timeData[1] = (byte) day;
            timeData[2] = (byte) month;
            timeData[3] = (byte) year;
            return timeData;
        }
        byte[] timeData = new byte[3];
        date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(time);
        } catch (ParseException e2) {
            e2.printStackTrace();
        }
        Calendar calend = Calendar.getInstance(TimeZone.getDefault());
        calend.setTime(date);
        int month = calend.get(Calendar.MONTH) + 1;
        int year = calend.get(Calendar.YEAR) - 2000;
        timeData[0] = (byte) calend.get(Calendar.DAY_OF_MONTH);
        timeData[1] = (byte) month;
        timeData[2] = (byte) year;
        return timeData;
    }

    private byte[] getTemp(String temp, String cutemp) {
        byte[] temps;
        if (cutemp == null || !cutemp.equals("")) {
            temps = new byte[3];
            String[] tempss = temp.split("~");
            int oneIndex = tempss[1].indexOf("℃");
            int twoIndex = tempss[0].indexOf("℃");
            temps[0] = (byte) Integer.parseInt(tempss[1].substring(0, oneIndex));
            temps[1] = (byte) Integer.parseInt(tempss[0].substring(0, twoIndex));
            temps[2] = (byte) Integer.parseInt(cutemp);
            return temps;
        }
        temps = new byte[2];
        String[] tempss = temp.split("~");
        int oneIndex = tempss[1].indexOf("℃");
        int twoIndex = tempss[0].indexOf("℃");
        temps[0] = (byte) Integer.parseInt(tempss[1].substring(0, oneIndex));
        temps[1] = (byte) Integer.parseInt(tempss[0].substring(0, twoIndex));
        return temps;
    }

    private byte[] getWeatherBytes(String weather) {
        byte[] weath = new byte[0];
        try {
            byte[] wea = weather.getBytes("UTF-8");
            if (wea.length > 72) {
                weath = new byte[72];
                System.arraycopy(wea, 0, weath, 0, 72);
                return weath;
            }
            weath = new byte[wea.length];
            System.arraycopy(wea, 0, weath, 0, wea.length);
            return weath;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return weath;
        }
    }

    public void setWeatherCallbackListener(DataSendCallback weatherCallbackListener) {
        this.weatherCallbackListener = weatherCallbackListener;
    }

    public void sendDatesWeather(byte byt, String city, List<WeatherEntity> datas) {
        if (datas.size() > 0) {
            StringBuffer dataWeather = new StringBuffer();
            dataWeather.append("02");
            for (WeatherEntity enty : datas) {
                Log.i("weathers", "date:" + enty.getDate());
                Log.i("weathers", "temp:" + enty.getTemp());
                Log.i("weathers", "weather:" + enty.getWeather());
                dataWeather.append(FormatUtils.bytesToHexString(getTimeBytes(enty.getDate())));
                String wea = String.valueOf(enty.getWeather());
                if (wea.length() < 2) {
                    wea = "0" + wea;
                }
                dataWeather.append(wea);
                dataWeather.append(FormatUtils.bytesToHexString(getTemp(enty.getTemp(), "")));
            }
            setMessageDataByString(byt, dataWeather.toString(), true);
        }
    }
}
