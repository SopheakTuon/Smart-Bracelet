package com.example.android.bluetoothlegatt.ble_service;

/**
 * @author Sopheak Tuon
 * @created on 04-Oct-17
 */

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class BleReadDeviceMenuState extends BleBaseDataManage {
    private static final int OPEN_OR_CLOSE = 1;
    private static BleReadDeviceMenuState bleReadDeviceMenuState = null;
    public static final byte fromDevice = (byte) -84;
    public static final byte toDevice = (byte) 44;
    private int count;
    DevicemenuCallback devicemenuCallback;
    private boolean isBack;
    Handler mHandler = new C14751();

    class C14751 extends Handler {
        C14751() {
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (BleReadDeviceMenuState.this.isBack) {
                BleReadDeviceMenuState.this.closeSendOpenOrClose(this);
            } else if (BleReadDeviceMenuState.this.count < 4) {
                BleReadDeviceMenuState.this.continueSendOpenOrClose(this, msg);
                BleReadDeviceMenuState.this.sendData((byte) msg.arg1, (byte) msg.arg2);
            } else {
                BleReadDeviceMenuState.this.closeSendOpenOrClose(this);
            }
        }
    }

    public interface DevicemenuCallback {
        void onGEtCharArray(byte[] bArr);
    }

    private BleReadDeviceMenuState() {
    }

    public static synchronized BleReadDeviceMenuState getInstance() {
        BleReadDeviceMenuState bleReadDeviceMenuState = null;
        synchronized (BleReadDeviceMenuState.class) {
            if (bleReadDeviceMenuState == null) {
                bleReadDeviceMenuState = new BleReadDeviceMenuState();
            }
            bleReadDeviceMenuState = bleReadDeviceMenuState;
        }
        return bleReadDeviceMenuState;
    }

    public void sendUpdateSwitchData(byte code) {
        geistate((byte) toDevice, new byte[]{(byte) 1, code, (byte) 0, (byte) 0, (byte) 0});
    }

    public void sendUpdateSwitchData32(int allData) {
        byte[] swich = new byte[5];
        swich[0] = (byte) 1;
        byte[] d = FormatUtils.int2Byte_HL_(allData);
        System.arraycopy(d, 0, swich, 1, d.length);
        geistate((byte) toDevice, swich);
    }

    private void closeSendOpenOrClose(Handler handler) {
        handler.removeMessages(1);
        this.isBack = false;
        this.count = 0;
    }

    public void geistate(byte code, byte content) {
        int sendLg = sendData(code, content);
        Message msg = Message.obtain();
        msg.what = 1;
        msg.arg1 = code;
        msg.arg2 = content;
        Bundle bundle = new Bundle();
        bundle.putInt("send_length", sendLg);
        bundle.putInt("rece_length", 8);
        msg.setData(bundle);
        this.mHandler.sendMessageDelayed(msg, (long) SendLengthHelper.getSendLengthDelay(sendLg, 8));
    }

    public void geistate(byte code, byte[] content) {
        int sendLg = sendData(code, content);
        Message msg = Message.obtain();
        msg.what = 1;
        msg.arg1 = code;
        msg.arg2 = content[0];
        Bundle bundle = new Bundle();
        bundle.putInt("send_length", sendLg);
        bundle.putInt("rece_length", 8);
        msg.setData(bundle);
        this.mHandler.sendMessageDelayed(msg, (long) SendLengthHelper.getSendLengthDelay(sendLg, 8));
    }

    public int sendData(byte controlCode, byte content) {
        byte[] sendData = new byte[]{content};
        return setMsgToByteDataAndSendToDevice(controlCode, sendData, sendData.length);
    }

    public int sendData(byte controlCode, byte[] content) {
        return setMsgToByteDataAndSendToDevice(controlCode, content, content.length);
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

    public void sendSuccess(byte[] receveData) {
        this.isBack = true;
        if (receveData[0] != (byte) 1) {
            this.devicemenuCallback.onGEtCharArray(receveData);
        }
    }

    public String formattingH(int a) {
        String i = String.valueOf(a);
        switch (a) {
            case 10:
                return "a";
            case 11:
                return "b";
            case 12:
                return "c";
            case 13:
                return "d";
            case 14:
                return "e";
            case 15:
                return "f";
            default:
                return i;
        }
    }

    public void setResultlistener(DevicemenuCallback resultlistener) {
        this.devicemenuCallback = resultlistener;
    }
}
