package com.example.android.bluetoothlegatt.ble_service;

/**
 * @author Sopheak Tuon
 * @created on 04-Oct-17
 */

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class BleDataForCustomRemind extends BleBaseDataManage {
    public static final String TAG = BleDataForCustomRemind.class.getSimpleName();
    private static BleDataForCustomRemind bleDataForCustomRemind = null;
    public static final byte fromDevice = (byte) -119;
    public static final byte fromDeviceNull = (byte) -55;
    public static final byte toDevice = (byte) 9;
    private final int DELETE_CUSTOM_REMIND = 2;
    private final int GET_CUSTOM_EREMIND = 0;
    private final String REMIND_NUMBER = "remind number";
    private final int SETTINGS_CUSTOM_REMIND = 1;
    private DeleteCallback callback;
    private int count = 0;
    private Handler customRemindHandler = new C14471();
    byte[] hasData = new byte[8];
    private boolean isRemindDataBack = false;
    private boolean isSettingsOk = false;
    private int numberes = 0;
    private DataSendCallback requesCallback;
    private int settingCount = 0;

    class C14471 extends Handler {
        C14471() {
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (BleDataForCustomRemind.this.numberes >= 8) {
                        BleDataForCustomRemind.this.closeSendData(this);
                        if (BleDataForCustomRemind.this.requesCallback != null) {
                            BleDataForCustomRemind.this.requesCallback.sendFinished();
                            return;
                        }
                        return;
                    } else if (BleDataForCustomRemind.this.isRemindDataBack) {
                        BleDataForCustomRemind.this.continueSendNext(this, msg);
                        BleDataForCustomRemind.this.numberes = BleDataForCustomRemind.this.numberes + 1;
                        if (BleDataForCustomRemind.this.numberes < 8) {
                            BleDataForCustomRemind.this.getCustomRemind(BleDataForCustomRemind.this.numberes);
                            return;
                        }
                        return;
                    } else if (BleDataForCustomRemind.this.count < 4) {
                        BleDataForCustomRemind.this.continueSendThis(this, msg);
                        BleDataForCustomRemind.this.getCustomRemind(BleDataForCustomRemind.this.numberes);
                        return;
                    } else {
                        BleDataForCustomRemind.this.continueSendNext(this, msg);
                        BleDataForCustomRemind.this.numberes = BleDataForCustomRemind.this.numberes + 1;
                        BleDataForCustomRemind.this.getCustomRemind(BleDataForCustomRemind.this.numberes);
                        return;
                    }
                case 1:
                    if (BleDataForCustomRemind.this.isSettingsOk) {
                        BleDataForCustomRemind.this.stopSendSettingsData(this);
                        return;
                    } else if (BleDataForCustomRemind.this.settingCount < 4) {
                        BleDataForCustomRemind.this.continueSendSettinsData(this, msg);
                        byte[] data = msg.getData().getByteArray("settings");
                        BleDataForCustomRemind.this.setMsgToByteDataAndSendToDevice((byte) 9, data, data.length);
                        return;
                    } else {
                        BleDataForCustomRemind.this.stopSendSettingsData(this);
                        return;
                    }
                case 2:
                    if (BleDataForCustomRemind.this.isSettingsOk) {
                        BleDataForCustomRemind.this.stopDelete(this);
                        return;
                    } else if (BleDataForCustomRemind.this.settingCount < 4) {
                        BleDataForCustomRemind.this.continueSendDelete(this, msg);
                        BleDataForCustomRemind.this.deleteCustomRemind(msg.getData().getByte("number"));
                        return;
                    } else {
                        BleDataForCustomRemind.this.stopDelete(this);
                        return;
                    }
                default:
                    return;
            }
        }
    }

    public interface DeleteCallback {
        void deleteCallback(byte b);
    }

    private void continueSendDelete(Handler handler, Message msg) {
        Message mzg = handler.obtainMessage();
        mzg.what = msg.what;
        mzg.arg1 = msg.arg1;
        mzg.arg2 = msg.arg2;
        mzg.setData(msg.getData());
        handler.sendMessageDelayed(mzg, (long) SendLengthHelper.getSendLengthDelay(msg.arg1, msg.arg2));
    }

    private void stopDelete(Handler handler) {
        handler.removeMessages(2);
        this.isSettingsOk = false;
        this.settingCount = 0;
    }

    private void continueSendSettinsData(Handler handler, Message msg) {
        Message mzg = handler.obtainMessage();
        mzg.what = 1;
        mzg.arg1 = msg.arg1;
        mzg.arg2 = msg.arg2;
        mzg.setData(msg.getData());
        handler.sendMessageDelayed(mzg, (long) SendLengthHelper.getSendLengthDelay(msg.arg1, msg.arg2));
        this.settingCount++;
    }

    private void stopSendSettingsData(Handler handler) {
        handler.removeMessages(1);
        if (!this.isSettingsOk) {
            this.requesCallback.sendFailed();
        }
        this.requesCallback.sendFinished();
        this.isSettingsOk = false;
        this.settingCount = 0;
    }

    private void closeSendData(Handler handler) {
        handler.removeMessages(0);
        this.isRemindDataBack = false;
        this.count = 0;
        this.numberes = 0;
    }

    private void continueSendThis(Handler handler, Message mzg) {
        Message msg = handler.obtainMessage();
        msg.what = 0;
        msg.arg1 = mzg.arg1;
        msg.arg2 = mzg.arg2;
        handler.sendMessageDelayed(msg, (long) SendLengthHelper.getSendLengthDelay(mzg.arg1, mzg.arg2));
        this.count++;
    }

    private void continueSendNext(Handler handler, Message mzg) {
        Message msg = handler.obtainMessage();
        msg.what = 0;
        msg.arg1 = mzg.arg1;
        msg.arg2 = mzg.arg2;
        handler.sendMessageDelayed(msg, (long) SendLengthHelper.getSendLengthDelay(mzg.arg1, mzg.arg2));
        this.isRemindDataBack = false;
        this.count = 0;
    }

    public static BleDataForCustomRemind getCustomRemindDataInstance() {
        if (bleDataForCustomRemind == null) {
            synchronized (BleDataForCustomRemind.class) {
                if (bleDataForCustomRemind == null) {
                    bleDataForCustomRemind = new BleDataForCustomRemind();
                }
            }
        }
        return bleDataForCustomRemind;
    }

    private BleDataForCustomRemind() {
    }

    public void deletePx(byte number) {
        int deleLg = deleteCustomRemind(number);
        Message msg = this.customRemindHandler.obtainMessage();
        msg.what = 2;
        msg.arg1 = deleLg;
        msg.arg2 = 6;
        Bundle bundle = new Bundle();
        bundle.putByte("number", number);
        msg.setData(bundle);
        this.customRemindHandler.sendMessageDelayed(msg, (long) SendLengthHelper.getSendLengthDelay(deleLg, 6));
    }

    private int deleteCustomRemind(byte number) {
        byte[] bytes = new byte[]{(byte) 2, number};
        return setMsgToByteDataAndSendToDevice((byte) 9, bytes, bytes.length);
    }

    public void dealTheDeleteCallback(byte[] buffer) {
        this.callback.deleteCallback(buffer[0]);
    }

    public void closeSendData() {
        closeSendData(this.customRemindHandler);
    }

    public void getTheCustomRemind(int num) {
        int sendLg = getCustomRemind(num);
        Message msg = this.customRemindHandler.obtainMessage();
        msg.what = 0;
        msg.arg1 = sendLg;
        msg.arg2 = 25;
        Bundle bundle = new Bundle();
        bundle.putInt("remind number", num);
        msg.setData(bundle);
        this.customRemindHandler.sendMessageDelayed(msg, (long) SendLengthHelper.getSendLengthDelay(sendLg, 25));
    }

    public void setCustomRingSettings(byte[] data) {
        int sendLg = setMsgToByteDataAndSendToDevice((byte) 9, data, data.length);
        Message msg = this.customRemindHandler.obtainMessage();
        msg.what = 1;
        msg.arg1 = sendLg;
        msg.arg2 = 10;
        Bundle bu = new Bundle();
        bu.putByteArray("settings", data);
        msg.setData(bu);
        this.customRemindHandler.sendMessageDelayed(msg, (long) SendLengthHelper.getSendLengthDelay(sendLg, 10));
    }

    private int getCustomRemind(int number) {
        byte[] bytes = new byte[]{(byte) 0, (byte) (number & 255)};
        return setMsgToByteDataAndSendToDevice((byte) 9, bytes, bytes.length);
    }

    public void dealTheValidData(byte cmd, Context mContext, byte[] dataValid) {
        if (cmd != fromDevice || dataValid.length > 4) {
            this.isRemindDataBack = true;
        } else {
            this.isSettingsOk = true;
        }
        this.requesCallback.sendSuccess(dataValid);
    }

    public void setOnDeleteListener(DeleteCallback callback) {
        this.callback = callback;
    }

    public void setOnRequesCallback(DataSendCallback requesCallback) {
        this.requesCallback = requesCallback;
    }
}
