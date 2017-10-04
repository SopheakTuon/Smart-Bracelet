package com.example.android.bluetoothlegatt.ble_service;

import android.os.Handler;
import android.os.Message;

/**
 * @author Sopheak Tuon
 * @created on 04-Oct-17
 */

public class BleDataForOnLineMovement extends BleBaseDataManage {
    public static final byte fromDevice = (byte) -122;
    private static BleDataForOnLineMovement onLineMovement = null;
    public static final byte toDevice = (byte) 6;
    private final int SEND_DATA_COMM = 0;
    private final int SEND_DATA_COMM_SWITCH = 1;
    public final String TAG = BleDataForOnLineMovement.class.getSimpleName();
    private boolean hasSendOneData = false;
    private boolean hasSendOneSwitch = false;
    private boolean isReceverData = false;
    private boolean isReceverSwitch = false;
    private Handler onLineHandler = new C14561();
    private DataSendCallback sendCallback;
    private int sendCount = 0;
    private int sendDataCount = 0;

    class C14561 extends Handler {
        C14561() {
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (BleDataForOnLineMovement.this.isReceverData) {
                        BleDataForOnLineMovement.this.closeSend(0, this, msg);
                        return;
                    } else if (BleDataForOnLineMovement.this.sendDataCount < 4) {
                        BleDataForOnLineMovement.this.onLineHRComm((byte) msg.arg2);
                        BleDataForOnLineMovement.this.continueSend(0, this, msg);
                        BleDataForOnLineMovement.this.sendDataCount = BleDataForOnLineMovement.this.sendDataCount + 1;
                        return;
                    } else {
                        BleDataForOnLineMovement.this.closeSend(0, this, msg);
                        BleDataForOnLineMovement.this.sendDataCount = 0;
                        BleDataForOnLineMovement.this.isReceverData = false;
                        return;
                    }
                case 1:
                    if (BleDataForOnLineMovement.this.isReceverSwitch) {
                        BleDataForOnLineMovement.this.closeSend(1, this, msg);
                        BleDataForOnLineMovement.this.isReceverSwitch = false;
                        BleDataForOnLineMovement.this.sendCount = 0;
                        return;
                    } else if (BleDataForOnLineMovement.this.sendCount < 4) {
                        BleDataForOnLineMovement.this.onLineHRComm((byte) msg.arg2);
                        BleDataForOnLineMovement.this.continueSend(1, this, msg);
                        BleDataForOnLineMovement.this.sendCount = BleDataForOnLineMovement.this.sendCount + 1;
                        return;
                    } else {
                        BleDataForOnLineMovement.this.closeSend(1, this, msg);
                        BleDataForOnLineMovement.this.isReceverSwitch = false;
                        BleDataForOnLineMovement.this.sendCount = 0;
                        return;
                    }
                default:
                    return;
            }
        }
    }

    private BleDataForOnLineMovement() {
    }

    public static BleDataForOnLineMovement getBleDataForOutlineInstance() {
        if (onLineMovement == null) {
            synchronized (BleDataForOnLineMovement.class) {
                if (onLineMovement == null) {
                    onLineMovement = new BleDataForOnLineMovement();
                }
            }
        }
        return onLineMovement;
    }

    private void continueSend(int send_data_comm, Handler handler, Message msg) {
        Message mzg = handler.obtainMessage();
        mzg.what = send_data_comm;
        mzg.arg1 = msg.arg1;
        mzg.arg2 = msg.arg2;
        handler.sendMessageDelayed(mzg, (long) SendLengthHelper.getSendLengthDelay(msg.arg1, 20));
    }

    private void closeSend(int send_data_comm, Handler handler, Message msg) {
        handler.removeMessages(send_data_comm);
        this.sendCallback.sendFinished();
    }

    public void sendHRDataToDevice(byte comm) {
        Message msg = this.onLineHandler.obtainMessage();
        int len = onLineHRComm(comm);
        msg.arg1 = len;
        msg.arg2 = comm;
        if (comm == (byte) 0) {
            this.hasSendOneData = true;
            msg.what = 0;
        } else {
            this.hasSendOneSwitch = true;
            msg.what = 1;
        }
        this.onLineHandler.sendMessageDelayed(msg, (long) SendLengthHelper.getSendLengthDelay(len, 200));
    }

    private int onLineHRComm(byte comm) {
        byte[] bytes = new byte[]{comm};
        return setMsgToByteDataAndSendToDevice((byte) 6, bytes, bytes.length);
    }

    public void dealOnlineHRMonitor(byte[] bufferTmp) {
        if (bufferTmp[0] == (byte) 0) {
            if (this.hasSendOneData) {
                this.isReceverData = true;
                this.hasSendOneData = false;
                this.sendCallback.sendSuccess(bufferTmp);
            }
        } else if (this.hasSendOneSwitch) {
            this.isReceverSwitch = true;
            this.hasSendOneSwitch = false;
            this.sendCallback.sendSuccess(bufferTmp);
        }
    }

    public void setOnSendRecever(DataSendCallback callback) {
        this.sendCallback = callback;
    }
}
