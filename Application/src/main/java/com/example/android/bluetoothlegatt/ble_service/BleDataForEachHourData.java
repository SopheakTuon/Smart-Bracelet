package com.example.android.bluetoothlegatt.ble_service;

/**
 * @author Sopheak Tuon
 * @created on 04-Oct-17
 */

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class BleDataForEachHourData extends BleBaseDataManage {
    private static final String TAG = BleDataForEachHourData.class.getSimpleName();
    public static BleDataForEachHourData bleDataForEachHourData = null;
    public static final byte fromDevice = (byte) -90;
    public static final byte toDevice = (byte) 38;
    private final int GET_EACH_HOUR_DATA = 0;
    private Handler eachHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (BleDataForEachHourData.this.isSendOk) {
                        BleDataForEachHourData.this.stopSend(this);
                        return;
                    } else if (BleDataForEachHourData.this.sendCount < 4) {
                        BleDataForEachHourData.this.continueSendData(this, msg);
                        BleDataForEachHourData.this.getTheEcchHourData();
                        return;
                    } else {
                        BleDataForEachHourData.this.stopSend(this);
                        return;
                    }
                default:
                    return;
            }
        }
    };
    private boolean isComm = false;
    private boolean isSendOk = false;
    private DataSendCallback sendCallback;
    private int sendCount = 0;

    private BleDataForEachHourData() {
    }

    public static BleDataForEachHourData getEachHourDataInstance() {
        if (bleDataForEachHourData == null) {
            synchronized (BleDataForEachHourData.class) {
                if (bleDataForEachHourData == null) {
                    bleDataForEachHourData = new BleDataForEachHourData();
                }
            }
        }
        return bleDataForEachHourData;
    }

    private void continueSendData(Handler handler, Message msg) {
        Message msges = handler.obtainMessage();
        msges.what = 0;
        msges.arg1 = msg.arg1;
        msges.arg2 = msg.arg2;
        handler.sendMessageDelayed(msges, (long) SendLengthHelper.getSendLengthDelay(msg.arg1, msg.arg2));
        this.sendCount++;
    }

    private void stopSend(Handler handler) {
        handler.removeMessages(0);
        if (!this.isSendOk) {
            this.sendCallback.sendFailed();
        }
        this.sendCallback.sendFinished();
        this.isSendOk = false;
        this.sendCount = 0;
    }

    public void getEachData() {
        this.isComm = true;
        int sendLength = getTheEcchHourData();
        Message msg = this.eachHandler.obtainMessage();
        msg.what = 0;
        msg.arg1 = sendLength;
        msg.arg2 = 300;
        this.eachHandler.sendMessageDelayed(msg, (long) SendLengthHelper.getSendLengthDelay(sendLength, 300));
    }

    private int getTheEcchHourData() {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR) - 2000;
        byte[] bytes = new byte[]{(byte) (day & 255), (byte) (month & 255), (byte) (year & 255), (byte) 1};
        return setMsgToByteDataAndSendToDevice((byte) 38, bytes, bytes.length);
    }

    public void dealTheEachData(byte[] eachData) {
        if (this.isComm) {
            this.isSendOk = true;
            this.isComm = false;
            if (this.sendCallback != null) {
                this.sendCallback.sendSuccess(eachData);
            }
        }
        Log.i(TAG, "每小时数据：" + FormatUtils.bytesToHexString(eachData));
        int monthNow = eachData[1] & 255;
        String dayS = formatTheDataDate((eachData[2] & 255) + 2000, monthNow, eachData[0] & 255);
        String dateCurrent = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance(TimeZone.getDefault()).getTime());
        if (dateCurrent != null && dayS != null && !dateCurrent.equals(dayS)) {
            byte[] backData = new byte[4];
            for (int i = 0; i < backData.length; i++) {
                backData[i] = eachData[i];
            }
            setMsgToByteDataAndSendToDevice((byte) 38, backData, backData.length);
            if (this.sendCallback != null) {
                this.sendCallback.sendSuccess(eachData);
            }
        }
    }

    private String formatTheDataDate(int yearNow, int monthNow, int dayNow) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.set(yearNow, monthNow - 1, dayNow);
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
    }

    public void setOnBleDataReceListener(DataSendCallback dataCallback) {
        this.sendCallback = dataCallback;
    }
}
