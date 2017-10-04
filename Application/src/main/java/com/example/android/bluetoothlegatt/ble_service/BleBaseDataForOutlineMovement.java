package com.example.android.bluetoothlegatt.ble_service;

/**
 * @author Sopheak Tuon
 * @created on 04-Oct-17
 */

import android.content.Context;
import java.math.BigDecimal;

public class BleBaseDataForOutlineMovement extends BleBaseDataManage {
    public static final String TAG = BleBaseDataForOutlineMovement.class.getSimpleName();
    private static BleBaseDataForOutlineMovement bleBaseDataForOutlineMovement = null;
    public static final byte mNotify_cmd = (byte) -94;
    public static final byte toDevice = (byte) 34;
    private DataSendCallback outlineDataListener;

    private BleBaseDataForOutlineMovement() {
    }

    public static BleBaseDataForOutlineMovement getOutlineInstance() {
        if (bleBaseDataForOutlineMovement == null) {
            synchronized (BleBaseDataForOutlineMovement.class) {
                if (bleBaseDataForOutlineMovement == null) {
                    bleBaseDataForOutlineMovement = new BleBaseDataForOutlineMovement();
                }
            }
        }
        return bleBaseDataForOutlineMovement;
    }

    public void setOnOutLineDataListener(DataSendCallback outLineDataListener) {
        this.outlineDataListener = outLineDataListener;
    }

    public void dealTheData(Context mContext, byte[] data, int length) {
        this.outlineDataListener.sendSuccess(data);
    }

    public void requstOutlineData(byte[] data) {
        byte[] back = new byte[9];
        System.arraycopy(data, 0, back, 0, 9);
        setMsgToByteDataAndSendToDevice(toDevice, back, back.length);
    }

    private int getTheHalfUp(float averageHr) {
        return new BigDecimal((double) averageHr).setScale(0, 4).intValue();
    }
}
