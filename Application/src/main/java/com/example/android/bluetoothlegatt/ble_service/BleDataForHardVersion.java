package com.example.android.bluetoothlegatt.ble_service;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

/**
 * @author Sopheak Tuon
 * @created on 04-Oct-17
 */

public class BleDataForHardVersion extends BleBaseDataManage {
    private static BleDataForHardVersion bleDataInstance = null;
    public static final byte fromDevice = (byte) -121;
    public static final byte send_cmd = (byte) 7;
    private static String versionString = null;
    private final int GET_HARD_VERSION = 0;
    private int count = 0;
    private DataSendCallback dataSendCallback;
    private boolean hasComm = false;
    private boolean isBack = false;
    private Handler verHandler = new C14541();

    class C14541 extends Handler {
        C14541() {
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (BleDataForHardVersion.this.isBack) {
                        BleDataForHardVersion.this.closeSendData(this);
                        return;
                    } else if (BleDataForHardVersion.this.count < 4) {
                        BleDataForHardVersion.this.coutinueSendData(this, msg);
                        BleDataForHardVersion.this.getHardVersion();
                        return;
                    } else {
                        BleDataForHardVersion.this.closeSendData(this);
                        return;
                    }
                default:
                    return;
            }
        }
    }

    private void coutinueSendData(Handler handler, Message msges) {
        Message msg = handler.obtainMessage();
        msg.what = 0;
        msg.arg1 = msges.arg1;
        msg.arg2 = msges.arg2;
        handler.sendMessageDelayed(msg, (long) SendLengthHelper.getSendLengthDelay(msges.arg1, msges.arg2));
        this.count++;
    }

    private void closeSendData(Handler handler) {
        handler.removeMessages(0);
        this.isBack = false;
        this.hasComm = false;
        this.count = 0;
        this.dataSendCallback.sendFinished();
    }

    private BleDataForHardVersion() {
    }

    public static BleDataForHardVersion getInstance() {
        if (bleDataInstance == null) {
            synchronized (BleDataForHardVersion.class) {
                if (bleDataInstance == null) {
                    bleDataInstance = new BleDataForHardVersion();
                }
            }
        }
        return bleDataInstance;
    }

    public void setDataSendCallback(DataSendCallback dataSendCallback) {
        this.dataSendCallback = dataSendCallback;
    }

    public void requestHardVersion() {
        this.hasComm = true;
        int length = getHardVersion();
        Message msg = this.verHandler.obtainMessage();
        msg.what = 0;
        msg.arg1 = length;
        msg.arg2 = 9;
        this.verHandler.sendMessageDelayed(msg, (long) SendLengthHelper.getSendLengthDelay(length, 9));
    }

    private int getHardVersion() {
        return setMessageDataByString((byte) 7, null, true);
    }

    public void dealReceData(Context mContext, byte[] bufferTmp, int dataLen) {
        if (this.hasComm) {
            this.isBack = true;
            this.hasComm = false;
            this.dataSendCallback.sendSuccess(bufferTmp);
        }
    }

    public static String getVersionString() {
        return versionString;
    }
}
