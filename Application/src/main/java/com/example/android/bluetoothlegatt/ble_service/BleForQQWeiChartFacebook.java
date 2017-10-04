package com.example.android.bluetoothlegatt.ble_service;

/**
 * @author Sopheak Tuon
 * @created on 04-Oct-17
 */

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class BleForQQWeiChartFacebook extends BleBaseDataManage {
    public static final String TAG = BleForQQWeiChartFacebook.class.getSimpleName();
    private static BleForQQWeiChartFacebook bleForQQWeiChartFacebook = null;
    public static final byte readFromDevice = (byte) -123;
    public static final byte readToDevice = (byte) 5;
    private final int CHECK_INFO_TYPE = 2;
    private final int OPEN_OR_CLOSE = 1;
    private Handler QQHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (BleForQQWeiChartFacebook.this.isBack) {
                        BleForQQWeiChartFacebook.this.closeSend(this);
                        return;
                    } else if (BleForQQWeiChartFacebook.this.count < 4) {
                        BleForQQWeiChartFacebook.this.continueSend(this, msg);
                        BleForQQWeiChartFacebook.this.readSwitchFromDevice();
                        return;
                    } else {
                        BleForQQWeiChartFacebook.this.closeSend(this);
                        return;
                    }
                case 1:
                    if (BleForQQWeiChartFacebook.this.isBack) {
                        BleForQQWeiChartFacebook.this.closeSendOpenOrClose(this);
                        return;
                    } else if (BleForQQWeiChartFacebook.this.count < 4) {
                        BleForQQWeiChartFacebook.this.continueSendOpenOrClose(this, msg);
                        BleForQQWeiChartFacebook.this.openQQWeiChartFacebook((byte) msg.arg1, (byte) msg.arg2);
                        return;
                    } else {
                        BleForQQWeiChartFacebook.this.closeSendOpenOrClose(this);
                        return;
                    }
                case 2:
                    if (BleForQQWeiChartFacebook.this.isBack) {
                        BleForQQWeiChartFacebook.this.closeCheck(this, msg);
                        return;
                    } else if (BleForQQWeiChartFacebook.this.count < 3) {
                        BleForQQWeiChartFacebook.this.checkInfoType();
                        BleForQQWeiChartFacebook.this.conitnueCheck(this, msg);
                        return;
                    } else {
                        BleForQQWeiChartFacebook.this.closeCheck(this, msg);
                        return;
                    }
                default:
                    return;
            }
        }
    };
    private final int READ_SWITCH = 0;
    private int count = 0;
    private boolean isBack = false;
    private DataSendCallback onCheckBack;
    private DataSendCallback onDataBack;

    private BleForQQWeiChartFacebook() {
    }

    public static synchronized BleForQQWeiChartFacebook getInstance() {
        BleForQQWeiChartFacebook bleForQQWeiChartFacebook = null;
        synchronized (BleForQQWeiChartFacebook.class) {
            if (bleForQQWeiChartFacebook == null) {
                bleForQQWeiChartFacebook = new BleForQQWeiChartFacebook();
            }
            bleForQQWeiChartFacebook = bleForQQWeiChartFacebook;
        }
        return bleForQQWeiChartFacebook;
    }

    private void conitnueCheck(Handler handler, Message msg) {
        handler.sendEmptyMessageDelayed(2, 500);
        this.count++;
    }

    private void closeCheck(Handler handler, Message msg) {
        handler.removeMessages(2);
        this.isBack = false;
        this.count = 0;
    }

    private void continueSendOpenOrClose(Handler handler, Message mzg) {
        Message msg = Message.obtain();
        msg.what = 1;
        msg.arg1 = mzg.arg1;
        msg.arg2 = mzg.arg2;
        Bundle bundle = mzg.getData();
        msg.setData(bundle);
        handler.sendMessageDelayed(msg, (long) SendLengthHelper.getSendLengthDelay(bundle.getInt("send_length"), bundle.getInt("rece_length")));
        this.count++;
    }

    private void closeSendOpenOrClose(Handler handler) {
        handler.removeMessages(1);
        this.isBack = false;
        this.count = 0;
    }

    private void continueSend(Handler handler, Message msg) {
        Message mzg = handler.obtainMessage();
        mzg.what = 0;
        mzg.arg1 = msg.arg1;
        mzg.arg2 = msg.arg2;
        handler.sendMessageDelayed(mzg, (long) SendLengthHelper.getSendLengthDelay(msg.arg1, msg.arg2));
        this.count++;
    }

    private void closeSend(Handler handler) {
        handler.removeMessages(0);
        this.isBack = false;
        this.count = 0;
    }

    public void checkInfoType() {
        checkData();
        Message msg = this.QQHandler.obtainMessage();
        msg.what = 2;
        this.QQHandler.sendMessageDelayed(msg, 500);
    }

    private void checkData() {
        byte[] data = new byte[]{(byte) 2, BleDataForWeather.toDevice};
        setMsgToByteDataAndSendToDevice((byte) 5, data, data.length);
    }

    public void readSwitch() {
        int readLg = readSwitchFromDevice();
        Message msg = this.QQHandler.obtainMessage();
        msg.what = 0;
        msg.arg1 = readLg;
        msg.arg2 = 21;
        this.QQHandler.sendMessageDelayed(msg, (long) SendLengthHelper.getSendLengthDelay(readLg, 21));
    }

    private int readSwitchFromDevice() {
        byte[] bytes = new byte[]{(byte) 1, (byte) 2, (byte) 9, (byte) 10, (byte) 11, (byte) 12, BleDataForTakePhoto.startToDevice, BleDataForTakePhoto.takeToDevice};
        return setMsgToByteDataAndSendToDevice((byte) 5, bytes, bytes.length);
    }

    public void dealTheResuponse(byte[] bufferTmp) {
        if (bufferTmp[1] == (byte) 2) {
            this.isBack = true;
            this.onDataBack.sendSuccess(bufferTmp);
        }
    }

    public void setOnDeviceDataBack(DataSendCallback back) {
        this.onDataBack = back;
    }

    public void setOnDeviceCheckBack(DataSendCallback backs) {
        this.onCheckBack = backs;
    }

    public void openRemind(byte num, byte swich) {
        int sendLg = openQQWeiChartFacebook(num, swich);
        Message msg = Message.obtain();
        msg.what = 1;
        msg.arg1 = num;
        msg.arg2 = swich;
        Bundle bundle = new Bundle();
        bundle.putInt("send_length", sendLg);
        bundle.putInt("rece_length", 8);
        msg.setData(bundle);
        this.QQHandler.sendMessageDelayed(msg, (long) SendLengthHelper.getSendLengthDelay(sendLg, 8));
    }

    public int openQQWeiChartFacebook(byte code, byte witch) {
        byte[] bytes = new byte[]{(byte) 0, code, witch};
        return setMsgToByteDataAndSendToDevice((byte) 5, bytes, bytes.length);
    }

    public void dealOpenOrCloseRequese(byte[] bufferTmp) {
        this.isBack = true;
        if (bufferTmp[0] == (byte) 2 && bufferTmp[1] == BleDataForWeather.toDevice && this.onCheckBack != null) {
            this.onCheckBack.sendSuccess(bufferTmp);
        }
    }
}
