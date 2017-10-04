package com.example.android.bluetoothlegatt.ble_service;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

/**
 * @author Sopheak Tuon
 * @created on 04-Oct-17
 */

public class BleDataForBattery extends BleBaseDataManage {
    private static BleDataForBattery bleBAttery;
    public static byte fromCmd = (byte) -125;
    private static int mCurrentBattery = -1;
    public static byte mReceCmd = (byte) 3;
    private final int GET_BLE_BATTERY = 0;
    private DataSendCallback battListerer;
    private Handler batteryHandler = new C14461();
    private boolean hasComm = false;
    private boolean isSendOk = false;
    private int sendCount = 0;

    class C14461 extends Handler {
        C14461() {
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (BleDataForBattery.this.isSendOk) {
                BleDataForBattery.this.stopSendData(this);
            } else if (BleDataForBattery.this.sendCount < 4) {
                BleDataForBattery.this.continueSend(this, msg);
                BleDataForBattery.this.getBatteryFromBr();
            } else {
                BleDataForBattery.this.stopSendData(this);
            }
        }
    }

    public static BleDataForBattery getInstance() {
        if (bleBAttery == null) {
            synchronized (BleDataForBattery.class) {
                if (bleBAttery == null) {
                    bleBAttery = new BleDataForBattery();
                }
            }
        }
        return bleBAttery;
    }

    private void continueSend(Handler handler, Message msges) {
        Message msg = handler.obtainMessage();
        msg.arg1 = msges.arg1;
        msg.arg2 = msges.arg2;
        handler.sendMessageDelayed(msg, (long) SendLengthHelper.getSendLengthDelay(msges.arg1, msges.arg2));
        this.sendCount++;
    }

    private void stopSendData(Handler handler) {
        handler.removeMessages(0);
        if (!this.isSendOk) {
            this.battListerer.sendFailed();
        }
        this.battListerer.sendFinished();
        this.isSendOk = false;
        this.sendCount = 0;
    }

    private BleDataForBattery() {
    }

    public void getBatteryPx() {
        int sendLength = getBatteryFromBr();
        Message msg = this.batteryHandler.obtainMessage();
        msg.what = 0;
        msg.arg1 = sendLength;
        msg.arg2 = 7;
        this.batteryHandler.sendMessageDelayed(msg, (long) SendLengthHelper.getSendLengthDelay(sendLength, 7));
    }

    private int getBatteryFromBr() {
        return setMessageDataByString(mReceCmd, null, true);
    }

    public void dealReceData(Context mContext, byte[] data, int dataLength) {
        this.isSendOk = true;
        this.battListerer.sendSuccess(data);
    }

    public static int getmCurrentBattery() {
        return mCurrentBattery;
    }

    public void setBatteryListener(DataSendCallback batteryCallback) {
        this.battListerer = batteryCallback;
    }
}
