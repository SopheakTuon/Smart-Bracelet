package com.example.android.bluetoothlegatt.ble_service;

/**
 * @author Sopheak Tuon
 * @created on 04-Oct-17
 */

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class BleDataForUpLoad extends BleBaseDataManage {
    public static final String TAG = BleDataForUpLoad.class.getSimpleName();
    private static BleDataForUpLoad bleDataForUpLoad = null;
    public static final byte fromDevice = (byte) -120;
    public static final byte toDevice = (byte) 8;
    private final int FINISH_UPGRAND_COMM = 1;
    private final int START_UPGRAND_COMM = 0;
    private DataSendCallback dataSendCallback;
    private boolean hasRecever = false;
    private boolean isSendOk = false;
    private int sendCount = 0;
    private Handler updateHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (BleDataForUpLoad.this.isSendOk) {
                        BleDataForUpLoad.this.stopSend(this);
                        return;
                    } else if (BleDataForUpLoad.this.sendCount < 5) {
                        BleDataForUpLoad.this.continueStart(this, msg);
                        return;
                    } else {
                        BleDataForUpLoad.this.stopSend(this);
                        return;
                    }
                case 1:
                    if (BleDataForUpLoad.this.isSendOk) {
                        BleDataForUpLoad.this.stopFinish(this);
                        return;
                    } else if (BleDataForUpLoad.this.sendCount < 4) {
                        BleDataForUpLoad.this.continueFinish(this, msg);
                        BleDataForUpLoad.this.overTheUpdate();
                        return;
                    } else {
                        BleDataForUpLoad.this.stopSend(this);
                        return;
                    }
                default:
                    return;
            }
        }
    };

    private BleDataForUpLoad() {
    }

    public static BleDataForUpLoad getUpLoadInstance() {
        if (bleDataForUpLoad == null) {
            synchronized (BleDataForUpLoad.class) {
                if (bleDataForUpLoad == null) {
                    bleDataForUpLoad = new BleDataForUpLoad();
                }
            }
        }
        return bleDataForUpLoad;
    }

    private void continueFinish(Handler handler, Message msg) {
        handler.sendEmptyMessageDelayed(1, 2000);
        this.sendCount++;
    }

    private void stopFinish(Handler handler) {
        handler.removeMessages(1);
        if (this.dataSendCallback != null) {
            if (!this.isSendOk) {
                this.dataSendCallback.sendFailed();
            }
            this.dataSendCallback.sendFinished();
        }
        this.isSendOk = false;
        this.sendCount = 0;
    }

    private void continueStart(Handler handler, Message msg) {
        Message mzg = handler.obtainMessage();
        mzg.what = 0;
        handler.sendMessageDelayed(mzg, 3000);
        this.sendCount++;
    }

    private void stopSend(Handler handler) {
        handler.removeMessages(0);
        if (this.dataSendCallback != null) {
            if (!this.isSendOk) {
                this.dataSendCallback.sendFailed();
            }
            this.dataSendCallback.sendFinished();
        }
        this.isSendOk = false;
        this.sendCount = 0;
    }

    public void sendStartComm(int length) {
        this.hasRecever = true;
        int leng = sendToDeviceToStartUpLoad(length);
        Message msg = this.updateHandler.obtainMessage();
        msg.what = 0;
        this.updateHandler.sendMessageDelayed(msg, 3000);
    }

    private int sendToDeviceToStartUpLoad(int length) {
        byte[] data = new byte[]{(byte) 0, (byte) length, (byte) (length >> 8), (byte) (length >> 16), (byte) (length >> 24)};
        return setMsgToByteDataAndSendToDevice((byte) 8, data, data.length);
    }

    public void finaishUpdate() {
        this.hasRecever = true;
        int lg = overTheUpdate();
        this.updateHandler.sendEmptyMessageDelayed(1, 2000);
    }

    public int overTheUpdate() {
        byte[] overDate = new byte[]{(byte) 2};
        return setMsgToByteDataAndSendToDevice((byte) 8, overDate, overDate.length);
    }

    public void sendDataUpdate(byte[] data) {
        setMsgToByteDataAndSendToDevice((byte) 8, data, data.length);
    }

    public void dealUpLoadBackData(byte cmd, byte[] bufferTmp) {
        if (bufferTmp[0] == (byte) 0 || bufferTmp[0] == (byte) 2) {
            this.isSendOk = true;
            if (this.hasRecever) {
                this.hasRecever = false;
                this.dataSendCallback.sendSuccess(bufferTmp);
            }
        }
        if (bufferTmp[0] == (byte) 1) {
            this.dataSendCallback.sendSuccess(bufferTmp);
        }
    }

    public void setUpLoadCallback(DataSendCallback dataSendCallback) {
        this.dataSendCallback = dataSendCallback;
    }
}
