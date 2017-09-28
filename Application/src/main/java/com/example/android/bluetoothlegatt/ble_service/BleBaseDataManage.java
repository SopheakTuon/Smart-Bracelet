package com.example.android.bluetoothlegatt.ble_service;

import android.util.Log;

import com.example.android.bluetoothlegatt.util.FormatUtils;

/**
 * @author Sopheak Tuon
 * @created on 28-Sep-17
 */

public class BleBaseDataManage {
    private byte msg_check_value = (byte) 22;
    private byte msg_cmd = (byte) 0;
    private byte[] msg_data;
    private int msg_data_length = 0;
    private byte msg_head = (byte) 104;

    public byte[] getSendByteArray() {
        byte[] sendData = new byte[(this.msg_data_length + 6)];
        int count_for_check = 0;
        int i = 0;
        while (i < sendData.length) {
            if (i == 0) {
                sendData[i] = this.msg_head;
                count_for_check += sendData[i];
            } else if (1 == i) {
                sendData[i] = this.msg_cmd;
                count_for_check += sendData[i];
            } else if (2 == i) {
                sendData[i] = (byte) (this.msg_data_length & 255);
                count_for_check += sendData[i];
            } else if (3 == i) {
                sendData[i] = (byte) ((this.msg_data_length >> 8) & 255);
                count_for_check += sendData[i];
            } else if (i >= 4 && i < this.msg_data_length + 4) {
                sendData[i] = this.msg_data[i - 4];
                count_for_check += sendData[i];
            } else if (i == this.msg_data_length + 4) {
                sendData[i] = (byte) (count_for_check % 256);
            } else if (i == this.msg_data_length + 5) {
                sendData[i] = msg_check_value;
            }
            i++;
        }
        return sendData;
    }

    public int setMsgToByteDataAndSendToDevice(byte cmd, byte[] long2ByteData, int dataLength) {
        this.msg_cmd = cmd;
        if (long2ByteData != null) {
            this.msg_data_length = dataLength;
            this.msg_data = long2ByteData;
        }
        byte[] datas = getSendByteArray();
        if (cmd == BleDataForFrame.toDevice) {
            Log.i("ssss", "huanhghhh" + FormatUtils.bytesToHexString(datas));
        }
        BleByteDataSendTool.getInstance().judgeTheDataLengthAndAddToSendByteArray(datas);
        return datas.length;
    }

    public int setMessageDataByString(byte cmd, String data, boolean isNeedSend) {
        byte[] bytes = null;
        this.msg_cmd = cmd;
        if (data != null) {
            if (data.length() == 1) {
                data = "0" + data;
            }
            this.msg_data_length = data.length() / 2;
            this.msg_data = FormatUtils.hexString2ByteArray(data);
        }
        if (isNeedSend) {
            bytes = getSendByteArray();
            BleByteDataSendTool.getInstance().judgeTheDataLengthAndAddToSendByteArray(bytes);
        }
        return bytes.length;
    }
}
