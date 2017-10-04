package com.example.android.bluetoothlegatt.ble_service;

/**
 * @author Sopheak Tuon
 * @created on 04-Oct-17
 */

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class BleDataForTarget extends BleBaseDataManage {
    public static final byte fromDevice = (byte) -83;
    private static BleDataForTarget mBleDataForTarget = null;
    public static final byte toDevice = (byte) 45;
    private final int SEND_TARGET_TO_DEVICE = 0;
    private Handler handler = new C14621();
    private boolean isSendOK = false;
    private boolean sendActive = false;
    private DataSendCallback sendCallback;
    private int sendCount;
    private final String sleepHours = "sleepHour";
    private final String sleepMinutes = "sleepMinute";
    private final String sleepTargets = "sleepTarget";
    private final String stepTargets = "stepTarget";

    class C14621 extends Handler {
        C14621() {
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (BleDataForTarget.this.isSendOK) {
                        BleDataForTarget.this.closeSend(this);
                        return;
                    } else if (BleDataForTarget.this.sendCount < 4) {
                        BleDataForTarget.this.continueSend(this, msg);
                        Bundle bun = msg.getData();
                        BleDataForTarget.this.sendTargetTo(bun.getInt("stepTarget"), bun.getInt("sleepTarget"), bun.getInt("sleepHour"), bun.getInt("sleepMinute"));
                        return;
                    } else {
                        BleDataForTarget.this.closeSend(this);
                        return;
                    }
                default:
                    return;
            }
        }
    }

    private BleDataForTarget() {
    }

    public static BleDataForTarget getInstance() {
        if (mBleDataForTarget == null) {
            synchronized (BleDataForTarget.class) {
                if (mBleDataForTarget == null) {
                    mBleDataForTarget = new BleDataForTarget();
                }
            }
        }
        return mBleDataForTarget;
    }

    private void continueSend(Handler handler, Message msg) {
        Message mzg = handler.obtainMessage();
        mzg.what = msg.what;
        mzg.setData(msg.getData());
        handler.sendMessageDelayed(mzg, 300);
        this.sendCount++;
    }

    private void closeSend(Handler handler) {
        handler.removeMessages(0);
        this.sendCount = 0;
        this.isSendOK = false;
    }

    public void sendTargetToDevice(int stepTarget, int sleepTimes, int sleepHour, int sleepMinute) {
        this.sendActive = true;
        sendTargetTo(stepTarget, sleepTimes, sleepHour, sleepMinute);
        Message msg = this.handler.obtainMessage();
        msg.what = 0;
        Bundle bundle = new Bundle();
        bundle.putInt("stepTarget", stepTarget);
        bundle.putInt("sleepTarget", sleepTimes);
        bundle.putInt("sleepHour", sleepHour);
        bundle.putInt("sleepMinute", sleepMinute);
        msg.setData(bundle);
        this.handler.sendMessageDelayed(msg, 300);
    }

    public void sendTargetTo(int stepTarget, int sleepTimes, int sleepHour, int sleepMinute) {
        byte[] datas = new byte[11];
        datas[0] = (byte) 2;
        byte[] steps = FormatUtils.int2Byte_HL_(stepTarget);
        System.arraycopy(steps, 0, datas, 1, steps.length);
        byte[] sleeps = FormatUtils.int2Byte_HL_(sleepTimes);
        System.arraycopy(sleeps, 0, datas, 5, sleeps.length);
        datas[9] = (byte) sleepMinute;
        datas[10] = (byte) sleepHour;
        setMsgToByteDataAndSendToDevice(toDevice, datas, datas.length);
    }

    public void dealComm(byte[] buffTmp) {
        if (buffTmp[0] != (byte) 1) {
            return;
        }
        if (this.sendActive) {
            this.sendActive = false;
            this.isSendOK = true;
        } else if (this.sendCount <= 0) {
            this.sendCallback.sendSuccess(null);
        }
    }

    public void setSendCallback(DataSendCallback callback) {
        this.sendCallback = callback;
    }
}
