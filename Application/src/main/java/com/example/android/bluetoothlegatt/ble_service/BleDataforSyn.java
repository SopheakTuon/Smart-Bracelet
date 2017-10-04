package com.example.android.bluetoothlegatt.ble_service;

/**
 * @author Sopheak Tuon
 * @created on 04-Oct-17
 */

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.android.bluetoothlegatt.ble_service.util.DateUtils;

import java.util.Calendar;
import java.util.TimeZone;

public class BleDataforSyn extends BleBaseDataManage {
    public static byte back_cmd = (byte) -96;
    private static BleDataforSyn bleDataforSyn;
    private static byte send_cmd = (byte) 32;
    private final int SYNC_TIME = 0;
    private int count = 0;
    private DataSendCallback dataSendCallback;
    private boolean hasComm = false;
    private boolean isBack = false;
    private Handler synHandler = new C14661();

    class C14661 extends Handler {
        C14661() {
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (BleDataforSyn.this.isBack) {
                        BleDataforSyn.this.closeSycn(this);
                        return;
                    } else if (BleDataforSyn.this.count < 4) {
                        BleDataforSyn.this.cotinueSycn(this, msg.arg1, msg.arg2);
                        BleDataforSyn.this.sysnTheTime();
                        return;
                    } else {
                        BleDataforSyn.this.closeSycn(this);
                        return;
                    }
                default:
                    return;
            }
        }
    }

    public void setDataSendCallback(DataSendCallback dataSendCallback) {
        this.dataSendCallback = dataSendCallback;
    }

    public static BleDataforSyn getSynInstance() {
        if (bleDataforSyn == null) {
            bleDataforSyn = new BleDataforSyn();
        }
        return bleDataforSyn;
    }

    private void cotinueSycn(Handler handler, int sendLength, int receLength) {
        Message msg = handler.obtainMessage();
        msg.what = 0;
        msg.arg1 = sendLength;
        msg.arg2 = receLength;
        handler.sendEmptyMessageDelayed(0, (long) SendLengthHelper.getSendLengthDelay(sendLength, receLength));
        this.count++;
    }

    private void closeSycn(Handler handler) {
        handler.removeMessages(0);
        if (this.dataSendCallback != null) {
            if (!this.isBack) {
                this.dataSendCallback.sendFailed();
            }
            this.dataSendCallback.sendFinished();
        }
        this.isBack = false;
        this.count = 0;
    }

    private BleDataforSyn() {
    }

    public int syncCurrentTime() {
        int delay = sysnTheTime();
        this.hasComm = true;
        Message msg = this.synHandler.obtainMessage();
        msg.what = 0;
        msg.arg1 = delay;
        msg.arg2 = 6;
        this.synHandler.sendMessageDelayed(msg, (long) SendLengthHelper.getSendLengthDelay(delay, 6));
        return delay;
    }

    private int sysnTheTime() {
        byte[] ret = FormatUtils.int2Byte_LH(DateUtils.currentTimeSeconds() + ((TimeZone.getDefault().getRawOffset() / 1000) + (Calendar.getInstance().get(Calendar.DST_OFFSET) / 1000)));
        return setMsgToByteDataAndSendToDevice(send_cmd, ret, ret.length);
    }

    public void dealTheResult() {
        if (this.hasComm) {
            this.isBack = true;
            if (this.dataSendCallback != null) {
                this.dataSendCallback.sendSuccess(null);
            } else {
                Log.e("", "回调为空");
            }
            this.hasComm = false;
        }
    }
}
