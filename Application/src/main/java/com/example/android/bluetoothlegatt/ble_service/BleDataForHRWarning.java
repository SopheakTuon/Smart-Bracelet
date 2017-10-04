package com.example.android.bluetoothlegatt.ble_service;

/**
 * @author Sopheak Tuon
 * @created on 04-Oct-17
 */

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class BleDataForHRWarning extends BleBaseDataManage {
    private static final String TAG = BleDataForHRWarning.class.getSimpleName();
    private static BleDataForHRWarning bleDataForHRWarning = null;
    public static final byte fromDevice = (byte) -112;
    public static final byte toDevice = (byte) 16;
    private final int CLOSE_OR_OPEN_WARNING = 2;
    private final int GET_WARNING_DATA = 0;
    private int closeOrOpenCount = 0;
    private int count = 0;
    private DataSendCallback dataSendCallback;
    private boolean hasComm = false;
    private boolean isBack = false;
    private boolean isCloseOrOpenBack = false;
    private Handler mHandler = new C14531();
    private int maxHR;
    private int minHR;

    class C14531 extends Handler {
        C14531() {
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (BleDataForHRWarning.this.isBack) {
                        BleDataForHRWarning.this.closeSendData(this);
                        return;
                    } else if (BleDataForHRWarning.this.count < 4) {
                        BleDataForHRWarning.this.continueSend(this, msg);
                        BleDataForHRWarning.this.requestTheHRWarningData();
                        return;
                    } else {
                        BleDataForHRWarning.this.closeSendData(this);
                        return;
                    }
                case 2:
                    if (BleDataForHRWarning.this.isCloseOrOpenBack) {
                        BleDataForHRWarning.this.closeSendDataClose(this);
                        return;
                    } else if (BleDataForHRWarning.this.closeOrOpenCount < 4) {
                        BleDataForHRWarning.this.conitueSendCloseData(this, msg);
                        BleDataForHRWarning.this.closeOrOpenWarning(BleDataForHRWarning.this.maxHR, BleDataForHRWarning.this.minHR, msg.getData().getByte("closeOrOpen"));
                        return;
                    } else {
                        BleDataForHRWarning.this.closeSendDataClose(this);
                        return;
                    }
                default:
                    return;
            }
        }
    }

    private void closeSendDataClose(Handler handler) {
        handler.removeMessages(2);
        this.isCloseOrOpenBack = false;
        this.closeOrOpenCount = 0;
    }

    private void continueSend(Handler handler, Message msg) {
        Message mzg = handler.obtainMessage();
        mzg.what = 0;
        mzg.arg1 = msg.arg1;
        mzg.arg2 = msg.arg2;
        handler.sendMessageDelayed(mzg, (long) SendLengthHelper.getSendLengthDelay(msg.arg1, msg.arg2));
        this.count++;
    }

    private void conitueSendCloseData(Handler handler, Message msges) {
        Message msg = handler.obtainMessage();
        msg.what = 2;
        msg.arg1 = msges.arg1;
        msg.arg2 = msges.arg2;
        msg.setData(msges.getData());
        handler.sendMessageDelayed(msg, (long) SendLengthHelper.getSendLengthDelay(msges.arg1, msges.arg2));
        this.closeOrOpenCount++;
    }

    private void closeSendData(Handler handler) {
        handler.removeMessages(0);
        if (this.dataSendCallback != null) {
            if (!this.isBack) {
                this.dataSendCallback.sendFailed();
            }
            this.dataSendCallback.sendFinished();
        }
        this.isBack = false;
        this.hasComm = false;
        this.count = 0;
    }

    public static BleDataForHRWarning getInstance() {
        if (bleDataForHRWarning == null) {
            synchronized (BleDataForHRWarning.class) {
                if (bleDataForHRWarning == null) {
                    bleDataForHRWarning = new BleDataForHRWarning();
                }
            }
        }
        return bleDataForHRWarning;
    }

    private BleDataForHRWarning() {
    }

    public void setDataSendCallback(DataSendCallback dataSendCallback) {
        this.dataSendCallback = dataSendCallback;
    }

    public void requestWarningData() {
        this.hasComm = true;
        int sendLg = requestTheHRWarningData();
        Message msg = this.mHandler.obtainMessage();
        msg.what = 0;
        msg.arg1 = sendLg;
        msg.arg2 = 10;
        this.mHandler.sendMessageDelayed(msg, (long) SendLengthHelper.getSendLengthDelay(sendLg, 10));
    }

    private int requestTheHRWarningData() {
        byte[] array = new byte[]{(byte) 2};
        return setMsgToByteDataAndSendToDevice(toDevice, array, array.length);
    }

    public void closeOrOpenWarning(int maxHR, int minHR, byte open) {
        this.maxHR = maxHR;
        this.minHR = minHR;
        int closeLg = settingsHRWarning(maxHR, minHR, open);
        Message msg = this.mHandler.obtainMessage();
        msg.what = 2;
        msg.arg1 = closeLg;
        msg.arg2 = 10;
        Bundle bundle = new Bundle();
        bundle.putByte("openOrNo", open);
        msg.setData(bundle);
        this.mHandler.sendMessageDelayed(msg, (long) SendLengthHelper.getSendLengthDelay(closeLg, 10));
    }

    private int settingsHRWarning(int maxHR, int minHR, byte open) {
        byte[] byteArray = new byte[]{(byte) 1, open, (byte) (maxHR & 255), (byte) (minHR & 255)};
        return setMsgToByteDataAndSendToDevice(toDevice, byteArray, byteArray.length);
    }

    public void dealTheHRData(byte[] bufferTmp, Context mcContext) {
        if (bufferTmp[0] == (byte) 2) {
            if (this.hasComm) {
                this.isBack = true;
                this.hasComm = false;
                this.dataSendCallback.sendSuccess(bufferTmp);
            }
        } else if (bufferTmp[0] == (byte) 1) {
            this.isCloseOrOpenBack = true;
            requestWarningData();
        }
    }
}
