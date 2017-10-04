package com.example.android.bluetoothlegatt.ble_service;

/**
 * @author Sopheak Tuon
 * @created on 04-Oct-17
 */

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class BleDataForSleepData extends BleBaseDataManage {
    public static final String TAG = BleDataForSleepData.class.getSimpleName();
    public static final byte fromDevice = (byte) -90;
    private static volatile BleDataForSleepData instance = null;
    public static final byte toDevice = (byte) 38;
    private final int GET_SLEEP_DATA = 0;
    private DataSendCallback callback;
    private int count = 0;
    private boolean isBack = false;
    private boolean isComm = false;
    private Context mContext;
    private Handler sleepHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (BleDataForSleepData.this.isBack) {
                        BleDataForSleepData.this.closeSendData(this);
                        return;
                    } else if (BleDataForSleepData.this.count < 4) {
                        BleDataForSleepData.this.continueSendData(this, msg.arg1, msg.arg2);
                        BleDataForSleepData.this.getTodaySleepdata();
                        return;
                    } else {
                        BleDataForSleepData.this.closeSendData(this);
                        return;
                    }
                default:
                    return;
            }
        }
    };

    private void continueSendData(Handler handler, int length, int reLength) {
        Message msg = handler.obtainMessage();
        msg.what = 0;
        msg.arg1 = length;
        msg.arg2 = reLength;
        handler.sendMessageDelayed(msg, (long) SendLengthHelper.getSendLengthDelay(length, reLength));
        this.count++;
    }

    private void closeSendData(Handler handler) {
        handler.removeMessages(0);
        if (this.callback != null) {
            if (!this.isBack) {
                this.callback.sendFailed();
            }
            this.callback.sendFinished();
        }
        this.isBack = false;
        this.count = 0;
    }

    public static BleDataForSleepData getInstance(Context context) {
        if (instance == null) {
            synchronized (BleDataForSleepData.class) {
                if (instance == null) {
                    instance = new BleDataForSleepData(context);
                }
            }
        }
        return instance;
    }

    private BleDataForSleepData(Context mContext) {
        this.mContext = mContext;
    }

    private int[] getTodayDate() {
      int[]  dates = new int[3];
        Calendar calendarCurrent = Calendar.getInstance(Locale.getDefault());
        dates[0] = calendarCurrent.get(Calendar.DAY_OF_MONTH);
        dates[1] = calendarCurrent.get(Calendar.MONTH) + 1;
        dates[2] = calendarCurrent.get(Calendar.YEAR) - 2000;
        return dates;
    }

    public void getSleepingData() {
        this.isComm = true;
        int datalength = getTodaySleepdata();
        Message msg = this.sleepHandler.obtainMessage();
        msg.what = 0;
        msg.arg1 = datalength;
        msg.arg2 = 46;
        this.sleepHandler.sendMessageDelayed(msg, (long) SendLengthHelper.getSendLengthDelay(datalength, 46));
    }

    private int getTodaySleepdata() {
        int[] dates = getTodayDate();
        byte[] bytes = new byte[]{(byte) (dates[0] & 255), (byte) (dates[1] & 255), (byte) (dates[2] & 255), (byte) 2};
        return setMsgToByteDataAndSendToDevice((byte) 38, bytes, bytes.length);
    }

    public void getTodaySleepdataAndCallback() {
        int[] dates = getTodayDate();
        byte[] bytes = new byte[]{(byte) (dates[0] & 255), (byte) (dates[1] & 255), (byte) (dates[2] & 255), (byte) 2};
        setMsgToByteDataAndSendToDevice((byte) 38, bytes, bytes.length);
    }

    public void setOnSleepDataRecever(DataSendCallback callback) {
        this.callback = callback;
    }

    public void dealTheSleepData(byte[] bufferTmp) {
        if (this.isComm) {
            this.isBack = true;
            this.isComm = false;
            if (this.callback == null) {
                Log.e(TAG, "bleDataForSleepData callback 为空");
            } else {
                this.callback.sendSuccess(bufferTmp);
            }
        }
        int d = bufferTmp[0] & 255;
        int m = bufferTmp[1] & 255;
        int y = bufferTmp[2] & 255;
        Calendar calendarFromSleepData = Calendar.getInstance(TimeZone.getDefault());
        calendarFromSleepData.set(y + 2000, m - 1, d);
        String curr = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendarFromSleepData.getTime());
        String dateSleep = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance(TimeZone.getDefault()).getTime());
        Log.i(TAG, "时间对比：" + curr + "--" + dateSleep);
        if (curr != null && !curr.equals("") && dateSleep != null && !dateSleep.equals("") && !curr.equals(dateSleep)) {
            byte[] backData = new byte[4];
            for (int i = 0; i < backData.length; i++) {
                backData[i] = bufferTmp[i];
            }
            setMsgToByteDataAndSendToDevice((byte) 38, backData, backData.length);
            if (this.callback == null) {
                Log.e(TAG, "bleDataForSleepData callback 为空");
            } else {
                this.callback.sendSuccess(bufferTmp);
            }
        }
    }
}
