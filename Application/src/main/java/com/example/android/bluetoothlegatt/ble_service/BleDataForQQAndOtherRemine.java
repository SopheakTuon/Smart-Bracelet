package com.example.android.bluetoothlegatt.ble_service;

/**
 * @author Sopheak Tuon
 * @created on 04-Oct-17
 */

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class BleDataForQQAndOtherRemine extends BleBaseDataManage {
    public static final String TAG = BleDataForQQAndOtherRemine.class.getSimpleName();
    private static BleDataForQQAndOtherRemine bleDataForQQAndOtherRemine = null;
    public static final byte facebook = (byte) 3;
    public static final byte fromDevice = (byte) -117;
    public static final byte qq = (byte) 2;
    public static final byte skype = (byte) 4;
    public static final byte toDevice = (byte) 11;
    public static final byte twitter = (byte) 5;
    public static final byte weichart = (byte) 1;
    public static final byte whatsapp = (byte) 6;
    private final String SEND_CONTENT = "send content";
    private final int SEND_FACEBOOK_MESSAGE = 2;
    private final String SEND_NUMBER = "send number";
    private final int SEND_QQ_MESSANE = 0;
    private final int SEND_WEICHART_MESSAGE = 1;
    private int count = 0;
    private boolean isSendOk = false;
    private Handler remindHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bun;
            byte num;
            byte[] content;
            switch (msg.what) {
                case 0:
                    if (BleDataForQQAndOtherRemine.this.isSendOk) {
                        BleDataForQQAndOtherRemine.this.closeSendQQ(this);
                        return;
                    } else if (BleDataForQQAndOtherRemine.this.count < 4) {
                        bun = msg.getData();
                        num = bun.getByte("send number");
                        content = bun.getByteArray("send content");
                        BleDataForQQAndOtherRemine.this.countinueSendQQ(this, num, content, msg.arg1);
                        BleDataForQQAndOtherRemine.this.startRemindToDevice(num, content);
                        return;
                    } else {
                        BleDataForQQAndOtherRemine.this.closeSendQQ(this);
                        return;
                    }
                case 1:
                    if (BleDataForQQAndOtherRemine.this.isSendOk) {
                        BleDataForQQAndOtherRemine.this.closeSendWeiChart(this);
                        return;
                    } else if (BleDataForQQAndOtherRemine.this.count < 4) {
                        bun = msg.getData();
                        byte numWei = bun.getByte("send number");
                        byte[] contentWei = bun.getByteArray("send content");
                        BleDataForQQAndOtherRemine.this.countinueSendWeiChart(this, numWei, contentWei, msg.arg1);
                        BleDataForQQAndOtherRemine.this.startRemindToDevice(numWei, contentWei);
                        return;
                    } else {
                        BleDataForQQAndOtherRemine.this.closeSendWeiChart(this);
                        return;
                    }
                case 2:
                    if (BleDataForQQAndOtherRemine.this.isSendOk) {
                        BleDataForQQAndOtherRemine.this.closeSendFacebook(this);
                        return;
                    } else if (BleDataForQQAndOtherRemine.this.count < 4) {
                        bun = msg.getData();
                        num = bun.getByte("send number");
                        content = bun.getByteArray("send content");
                        BleDataForQQAndOtherRemine.this.countinueSendFacebook(this, num, content, msg.arg1);
                        BleDataForQQAndOtherRemine.this.startRemindToDevice(num, content);
                        return;
                    } else {
                        BleDataForQQAndOtherRemine.this.closeSendFacebook(this);
                        return;
                    }
                default:
                    return;
            }
        }
    };

    private BleDataForQQAndOtherRemine() {
    }

    public static synchronized BleDataForQQAndOtherRemine getIntance() {
        BleDataForQQAndOtherRemine bleDataForQQAndOtherRemine = null;
        synchronized (BleDataForQQAndOtherRemine.class) {
            if (bleDataForQQAndOtherRemine == null) {
                bleDataForQQAndOtherRemine = new BleDataForQQAndOtherRemine();
            }
            bleDataForQQAndOtherRemine = bleDataForQQAndOtherRemine;
        }
        return bleDataForQQAndOtherRemine;
    }

    private void countinueSendFacebook(Handler handler, byte num, byte[] name, int delayTime) {
        Message msg = Message.obtain();
        msg.what = 2;
        Bundle bu = new Bundle();
        bu.putByte("send number", num);
        bu.putByteArray("send content", name);
        msg.setData(bu);
        msg.arg1 = delayTime;
        handler.sendMessageDelayed(msg, (long) delayTime);
        this.count++;
    }

    private void closeSendFacebook(Handler handler) {
        handler.removeMessages(2);
        this.isSendOk = false;
        this.count = 0;
    }

    private void countinueSendWeiChart(Handler handler, byte num, byte[] name, int delayTime) {
        Message msg = Message.obtain();
        msg.what = 1;
        Bundle bu = new Bundle();
        bu.putByte("send number", num);
        bu.putByteArray("send content", name);
        msg.setData(bu);
        msg.arg1 = delayTime;
        handler.sendMessageDelayed(msg, (long) delayTime);
        this.count++;
    }

    private void closeSendWeiChart(Handler handler) {
        handler.removeMessages(1);
        this.isSendOk = false;
        this.count = 0;
    }

    private void countinueSendQQ(Handler handler, byte num, byte[] name, int delayTime) {
        Message msg = Message.obtain();
        msg.what = 0;
        Bundle bu = new Bundle();
        bu.putByte("send number", num);
        bu.putByteArray("send content", name);
        msg.setData(bu);
        msg.what = delayTime;
        handler.sendMessageDelayed(msg, (long) delayTime);
        this.count++;
    }

    private void closeSendQQ(Handler handler) {
        handler.removeMessages(0);
        this.isSendOk = false;
        this.count = 0;
    }

    public void sendMessageToDevice(byte num, byte[] msg) {
        int remindLength = startRemindToDevice(num, msg);
        Message msgs = this.remindHandler.obtainMessage();
        Bundle bun = new Bundle();
        bun.putByte("send number", num);
        bun.putByteArray("send content", msg);
        msgs.setData(bun);
        msgs.arg1 = CommonStatusCodes.AUTH_API_INVALID_CREDENTIALS;
        if (num == (byte) 1) {
            msgs.what = 1;
        } else if (num == (byte) 2) {
            msgs.what = 0;
        } else if (num == (byte) 3) {
            msgs.what = 2;
        } else {
            msgs.what = 1;
        }
        this.remindHandler.sendMessageDelayed(msgs, (long) 3000);
    }

    private int getRandom() {
        return (int) (24.0d + (Math.random() * 76.0d));
    }

    private int startRemindToDevice(byte num, byte[] msg) {
        byte[] data = new byte[(msg.length + 1)];
        System.arraycopy(msg, 0, data, 1, msg.length);
        data[0] = num;
        return setMsgToByteDataAndSendToDevice((byte) 11, data, data.length);
    }

    public void dealRemindResponse(byte[] bufferTmp) {
        this.isSendOk = true;
    }
}
