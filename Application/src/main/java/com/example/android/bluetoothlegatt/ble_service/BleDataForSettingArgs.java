package com.example.android.bluetoothlegatt.ble_service;

/**
 * @author Sopheak Tuon
 * @created on 04-Oct-17
 */

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class BleDataForSettingArgs extends BleBaseDataManage {
    private static BleDataForSettingArgs bleDataForSettingArgs = null;
    public static final byte fromDevice = (byte) -126;
    private final int READ_HEART_AND_FATIGUE = 3;
    private final int SETTING_PARAMTER = 0;
    private final int SET_FATIGUE = 2;
    private final int SET_HEART_MONITOR = 1;
    private Context context;
    private int count = 0;
    private DataSendCallback dataSendCallback;
    private boolean hasComm = false;
    private boolean isAlreadyBack = false;
    private DataSendCallback listener;
    private Handler settingHandler = new C14591();
    private final byte toDevice = (byte) 2;

    class C14591 extends Handler {
        C14591() {
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (BleDataForSettingArgs.this.isAlreadyBack) {
                        BleDataForSettingArgs.this.closeSend(this);
                        return;
                    } else if (BleDataForSettingArgs.this.count < 4) {
                        BleDataForSettingArgs.this.couninueSend(this, msg);
                        Bundle bundle = msg.getData();
                        BleDataForSettingArgs.this.settingParamter(bundle.getString("unit"), bundle.getBoolean("is24"));
                        BleDataForSettingArgs.this.settingDateOrder(CountryUtils.getMonthAndDayFormate(), (byte) 5);
                        BleDataForSettingArgs.this.settingDateOrder(CountryUtils.getLanguageFormate(), (byte) 6);
                        return;
                    } else {
                        BleDataForSettingArgs.this.closeSend(this);
                        return;
                    }
                case 1:
                    if (BleDataForSettingArgs.this.isAlreadyBack) {
                        BleDataForSettingArgs.this.closeSendHeartMonitor(this);
                        return;
                    } else if (BleDataForSettingArgs.this.count < 4) {
                        BleDataForSettingArgs.this.continueSendHeartMonitor(this, msg);
                        BleDataForSettingArgs.this.setHeartReatMonnitor((byte) msg.arg1);
                        return;
                    } else {
                        BleDataForSettingArgs.this.closeSendHeartMonitor(this);
                        return;
                    }
                case 2:
                    if (BleDataForSettingArgs.this.isAlreadyBack) {
                        BleDataForSettingArgs.this.closeFatigue(this);
                        return;
                    } else if (BleDataForSettingArgs.this.count < 4) {
                        BleDataForSettingArgs.this.continueSendFatigue(this, msg);
                        BleDataForSettingArgs.this.setFatigue((byte) msg.arg1);
                        return;
                    } else {
                        BleDataForSettingArgs.this.closeFatigue(this);
                        return;
                    }
                case 3:
                    if (BleDataForSettingArgs.this.isAlreadyBack) {
                        BleDataForSettingArgs.this.closeRead(this);
                        return;
                    } else if (BleDataForSettingArgs.this.count < 4) {
                        BleDataForSettingArgs.this.conitnueRead(this, msg);
                        BleDataForSettingArgs.this.readFromDeviceHeartAndFatigue();
                        return;
                    } else {
                        BleDataForSettingArgs.this.closeRead(this);
                        return;
                    }
                default:
                    return;
            }
        }
    }

    private BleDataForSettingArgs(Context context) {
        this.context = context;
    }

    public static synchronized BleDataForSettingArgs getInstance(Context context) {
        BleDataForSettingArgs bleDataForSettingArgs = null;
        synchronized (BleDataForSettingArgs.class) {
            if (bleDataForSettingArgs == null) {
                bleDataForSettingArgs = new BleDataForSettingArgs(context);
            }
            bleDataForSettingArgs = bleDataForSettingArgs;
        }
        return bleDataForSettingArgs;
    }

    public void setDataSendCallback(DataSendCallback dataSendCallback) {
        this.dataSendCallback = dataSendCallback;
    }

    private void conitnueRead(Handler handler, Message mzg) {
        Message msg = handler.obtainMessage();
        msg.what = 3;
        msg.arg1 = mzg.arg1;
        msg.arg2 = mzg.arg2;
        handler.sendMessageDelayed(msg, (long) SendLengthHelper.getSendLengthDelay(mzg.arg1, mzg.arg2));
        this.count++;
    }

    private void closeRead(Handler handler) {
        handler.removeMessages(3);
        if (!this.isAlreadyBack) {
            this.listener.sendFailed();
        }
        this.listener.sendFinished();
        this.isAlreadyBack = false;
        this.count = 0;
    }

    private void continueSendFatigue(Handler handler, Message mzg) {
        Message msg = Message.obtain();
        msg.what = 2;
        msg.arg1 = mzg.arg1;
        msg.arg2 = mzg.arg2;
        handler.sendMessageDelayed(msg, (long) SendLengthHelper.getSendLengthDelay(mzg.arg2, 10));
        this.count++;
    }

    private void closeFatigue(Handler handler) {
        handler.removeMessages(2);
        this.isAlreadyBack = false;
        this.count = 0;
    }

    private void continueSendHeartMonitor(Handler handler, Message mzg) {
        Message msg = new Message();
        msg.what = 1;
        msg.arg1 = mzg.arg1;
        msg.arg2 = mzg.arg2;
        handler.sendMessageDelayed(msg, (long) SendLengthHelper.getSendLengthDelay(mzg.arg2, 10));
        this.count++;
    }

    private void closeSendHeartMonitor(Handler handler) {
        handler.removeMessages(1);
        this.isAlreadyBack = false;
        this.count = 0;
    }

    private void couninueSend(Handler handler, Message msges) {
        Message msg = handler.obtainMessage();
        msg.what = 0;
        msg.arg1 = msges.arg1;
        msg.arg2 = msges.arg2;
        msg.setData(msges.getData());
        handler.sendMessageDelayed(msg, (long) SendLengthHelper.getSendLengthDelay(msges.arg1, msges.arg2));
        this.count++;
    }

    private void closeSend(Handler handler) {
        handler.removeMessages(0);
        this.isAlreadyBack = false;
        this.hasComm = false;
        this.count = 0;
        if (this.dataSendCallback != null) {
            this.dataSendCallback.sendFinished();
        }
    }

    public void setArgs(String unit, boolean is24) {
        this.hasComm = true;
        int lenth = settingParamter(unit, is24);
        settingDateOrder(CountryUtils.getMonthAndDayFormate(), (byte) 5);
        settingDateOrder(CountryUtils.getLanguageFormate(), (byte) 6);
        Message msg = this.settingHandler.obtainMessage();
        msg.what = 0;
        msg.arg1 = lenth;
        msg.arg2 = 9;
        Bundle bundle = new Bundle();
        bundle.putString("unit", unit);
        bundle.putBoolean("is24", is24);
        msg.setData(bundle);
        this.settingHandler.sendMessageDelayed(msg, (long) SendLengthHelper.getSendLengthDelay(lenth, 9));
    }

    private int settingParamter(String unit, boolean is24) {
        byte[] bytes = new byte[5];
        bytes[0] = (byte) 1;
        bytes[1] = (byte) 0;
        if (is24) {
            bytes[2] = (byte) 1;
        } else {
            bytes[2] = (byte) 0;
        }
        bytes[3] = (byte) 1;
        if (unit == null || !unit.equals("inch")) {
            bytes[4] = (byte) 0;
        } else {
            bytes[4] = (byte) 1;
        }
        return setMsgToByteDataAndSendToDevice((byte) 2, bytes, bytes.length);
    }

    private int settingDateOrder(boolean isMonthDay, byte comm) {
        byte[] datas = new byte[3];
        datas[0] = (byte) 1;
        datas[1] = comm;
        if (isMonthDay) {
            datas[2] = (byte) 0;
        } else {
            datas[2] = (byte) 1;
        }
        return setMsgToByteDataAndSendToDevice((byte) 2, datas, datas.length);
    }

    public void setHeartReatArgs(byte time) {
        int sendLen = setHeartReatMonnitor(time);
        Message msg = new Message();
        msg.what = 1;
        msg.arg1 = time;
        msg.arg2 = sendLen;
        this.settingHandler.sendMessageDelayed(msg, (long) SendLengthHelper.getSendLengthDelay(sendLen, 9));
    }

    private int setHeartReatMonnitor(byte time) {
        byte[] heData = new byte[]{(byte) 1, (byte) 2, time};
        return setMsgToByteDataAndSendToDevice((byte) 2, heData, heData.length);
    }

    public void setFatigueSwich(byte open) {
        int sendLg = setFatigue(open);
        Message msg = new Message();
        msg.what = 2;
        msg.arg1 = open;
        msg.arg2 = sendLg;
        this.settingHandler.sendMessageDelayed(msg, (long) SendLengthHelper.getSendLengthDelay(sendLg, 9));
    }

    private int setFatigue(byte open) {
        byte[] heData = new byte[]{(byte) 1, (byte) 4, open};
        return setMsgToByteDataAndSendToDevice((byte) 2, heData, heData.length);
    }

    public void dealTheBack(byte[] bufferTmp) {
        Log.i("", "设置参数返回的：" + FormatUtils.bytesToHexString(bufferTmp));
        if (bufferTmp[0] == (byte) 1) {
            if (bufferTmp[1] == (byte) 2) {
                this.isAlreadyBack = true;
            } else if (bufferTmp[1] == (byte) 0 || bufferTmp[1] == (byte) 1) {
                if (this.hasComm) {
                    this.isAlreadyBack = true;
                    this.hasComm = false;
                }
            } else if (bufferTmp[1] == (byte) 4) {
                this.isAlreadyBack = true;
            }
        } else if (bufferTmp[0] == (byte) 0) {
            this.isAlreadyBack = true;
            this.listener.sendSuccess(bufferTmp);
        }
    }

    public void readHeartAndFatigue() {
        int lenglg = readFromDeviceHeartAndFatigue();
        Message msg = this.settingHandler.obtainMessage();
        msg.what = 3;
        msg.arg1 = lenglg;
        msg.arg2 = 11;
        this.settingHandler.sendMessageDelayed(msg, (long) SendLengthHelper.getSendLengthDelay(lenglg, 11));
    }

    private int readFromDeviceHeartAndFatigue() {
        byte[] readData = new byte[]{(byte) 0, (byte) 2, (byte) 4};
        return setMsgToByteDataAndSendToDevice((byte) 2, readData, readData.length);
    }

    public void setOnArgsBackListener(DataSendCallback dataSendCallback) {
        this.listener = dataSendCallback;
    }
}
