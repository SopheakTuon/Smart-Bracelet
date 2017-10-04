package com.example.android.bluetoothlegatt.ble_service;

/**
 * @author Sopheak Tuon
 * @created on 04-Oct-17
 */

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class BleDataForDayData extends BleBaseDataManage {
    public static final String TAG = BleDataForDayData.class.getSimpleName();
    private static BleDataForDayData bleDataForDayData;
    public static byte fromDevice = (byte) -90;
    public static byte toDevice = (byte) 38;
    private final int GET_DAY_DATA_HANDLER = 0;
    private Context context;
    private DataSendCallback dayCallback;
    private Handler dayHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (BleDataForDayData.this.isSendOk) {
                        BleDataForDayData.this.stopSendData(this);
                        return;
                    } else if (BleDataForDayData.this.sendCount < 4) {
                        BleDataForDayData.this.continueSend(this, msg);
                        BleDataForDayData.this.requestDayDate();
                        return;
                    } else {
                        BleDataForDayData.this.stopSendData(this);
                        return;
                    }
                default:
                    return;
            }
        }
    };
    private boolean hasComm = false;
    private boolean isSendOk = false;
    private int sendCount = 0;

    private void continueSend(Handler handler, Message msges) {
        Message msg = handler.obtainMessage();
        msg.what = 0;
        msg.arg1 = msges.arg1;
        msg.arg2 = msges.arg2;
        handler.sendMessageDelayed(msg, (long) SendLengthHelper.getSendLengthDelay(msges.arg1, msges.arg2));
        this.sendCount++;
    }

    private void stopSendData(Handler handler) {
        handler.removeMessages(0);
        if (!(this.isSendOk || this.dayCallback == null)) {
            this.dayCallback.sendFailed();
        }
        if (this.dayCallback != null) {
            this.dayCallback.sendFinished();
        }
        this.isSendOk = false;
        this.sendCount = 0;
    }

    private BleDataForDayData(Context context) {
        this.context = context;
    }

    public static BleDataForDayData getDayDataInstance(Context context) {
        if (bleDataForDayData == null) {
            synchronized (BleDataForDayData.class) {
                if (bleDataForDayData == null) {
                    bleDataForDayData = new BleDataForDayData(context);
                }
            }
        }
        return bleDataForDayData;
    }

    public void getDayData() {
        this.hasComm = true;
        int length = requestDayDate();
        Message msg = this.dayHandler.obtainMessage();
        msg.what = 0;
        msg.arg1 = length;
        msg.arg2 = 38;
        this.dayHandler.sendMessageDelayed(msg, (long) SendLengthHelper.getSendLengthDelay(length, 38));
    }

    private int requestDayDate() {
        byte[] reqData = new byte[4];
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        reqData[0] = (byte) cal.get(Calendar.DAY_OF_MONTH);
        reqData[1] = (byte) month;
        reqData[2] = (byte) (year - 2000);
        reqData[3] = (byte) 0;
        return setMsgToByteDataAndSendToDevice(toDevice, reqData, reqData.length);
    }

    public void juestResponse(byte[] data) {
        byte[] responseData = new byte[4];
        System.arraycopy(data, 0, responseData, 0, responseData.length);
        setMsgToByteDataAndSendToDevice(toDevice, responseData, responseData.length);
    }

    public void dealDayData(Context mContext, byte[] data) {
        if (this.hasComm) {
            this.isSendOk = true;
            this.hasComm = false;
            if (this.dayCallback != null) {
                this.dayCallback.sendSuccess(data);
            }
        }
        int day = data[0];
        String dataDate = formatTheDate(data[2] + 2000, data[1], day);
        String dateC = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance(TimeZone.getDefault()).getTime());
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        if (dataDate != null && dateC != null && !dataDate.equals(dateC)) {
            byte[] bytess = new byte[]{(byte) day, (byte) month, (byte) year, (byte) 0};
            setMsgToByteDataAndSendToDevice(toDevice, bytess, bytess.length);
            if (this.dayCallback != null) {
                this.dayCallback.sendSuccess(data);
            }
        }
    }

    public void setOnDayDataListener(DataSendCallback sendCallback) {
        this.dayCallback = sendCallback;
    }

    private String formatTheDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.set(year, month - 1, day);
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
    }
}
