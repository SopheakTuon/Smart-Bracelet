package com.example.android.bluetoothlegatt.ble_service;

/**
 * @author Sopheak Tuon
 * @created on 04-Oct-17
 */

import android.content.Context;

public class DeviceExceptionDeal extends BleBaseDataManage {
    public static final String TAG = DeviceExceptionDeal.class.getSimpleName();
    private static DeviceExceptionDeal deviceExceptionDeal = null;
    public static final byte fromDevice = (byte) -89;
    public static final byte testFromDevice = (byte) -88;
    public static final byte testToDevice = (byte) 40;
    public static final byte toDevice = (byte) 39;
    private Context context;
    private DataSendCallback dataSendCallback;

    private DeviceExceptionDeal(Context context) {
        this.context = context;
    }

    public static DeviceExceptionDeal getExceptionInstance(Context context) {
        if (deviceExceptionDeal == null) {
            synchronized (DeviceExceptionDeal.class) {
                if (deviceExceptionDeal == null) {
                    deviceExceptionDeal = new DeviceExceptionDeal(context);
                }
            }
        }
        return deviceExceptionDeal;
    }

    public void dealExceptionInfo(byte[] bufferTmp) {
        byte[] bytes = new byte[]{(byte) 1, (byte) 0};
        setMsgToByteDataAndSendToDevice(toDevice, bytes, bytes.length);
        byte[] temp = new byte[(bufferTmp.length + 1)];
        System.arraycopy(bufferTmp, 0, temp, 1, bufferTmp.length);
        temp[0] = (byte) 0;
        this.dataSendCallback.sendSuccess(temp);
    }

    public void dealTextData(byte[] bufferTmp) {
        byte[] temp = new byte[(bufferTmp.length + 1)];
        System.arraycopy(bufferTmp, 0, temp, 1, bufferTmp.length);
        temp[0] = (byte) 1;
        this.dataSendCallback.sendSuccess(bufferTmp);
        if (bufferTmp[0] == (byte) 1) {
            responseData(bufferTmp[0], bufferTmp[bufferTmp.length - 4], bufferTmp[bufferTmp.length - 3]);
        } else if (bufferTmp[0] != (byte) 2) {
        }
    }

    private void responseData(byte data1, byte b, byte data) {
        byte[] resData = new byte[]{data1, (byte) 0, b, data};
        setMsgToByteDataAndSendToDevice(testToDevice, resData, resData.length);
    }

    public void setOnExceptionData(DataSendCallback dataSendCallback) {
        this.dataSendCallback = dataSendCallback;
    }
}
