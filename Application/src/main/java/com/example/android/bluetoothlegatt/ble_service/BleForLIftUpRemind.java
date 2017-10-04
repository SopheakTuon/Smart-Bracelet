package com.example.android.bluetoothlegatt.ble_service;

/**
 * @author Sopheak Tuon
 * @created on 04-Oct-17
 */

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class BleForLIftUpRemind extends BleBaseDataManage {
    public static final String TAG = "BleForLIftUpRemind";
    private static BleForLIftUpRemind bleForLostRemind = null;
    public static final byte fromDevice = (byte) -123;
    public static final byte toDevice = (byte) 5;
    private final int GET_SWITCH_STATUS = 0;
    private String ISOPENORNO = "is open or no";
    private final int SETTING_OPEN_OR_CLOSE = 1;
    private int count = 0;
    private boolean isAlreadyBack = false;
    private Handler mHandler = new C14691();
    private DataSendCallback readLostDataListener;

    class C14691 extends Handler {
        C14691() {
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (BleForLIftUpRemind.this.isAlreadyBack) {
                        BleForLIftUpRemind.this.closeSend(this);
                        return;
                    } else if (BleForLIftUpRemind.this.count < 4) {
                        BleForLIftUpRemind.this.continueSend(this, msg);
                        BleForLIftUpRemind.this.requestLiftUpData();
                        return;
                    } else {
                        BleForLIftUpRemind.this.closeSend(this);
                        return;
                    }
                case 1:
                    if (BleForLIftUpRemind.this.isAlreadyBack) {
                        BleForLIftUpRemind.this.closeSend(this);
                        return;
                    } else if (BleForLIftUpRemind.this.count < 4) {
                        BleForLIftUpRemind.this.continueSendOpen(this, msg);
                        boolean op = msg.getData().getBoolean(BleForLIftUpRemind.this.ISOPENORNO);
                        BleForLIftUpRemind.this.openAndClose(op);
                        BleForLIftUpRemind.this.openAndCloseRotateWrist(op);
                        return;
                    } else {
                        BleForLIftUpRemind.this.closeSend(this);
                        return;
                    }
                default:
                    return;
            }
        }
    }

    private BleForLIftUpRemind() {
    }

    public static BleForLIftUpRemind getInstance() {
        if (bleForLostRemind == null) {
            synchronized (BleForLIftUpRemind.class) {
                if (bleForLostRemind == null) {
                    bleForLostRemind = new BleForLIftUpRemind();
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

    public void requestLiftUpData() {
        int sendLg = requestLiftUpSwitch();
        Message msg = this.mHandler.obtainMessage();
        msg.what = 0;
        msg.arg1 = sendLg;
        msg.arg2 = 9;
        this.mHandler.sendMessageDelayed(msg, (long) SendLengthHelper.getSendLengthDelay(sendLg, 9));
    }

    private int requestLiftUpSwitch() {
        byte[] bates = new byte[]{(byte) 1, (byte) 6};
        return setMsgToByteDataAndSendToDevice((byte) 5, bates, bates.length);
    }

    public void openLiftUp(boolean open) {
        int sendLg = openAndClose(open);
        openAndCloseRotateWrist(open);
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
        bytes[1] = (byte) 6;
        if (open) {
            bytes[2] = (byte) 1;
        } else {
            bytes[2] = (byte) 0;
        }
        return setMsgToByteDataAndSendToDevice((byte) 5, bytes, bytes.length);
    }

    private int openAndCloseRotateWrist(boolean open) {
        byte[] bytes = new byte[3];
        bytes[0] = (byte) 0;
        bytes[1] = (byte) 7;
        if (open) {
            bytes[2] = (byte) 1;
        } else {
            bytes[2] = (byte) 0;
        }
        return setMsgToByteDataAndSendToDevice((byte) 5, bytes, bytes.length);
    }

    public void setLiftUpListener(DataSendCallback lsr) {
        this.readLostDataListener = lsr;
    }

    public void dealLiftUpResqonse(byte[] bufferTmp) {
        if (bufferTmp[0] == (byte) 1) {
            if (bufferTmp[1] == (byte) 6) {
                this.isAlreadyBack = true;
            }
        } else if (bufferTmp[0] == (byte) 0 && bufferTmp[1] == (byte) 6) {
            this.isAlreadyBack = true;
        }
        this.readLostDataListener.sendSuccess(bufferTmp);
    }
}

