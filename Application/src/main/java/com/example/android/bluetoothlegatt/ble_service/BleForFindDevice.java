package com.example.android.bluetoothlegatt.ble_service;

/**
 * @author Sopheak Tuon
 * @created on 04-Oct-17
 */

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class BleForFindDevice extends BleBaseDataManage {
    public static byte fromDevice = (byte) -109;
    private static BleForFindDevice mBleForFindDevice;
    public static byte toDevice = (byte) 19;
    private final int FIND_DEVICE_INFO = 0;
    public final String TAG = BleForFindDevice.class.getSimpleName();
    private Handler findHandler = new C14671();
    private Boolean isSendOk = Boolean.valueOf(false);
    private Boolean isSendStart = Boolean.valueOf(false);
    private DataSendCallback onsendOk;
    private int sendConut = 0;

    class C14671 extends Handler {
        C14671() {
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (BleForFindDevice.this.isSendOk.booleanValue()) {
                        BleForFindDevice.this.stopFind(this);
                        return;
                    } else if (BleForFindDevice.this.sendConut < 4) {
                        BleForFindDevice.this.continueFind(this, msg);
                        BleForFindDevice.this.sendToStartFindDevice(msg.getData().getByte("comm"));
                        return;
                    } else {
                        BleForFindDevice.this.stopFind(this);
                        return;
                    }
                default:
                    return;
            }
        }
    }

    private BleForFindDevice() {
    }

    public static BleForFindDevice getBleForFindDeviceInstance() {
        if (mBleForFindDevice == null) {
            synchronized (BleForFindDevice.class) {
                if (mBleForFindDevice == null) {
                    mBleForFindDevice = new BleForFindDevice();
                }
            }
        }
        return mBleForFindDevice;
    }

    private void continueFind(Handler handler, Message msg) {
        Message mzg = handler.obtainMessage();
        mzg.what = 0;
        mzg.arg1 = msg.arg1;
        mzg.arg2 = msg.arg2;
        mzg.setData(msg.getData());
        handler.sendMessageDelayed(mzg, (long) SendLengthHelper.getSendLengthDelay(msg.arg1, msg.arg2));
        this.sendConut++;
    }

    private void stopFind(Handler handler) {
        handler.removeMessages(0);
        if (!this.isSendOk.booleanValue()) {
            this.onsendOk.sendFailed();
        }
        this.onsendOk.sendFinished();
        this.isSendOk = Boolean.valueOf(false);
        this.sendConut = 0;
    }

    public void findConnectedDevice(byte comm) {
        this.isSendStart = Boolean.valueOf(true);
        int sendLg = sendToStartFindDevice(comm);
        Message msg = this.findHandler.obtainMessage();
        msg.what = 0;
        msg.arg1 = sendLg;
        msg.arg2 = 20;
        Bundle bu = new Bundle();
        bu.putByte("comm", comm);
        msg.setData(bu);
        this.findHandler.sendMessageDelayed(msg, (long) SendLengthHelper.getSendLengthDelay(sendLg, 20));
    }

    private int sendToStartFindDevice(byte comm) {
        byte[] data = new byte[]{comm};
        return setMsgToByteDataAndSendToDevice(toDevice, data, data.length);
    }

    public void dealTheResponseData(byte[] backData) {
        if (this.isSendStart.booleanValue()) {
            this.isSendStart = Boolean.valueOf(false);
            this.isSendOk = Boolean.valueOf(true);
            if (backData[0] == (byte) 0 && this.onsendOk != null) {
                this.onsendOk.sendSuccess(backData);
            }
            if (backData[1] != (byte) 0) {
            }
        }
    }

    public void setListener(DataSendCallback listener) {
        this.onsendOk = listener;
    }
}
