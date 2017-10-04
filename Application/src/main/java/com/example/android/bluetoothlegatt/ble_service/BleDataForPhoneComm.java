package com.example.android.bluetoothlegatt.ble_service;

/**
 * @author Sopheak Tuon
 * @created on 04-Oct-17
 */

public class BleDataForPhoneComm extends BleBaseDataManage {
    private static BleDataForPhoneComm bleDataForPhoneComm = null;
    public static final byte fromDevice = (byte) -85;
    public static final byte toDevice = (byte) 43;
    private DataSendCallback mDataSendCallback;

    private BleDataForPhoneComm() {
    }

    public static BleDataForPhoneComm getInstance() {
        if (bleDataForPhoneComm == null) {
            synchronized (BleDataForPhoneComm.class) {
                if (bleDataForPhoneComm == null) {
                    bleDataForPhoneComm = new BleDataForPhoneComm();
                }
            }
        }
        return bleDataForPhoneComm;
    }

    public void dealDeviceComm(byte[] bufferTmp) {
        if (this.mDataSendCallback != null) {
            this.mDataSendCallback.sendSuccess(bufferTmp);
        }
    }

    public void setDeviceCommListener(DataSendCallback callback) {
        this.mDataSendCallback = callback;
    }

    public void respondFlag2() {
        byte[] data = new byte[]{(byte) 2};
        setMsgToByteDataAndSendToDevice(toDevice, data, data.length);
    }
}
