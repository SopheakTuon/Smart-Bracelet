package com.example.android.bluetoothlegatt.ble_service;

/**
 * @author Sopheak Tuon
 * @created on 04-Oct-17
 */

import android.content.Context;

import java.util.Calendar;

public class BleDataForSettingParams extends BleBaseDataManage {
    public static final byte fromDevice = (byte) -124;
    public static final byte getSettingCmd = (byte) 4;
    private Context mContext;

    public BleDataForSettingParams(Context mContext) {
        this.mContext = mContext;
    }

    public void settingTheStepParamsToBracelet(String hei, String wei, String gen, String bir) {
        if (bir == null) {
            return;
        }
        if ((bir != null && bir.equals("")) || hei == null) {
            return;
        }
        if ((hei != null && hei.equals("")) || wei == null) {
            return;
        }
        if ((wei != null && wei.equals("")) || gen == null) {
            return;
        }
        if (gen == null || !gen.equals("")) {
            int age = Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(bir.substring(0, 4));
            byte[] buffers = new byte[]{(byte) Integer.parseInt(hei), (byte) Integer.parseInt(wei), (byte) Integer.parseInt(gen), (byte) age};
            setMsgToByteDataAndSendToDevice((byte) 4, buffers, buffers.length);
        }
    }

    public void dealBleResponse(Context context, byte[] data, int length) {
    }
}
