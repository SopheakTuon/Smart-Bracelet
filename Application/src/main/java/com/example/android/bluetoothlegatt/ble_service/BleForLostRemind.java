package com.example.android.bluetoothlegatt.ble_service;

/**
 * @author Sopheak Tuon
 * @created on 04-Oct-17
 */

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class BleForLostRemind extends BleBaseDataManage {
    private static BleForLostRemind bleForLostRemind = null;
    public static final byte fromDevice = (byte) -123;
    public static final byte toDevice = (byte) 5;
    private final int GET_SWITCH_STATUS = 0;
    private String ISOPENORNO = "is open or no";
    private final int SETTING_OPEN_OR_CLOSE = 1;
    private int count = 0;
    private boolean isAlreadyBack = false;
    private Handler mHandler = new C14701();
    private DataSendCallback readLostDataListener;

    class C14701 extends Handler {
        C14701() {
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (BleForLostRemind.this.isAlreadyBack) {
                        BleForLostRemind.this.closeSend(this);
                        return;
                    } else if (BleForLostRemind.this.count < 4) {
                        BleForLostRemind.this.continueSend(this, msg);
                        BleForLostRemind.this.requestLostSwitch();
                        return;
                    } else {
                        BleForLostRemind.this.closeSend(this);
                        return;
                    }
                case 1:
                    if (BleForLostRemind.this.isAlreadyBack) {
                        BleForLostRemind.this.closeSend(this);
                        return;
                    } else if (BleForLostRemind.this.count < 4) {
                        BleForLostRemind.this.continueSendOpen(this, msg);
                        BleForLostRemind.this.openAndClose(msg.getData().getBoolean(BleForLostRemind.this.ISOPENORNO));
                        return;
                    } else {
                        BleForLostRemind.this.closeSend(this);
                        return;
                    }
                default:
                    return;
            }
        }
    }

    private BleForLostRemind() {
    }

    public static BleForLostRemind getInstance() {
        if (bleForLostRemind == null) {
            synchronized (BleForLostRemind.class) {
                if (bleForLostRemind == null) {
                    bleForLostRemind = new BleForLostRemind();
                }
            }
        }
        return bleForLostRemind;
    }

    private void continueSendOpen(Handler handler, Message msg) {
        Message msgIn = handler.obtainMessage();
        msgIn.what = 1;
        msgIn.arg1 = msg.arg1;
        msgIn.arg2 = msg.arg2;
        msgIn.setData(msg.getData());
        handler.sendMessageDelayed(msgIn, (long) SendLengthHelper.getSendLengthDelay(msg.arg1, msg.arg2));
    }

    private void continueSend(Handler handler, Message mzg) {
        Message msg = handler.obtainMessage();
        msg.what = 0;
        msg.arg1 = mzg.arg1;
        msg.arg2 = mzg.arg2;
        handler.sendMessageDelayed(msg, (long) SendLengthHelper.getSendLengthDelay(mzg.arg1, mzg.arg2));
        this.count++;
    }

    private void closeSend(Handler handler) {
        handler.removeMessages(0);
        this.isAlreadyBack = false;
        this.count = 0;
    }

    public void requestAndHandler() {
        int sendLg = requestLostSwitch();
        Message msg = this.mHandler.obtainMessage();
        msg.what = 0;
        msg.arg1 = sendLg;
        msg.arg2 = 9;
        this.mHandler.sendMessageDelayed(msg, (long) SendLengthHelper.getSendLengthDelay(sendLg, 9));
    }

    private int requestLostSwitch() {
        byte[] bates = new byte[]{(byte) 1, (byte) 1};
        return setMsgToByteDataAndSendToDevice((byte) 5, bates, bates.length);
    }

    public void openAndHandler(boolean open) {
        int sendLg = openAndClose(open);
        Message msg = Message.obtain();
        msg.what = 1;
        msg.arg1 = sendLg;
        msg.arg2 = 8;
        Bundle bundle = new Bundle();
        bundle.putBoolean(this.ISOPENORNO, open);
        msg.setData(bundle);
        this.mHandler.sendMessageDelayed(msg, (long) SendLengthHelper.getSendLengthDelay(sendLg, 8));
    }

    private int openAndClose(boolean open) {
        byte[] bytes = new byte[3];
        bytes[0] = (byte) 0;
        bytes[1] = (byte) 1;
        if (open) {
            bytes[2] = (byte) 1;
        } else {
            bytes[2] = (byte) 0;
        }
        return setMsgToByteDataAndSendToDevice((byte) 5, bytes, bytes.length);
    }

    public void setLostListener(DataSendCallback lsr) {
        this.readLostDataListener = lsr;
    }

    public void dealTheLostResqonse(byte[] bufferTmp) {
        if (bufferTmp[0] == (byte) 1) {
            if (bufferTmp[1] == (byte) 1) {
                this.isAlreadyBack = true;
            }
        } else if (bufferTmp[0] == (byte) 0 && bufferTmp[1] == (byte) 1) {
            this.isAlreadyBack = true;
        }
        this.readLostDataListener.sendSuccess(bufferTmp);
    }
}
