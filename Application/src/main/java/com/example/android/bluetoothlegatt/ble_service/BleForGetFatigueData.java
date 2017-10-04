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

public class BleForGetFatigueData extends BleBaseDataManage {
    private static BleForGetFatigueData bleForGetFatigueData = null;
    public static final byte exceptionDevice = (byte) -27;
    public static final byte fromDevice = (byte) -91;
    private final int GET_FATIGUE_DATA = 0;
    private final String TAG = BleForGetFatigueData.class.getSimpleName();
    private Context context;
    private int count = 0;
    private String currentDate = null;
    private DataSendCallback dataSendCallback;
    private int day;
    private onDeviceException exception;
    private boolean hasComm = false;
    private boolean isBack = false;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (BleForGetFatigueData.this.isBack) {
                        BleForGetFatigueData.this.closeSendData(this);
                        return;
                    } else if (BleForGetFatigueData.this.count < 4) {
                        BleForGetFatigueData.this.continueSendGetData(this, msg);
                        BleForGetFatigueData.this.GetFatigueData();
                        return;
                    } else {
                        BleForGetFatigueData.this.closeSendData(this);
                        return;
                    }
                default:
                    return;
            }
        }
    };
    private int month;
    private final byte toDevice = (byte) 37;
    private int year;

    public interface onDeviceException {
        void onException();
    }

    private BleForGetFatigueData(Context context) {
        this.context = context;
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        this.year = calendar.get(Calendar.YEAR) - 2000;
        this.month = calendar.get(Calendar.MONTH) + 1;
        this.day = calendar.get(Calendar.DAY_OF_MONTH);
        this.currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
    }

    private void continueSendGetData(Handler handler, Message msg) {
        Message mzg = handler.obtainMessage();
        mzg.what = 0;
        mzg.arg1 = msg.arg1;
        mzg.arg2 = msg.arg2;
        handler.sendMessageDelayed(mzg, (long) SendLengthHelper.getSendLengthDelay(msg.arg1, msg.arg2));
        this.count++;
    }

    private void closeSendData(Handler handler) {
        handler.removeMessages(0);
        if (this.dataSendCallback != null) {
            if (!this.isBack) {
                this.dataSendCallback.sendFailed();
            }
            this.dataSendCallback.sendFinished();
        }
        this.hasComm = false;
        this.isBack = false;
        this.count = 0;
    }

    public static BleForGetFatigueData getInstance(Context context) {
        if (bleForGetFatigueData == null) {
            synchronized (BleForGetFatigueData.class) {
                if (bleForGetFatigueData == null) {
                    bleForGetFatigueData = new BleForGetFatigueData(context);
                }
            }
        }
        return bleForGetFatigueData;
    }

    public void setDataSendCallback(DataSendCallback dataSendCallback) {
        this.dataSendCallback = dataSendCallback;
    }

    public void getFatigueDayData() {
        this.hasComm = true;
        int sendLg = GetFatigueData();
        Message msg = this.mHandler.obtainMessage();
        msg.what = 0;
        msg.arg1 = sendLg;
        msg.arg2 = 33;
        this.mHandler.sendMessageDelayed(msg, (long) SendLengthHelper.getSendLengthDelay(sendLg, 33));
    }

    private int GetFatigueData() {
        byte[] by = new byte[]{(byte) this.day, (byte) this.month, (byte) this.year};
        return setMsgToByteDataAndSendToDevice((byte) 37, by, by.length);
    }

    public void dealTheResbonseData(byte[] bufferTmp) {
        if (this.hasComm) {
            this.isBack = true;
            this.hasComm = false;
            if (this.dataSendCallback != null) {
                this.dataSendCallback.sendSuccess(bufferTmp);
            } else {
                Log.e(this.TAG, "BleForGetFatigueData 为空");
            }
        }
        int day = bufferTmp[0] & 255;
        int month = (bufferTmp[1] & 255) - 1;
        int year = (bufferTmp[2] & 255) + 2000;
        Calendar calend = Calendar.getInstance(TimeZone.getDefault());
        calend.set(year, month, day);
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calend.getTime());
        if (date != null && !date.equals(this.currentDate)) {
            Log.i(this.TAG, "是否是当天数据" + date + "--" + this.currentDate);
            backToDevice(bufferTmp);
            if (this.dataSendCallback != null) {
                this.dataSendCallback.sendSuccess(bufferTmp);
            } else {
                Log.e(this.TAG, "BleForGetFatigueData 为空");
            }
        }
    }

    private void backToDevice(byte[] bufferTmp) {
        byte[] backData = new byte[3];
        System.arraycopy(bufferTmp, 0, backData, 0, 3);
        setMsgToByteDataAndSendToDevice((byte) 37, backData, backData.length);
    }

    public void dealException() {
        if (this.hasComm) {
            this.isBack = true;
            this.hasComm = false;
        }
        if (this.exception != null) {
            this.exception.onException();
        }
    }

    public void setOnDeviceException(onDeviceException exception) {
        this.exception = exception;
    }
}
