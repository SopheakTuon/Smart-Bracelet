package com.example.android.bluetoothlegatt.ble_service;

import java.math.BigDecimal;

/**
 * @author Sopheak Tuon
 * @created on 02-Oct-17
 */

public class BleBaseDataForBlood extends BleBaseDataManage {
    public static final String TAG = BleBaseDataForBlood.class.getSimpleName();
    private static BleBaseDataForBlood bleBaseDataForBloodMovement = null;
    public static final byte mNotify_cmd = (byte) -86;
    public static final byte toDevice = (byte) 42;
    private DataSendCallback bloodDataListener;

    private BleBaseDataForBlood() {
    }

    public static BleBaseDataForBlood getBloodInstance() {
        if (bleBaseDataForBloodMovement == null) {
            synchronized (BleBaseDataForBlood.class) {
                if (bleBaseDataForBloodMovement == null) {
                    bleBaseDataForBloodMovement = new BleBaseDataForBlood();
                }
            }
        }
        return bleBaseDataForBloodMovement;
    }

    public void setOnBloodDataListener(DataSendCallback outLineDataListener) {
        this.bloodDataListener = outLineDataListener;
    }

    public void dealTheData(byte[] data) {
        this.bloodDataListener.sendSuccess(data);
    }

    public void requstOutlineData(int hig, int low) {
        byte[] send = new byte[]{(byte) 5, (byte) hig, (byte) low};
        setMsgToByteDataAndSendToDevice(toDevice, send, send.length);
    }

    public void requestdevice() {
        byte[] send = new byte[]{(byte) 2, (byte) 0};
        setMsgToByteDataAndSendToDevice(toDevice, send, send.length);
    }

    public void requstOutlineData() {
        byte[] back = new byte[]{(byte) 0};
        setMsgToByteDataAndSendToDevice(toDevice, back, back.length);
    }

    private int getTheHalfUp(float averageHr) {
        return new BigDecimal((double) averageHr).setScale(0, 4).intValue();
    }

    public void sendStandardBloodData(int firstData, int secondData) {
        byte[] send = new byte[]{(byte) 5, (byte) firstData, (byte) secondData};
        setMsgToByteDataAndSendToDevice(toDevice, send, send.length);
    }

    public void sendStandardBloodDataYD(int true_or_flase) {
        byte[] send = new byte[]{(byte) 1, (byte) true_or_flase};
        setMsgToByteDataAndSendToDevice(toDevice, send, send.length);
    }

    public void sendStandardBloodDataFA(int t_or_f) {
        byte[] send = new byte[]{(byte) 2, (byte) t_or_f};
        setMsgToByteDataAndSendToDevice(toDevice, send, send.length);
    }

    public void sendStandardBloodDataTT(int firstData, int secondData) {
        byte[] send = new byte[]{(byte) 4, (byte) firstData, (byte) secondData};
    }
}
