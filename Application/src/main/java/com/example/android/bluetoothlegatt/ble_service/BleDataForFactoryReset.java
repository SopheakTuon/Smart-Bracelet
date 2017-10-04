package com.example.android.bluetoothlegatt.ble_service;

/**
 * @author Sopheak Tuon
 * @created on 04-Oct-17
 */

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class BleDataForFactoryReset extends BleBaseDataManage {
    private static final String TAG = BleDataForFactoryReset.class.getSimpleName();
    private static BleDataForFactoryReset bleDataForFactoryReset = null;
    public static final byte fromDevice = (byte) -111;
    public static final byte toDevice = (byte) 17;
    private final int FACTORY_RESET = 0;
    private Handler factoryHandler = new C14511();
    private boolean isSendOk = false;
    private DataSendCallback resetCallback;
    private int sendCount = 0;

    class C14511 extends Handler {
        C14511() {
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (BleDataForFactoryReset.this.isSendOk) {
                        BleDataForFactoryReset.this.stopSend(this);
                        return;
                    } else if (BleDataForFactoryReset.this.sendCount < 4) {
                        BleDataForFactoryReset.this.continueSend(this, msg);
                        BleDataForFactoryReset.this.factoryReset();
                        return;
                    } else {
                        BleDataForFactoryReset.this.stopSend(this);
                        return;
                    }
                default:
                    return;
            }
        }
    }

    public static BleDataForFactoryReset getBleDataInstance() {
        if (bleDataForFactoryReset == null) {
            synchronized (BleDataForFactoryReset.class) {
                if (bleDataForFactoryReset == null) {
                    bleDataForFactoryReset = new BleDataForFactoryReset();
                }
            }
        }
        return bleDataForFactoryReset;
    }

    private BleDataForFactoryReset() {
    }

    private void continueSend(Handler handler, Message msg) {
        Message mzg = handler.obtainMessage();
        mzg.what = 0;
        mzg.arg1 = msg.arg1;
        mzg.arg2 = msg.arg2;
        handler.sendMessageDelayed(mzg, (long) SendLengthHelper.getSendLengthDelay(msg.arg1, msg.arg2));
        this.sendCount++;
    }

    private void stopSend(Handler handler) {
        handler.removeMessages(0);
        if (this.resetCallback != null) {
            if (!this.isSendOk) {
                this.resetCallback.sendFailed();
            }
            this.resetCallback.sendFinished();
        }
        this.isSendOk = false;
        this.sendCount = 0;
    }

    public void settingFactoryReset() {
        int sendLg = factoryReset();
        Message msg = this.factoryHandler.obtainMessage();
        msg.what = 0;
        msg.arg1 = sendLg;
        msg.arg2 = 10;
        this.factoryHandler.sendMessageDelayed(msg, (long) SendLengthHelper.getSendLengthDelay(sendLg, 10));
    }

    private int factoryReset() {
        byte[] bytes = new byte[]{(byte) 1};
        return setMsgToByteDataAndSendToDevice(toDevice, bytes, bytes.length);
    }

    public void dealTheResult(Context mContext, byte[] bufferTmp) {
        this.isSendOk = true;
        byte dataResult = bufferTmp[1];
        String result = "";
        if (bufferTmp[0] != (byte) 1) {
            return;
        }
        if (dataResult == (byte) 0) {
            if (this.resetCallback != null) {
                this.resetCallback.sendSuccess(bufferTmp);
            }
        } else if (dataResult == (byte) 1) {
            factoryReset();
        }
    }

    public void setFactoryResetListener(DataSendCallback resetCallback) {
        this.resetCallback = resetCallback;
    }
}
