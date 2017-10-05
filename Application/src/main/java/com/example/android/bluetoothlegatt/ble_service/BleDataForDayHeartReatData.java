package com.example.android.bluetoothlegatt.ble_service;

/**
 * @author Sopheak Tuon
 * @created on 04-Oct-17
 */

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class BleDataForDayHeartReatData extends BleBaseDataManage {
    private static final String TAG = BleDataForDayHeartReatData.class.getSimpleName();
    private static BleDataForDayHeartReatData bleDataForDayHeartReatData = null;
    public static final byte fromDevice = (byte) -90;
    public static final byte toDevice = (byte) 38;
    private final String DATA_PACKAGE = "data_pak_send";
    private final int GET_HEART_WRATE_DATA = 0;
    private final String PACKAGE = "package_int";
    private DataSendCallback hrCallback;
    private Handler hrHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (BleDataForDayHeartReatData.this.isSendOk) {
                        BleDataForDayHeartReatData.this.stopSendData(this);
                        return;
                    } else if (BleDataForDayHeartReatData.this.sendTimes < 4) {
                        BleDataForDayHeartReatData.this.continueSendData(this, msg);
                        byte[] datas = msg.getData().getByteArray("data_pak_send");
                        BleDataForDayHeartReatData.this.setMsgToByteDataAndSendToDevice((byte) 38, datas, datas.length);
                        return;
                    } else {
                        BleDataForDayHeartReatData.this.stopSendData(this);
                        return;
                    }
                default:
                    return;
            }
        }
    };
    private boolean isSendOk = false;
    private Context mContext;
    private int packageCount = 0;
    private int sendTimes = 0;
    private String stringDateFormat;

    private void continueSendData(Handler handler, Message msg) {
        Message msges = handler.obtainMessage();
        msges.what = 0;
        msges.arg1 = msg.arg1;
        msges.arg2 = msg.arg2;
        msges.setData(msg.getData());
        handler.sendMessageDelayed(msges, (long) SendLengthHelper.getSendLengthDelay(msg.arg1, msg.arg2));
        this.sendTimes++;
    }

    private void stopSendData(Handler handler) {
        handler.removeMessages(0);
        this.isSendOk = false;
        this.sendTimes = 0;
        synchronized (this) {
            notify();
        }
        if (this.packageCount == 1) {
            this.hrCallback.sendFinished();
        }
    }

    public static BleDataForDayHeartReatData getHRDataInstance(Context context) {
        if (bleDataForDayHeartReatData == null) {
            synchronized (BleDataForDayHeartReatData.class) {
                if (bleDataForDayHeartReatData == null) {
                    bleDataForDayHeartReatData = new BleDataForDayHeartReatData(context);
                }
            }
        }
        return bleDataForDayHeartReatData;
    }

    private BleDataForDayHeartReatData(Context mContext) {
        this.mContext = mContext;
    }

    public void setOnHrDataRecever(DataSendCallback hrCallback) {
        this.hrCallback = hrCallback;
    }

    public void requestHeartReatDataAll() {
        byte[] bytes = new byte[6];
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR) - 2000;
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int mi = calendar.get(Calendar.MINUTE);
        int count = hour / 3;
        if (!(hour % 3 == 0 && mi == 0)) {
            count++;
        }
        bytes[0] = (byte) (day & 255);
        bytes[1] = (byte) (month & 255);
        bytes[2] = (byte) (year & 255);
        bytes[3] = (byte) 3;
        bytes[4] = (byte) 8;
//        synchronized (this) {
            for (int i = count; i >= 1; i--) {
                this.packageCount = i;
                bytes[5] = (byte) (i & 255);
                int length = setMsgToByteDataAndSendToDevice((byte) 38, bytes, bytes.length);
                Message msg = this.hrHandler.obtainMessage();
                msg.what = 0;
                msg.arg1 = length;
                msg.arg2 = 300;
                Bundle bundle = new Bundle();
                bundle.putByteArray("data_pak_send", bytes);
                msg.setData(bundle);
                this.hrHandler.sendMessageDelayed(msg, (long) SendLengthHelper.getSendLengthDelay(length, 300));
//                try {
//                    wait();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
//        }
    }

    public void dealTheHeartRateData(byte[] hr) {
        if (hr[4] == (byte) 8 && hr[5] == this.packageCount) {
            this.isSendOk = true;
        }
        if (this.hrCallback != null) {
            this.hrCallback.sendSuccess(hr);
        }
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        int dayCurrent = calendar.get(Calendar.DAY_OF_MONTH);
        int monthCurrent = calendar.get(Calendar.MONTH) + 1;
        int yearCurrent = calendar.get(Calendar.YEAR);
        int day = hr[0] & 255;
        int month = hr[1] & 255;
        int year = (hr[2] & 255) + 2000;
        Calendar calendarData = Calendar.getInstance();
        calendarData.set(year, month - 1, day);
        String dataDay = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendarData.getTime());
        if (day != dayCurrent || month != monthCurrent || year != yearCurrent) {
            Log.i(TAG, "心率日期对比：" + day + "--" + dayCurrent + "--" + month + "--" + monthCurrent + "--" + year + "--" + yearCurrent);
            byte[] responseData = new byte[6];
            for (int i = 0; i < responseData.length; i++) {
                responseData[i] = hr[i];
            }
            setMsgToByteDataAndSendToDevice((byte) 38, responseData, responseData.length);
        }
    }
}
