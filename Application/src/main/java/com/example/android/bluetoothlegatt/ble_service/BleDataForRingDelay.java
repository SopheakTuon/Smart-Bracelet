package com.example.android.bluetoothlegatt.ble_service;

/**
 * @author Sopheak Tuon
 * @created on 04-Oct-17
 */

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class BleDataForRingDelay extends BleBaseDataManage {
    public static final String TAG = BleDataForRingDelay.class.getSimpleName();
    public static final byte fromDevice = (byte) -110;
    private static BleDataForRingDelay mBleDataDelay = null;
    private static final byte toDevice = (byte) 18;
    private final int SEND_TO_GET_RING_DELAY = 0;
    private final int SETTING_DELAY_DATA = 1;
    private int count = 0;
    private boolean isBack = false;
    private DataSendCallback mRecever;
    private Handler ringHandler = new C14581();

    class C14581 extends Handler {
        C14581() {
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (BleDataForRingDelay.this.isBack) {
                        BleDataForRingDelay.this.closeSendData(this);
                        return;
                    } else if (BleDataForRingDelay.this.count < 4) {
                        BleDataForRingDelay.this.continueSendData(this, msg);
                        BleDataForRingDelay.this.sendToGetTheDealyData();
                        return;
                    } else {
                        BleDataForRingDelay.this.closeSendData(this);
                        return;
                    }
                case 1:
                    if (BleDataForRingDelay.this.isBack) {
                        BleDataForRingDelay.this.closeSetting(this);
                        return;
                    } else if (BleDataForRingDelay.this.count < 4) {
                        BleDataForRingDelay.this.continueSetting(this, msg);
                        BleDataForRingDelay.this.settingTheRingDelay(msg.arg1);
                        return;
                    } else {
                        BleDataForRingDelay.this.closeSetting(this);
                        return;
                    }
                default:
                    return;
            }
        }
    }

    private BleDataForRingDelay() {
    }

    private void continueSetting(Handler handler, Message mzg) {
        Message msg = Message.obtain();
        msg.what = 1;
        msg.arg1 = mzg.arg1;
        Bundle bundle = mzg.getData();
        msg.setData(bundle);
        handler.sendMessageDelayed(msg, (long) SendLengthHelper.getSendLengthDelay(bundle.getInt("send_length"), bundle.getInt("rece_length")));
    }

    private void closeSetting(Handler handler) {
        handler.removeMessages(1);
        this.isBack = false;
        this.count = 0;
    }

    private void continueSendData(Handler handler, Message mzg) {
        Message msg = handler.obtainMessage();
        msg.what = 0;
        msg.arg1 = mzg.arg1;
        msg.arg2 = mzg.arg2;
        handler.sendMessageDelayed(msg, (long) SendLengthHelper.getSendLengthDelay(mzg.arg1, mzg.arg2));
        this.count++;
    }

    private void closeSendData(Handler handler) {
        handler.removeMessages(0);
        if (this.mRecever != null) {
            if (!this.isBack) {
                this.mRecever.sendFailed();
            }
            this.mRecever.sendFinished();
        }
        this.isBack = false;
        this.count = 0;
    }

    public void addListener(DataSendCallback re) {
        this.mRecever = re;
    }

    public static BleDataForRingDelay getDelayInstance() {
        if (mBleDataDelay == null) {
            synchronized (BleDataForRingDelay.class) {
                if (mBleDataDelay == null) {
                    mBleDataDelay = new BleDataForRingDelay();
                }
            }
        }
        return mBleDataDelay;
    }

    public void getDelayData() {
        int sendLg = sendToGetTheDealyData();
        Message msg = this.ringHandler.obtainMessage();
        msg.what = 0;
        msg.arg1 = sendLg;
        msg.arg2 = 8;
        this.ringHandler.sendMessageDelayed(msg, (long) SendLengthHelper.getSendLengthDelay(sendLg, 8));
    }

    private int sendToGetTheDealyData() {
        byte[] data = new byte[]{(byte) 2};
        return setMsgToByteDataAndSendToDevice(toDevice, data, data.length);
    }

    public void dealTheDelayData(Context mContexts, byte[] bufferTmp) {
        this.isBack = true;
        if (bufferTmp[0] == (byte) 2 && this.mRecever != null) {
            this.mRecever.sendSuccess(bufferTmp);
        }
    }

    public void settingDelayData(int second) {
        int sendLg = settingTheRingDelay(second);
        Message msg = this.ringHandler.obtainMessage();
        msg.what = 1;
        msg.arg1 = second;
        Bundle bundle = new Bundle();
        bundle.putInt("send_length", sendLg);
        bundle.putInt("rece_length", 10);
        this.ringHandler.sendMessageDelayed(msg, (long) SendLengthHelper.getSendLengthDelay(sendLg, 10));
    }

    private int settingTheRingDelay(int second) {
        byte[] bytes = new byte[]{(byte) 1, (byte) (second & 255)};
        return setMsgToByteDataAndSendToDevice(toDevice, bytes, bytes.length);
    }
}
