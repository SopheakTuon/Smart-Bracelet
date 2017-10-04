package com.example.android.bluetoothlegatt.ble_service;

/**
 * @author Sopheak Tuon
 * @created on 04-Oct-17
 */

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class BleDataForTakePhoto extends BleBaseDataManage {
    private static BleDataForTakePhoto bleDataForTakePhoto = null;
    public static final byte startFromDevice = (byte) -115;
    public static final byte startToDevice = (byte) 13;
    public static final byte takeFromDevice = (byte) -114;
    public static final byte takeToDevice = (byte) 14;
    private final int START_END_MESSAGE = 0;
    private final int TAKE_PHOTO_MESSAGE = 1;
    private int count = 0;
    private boolean hasComm = false;
    private boolean isAlreadyBack = false;
    private DataSendCallback onDeviceCallback;
    private Handler takePhotoHandler = new C14611();

    class C14611 extends Handler {
        C14611() {
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (BleDataForTakePhoto.this.isAlreadyBack) {
                        BleDataForTakePhoto.this.closeSendStartMessage(this);
                        return;
                    } else if (BleDataForTakePhoto.this.count < 4) {
                        BleDataForTakePhoto.this.continueSendStartMessage(this, msg);
                        BleDataForTakePhoto.this.sendTakePhoto((byte) msg.arg1);
                        return;
                    } else {
                        BleDataForTakePhoto.this.closeSendStartMessage(this);
                        return;
                    }
                case 1:
                    if (BleDataForTakePhoto.this.isAlreadyBack) {
                        BleDataForTakePhoto.this.closeSendTakeMessage(this);
                        return;
                    } else if (BleDataForTakePhoto.this.count < 4) {
                        BleDataForTakePhoto.this.continueSendTakeMessage(this);
                        BleDataForTakePhoto.this.backMessage();
                        return;
                    } else {
                        BleDataForTakePhoto.this.closeSendTakeMessage(this);
                        return;
                    }
                default:
                    return;
            }
        }
    }

    private BleDataForTakePhoto() {
    }

    public static BleDataForTakePhoto getInstance() {
        if (bleDataForTakePhoto == null) {
            synchronized (BleDataForTakePhoto.class) {
                if (bleDataForTakePhoto == null) {
                    bleDataForTakePhoto = new BleDataForTakePhoto();
                }
            }
        }
        return bleDataForTakePhoto;
    }

    private void continueSendTakeMessage(Handler handler) {
        handler.sendEmptyMessage(1);
        this.count++;
    }

    private void closeSendTakeMessage(Handler handler) {
        handler.removeMessages(1);
        this.isAlreadyBack = false;
        this.count = 0;
    }

    private void continueSendStartMessage(Handler handler, Message msges) {
        Message msg = Message.obtain();
        msg.what = 0;
        msg.arg1 = msges.arg1;
        Bundle bundle = msges.getData();
        msg.setData(bundle);
        handler.sendMessageDelayed(msg, (long) SendLengthHelper.getSendLengthDelay(bundle.getInt("send_length"), bundle.getInt("rece_length")));
        this.count++;
    }

    private void closeSendStartMessage(Handler handler) {
        handler.removeMessages(0);
        if (this.onDeviceCallback != null) {
            if (!this.isAlreadyBack) {
                this.onDeviceCallback.sendFailed();
            }
            this.onDeviceCallback.sendFinished();
        }
        this.isAlreadyBack = false;
        this.count = 0;
        this.hasComm = false;
    }

    public void openTakePhoto(byte swich) {
        this.hasComm = true;
        int sendLg = sendTakePhoto(swich);
        Message msg = Message.obtain();
        msg.what = 0;
        msg.arg1 = swich;
        Bundle bundle = new Bundle();
        bundle.putInt("send_length", sendLg);
        bundle.putInt("rece_length", 7);
        msg.setData(bundle);
        this.takePhotoHandler.sendMessageDelayed(msg, (long) SendLengthHelper.getSendLengthDelay(sendLg, 7));
    }

    private int sendTakePhoto(byte swich) {
        byte[] bytes = new byte[]{swich};
        return setMsgToByteDataAndSendToDevice(startToDevice, bytes, bytes.length);
    }

    public void dealOpenResponse(byte[] bufferTmp) {
        if (this.hasComm) {
            this.isAlreadyBack = true;
            this.hasComm = false;
            if (this.onDeviceCallback != null) {
                this.onDeviceCallback.sendSuccess(bufferTmp);
            }
        }
    }

    private void sendToDeviceMessage() {
        backMessage();
    }

    private void backMessage() {
        setMsgToByteDataAndSendToDevice(takeToDevice, new byte[0], 0);
    }

    public void backMessageToDevice() {
        sendToDeviceMessage();
        if (this.onDeviceCallback != null) {
            this.onDeviceCallback.sendSuccess(null);
        }
    }

    public void setOnDeviceTakePhotoOpen(DataSendCallback cameraCallback) {
        this.onDeviceCallback = cameraCallback;
    }
}
