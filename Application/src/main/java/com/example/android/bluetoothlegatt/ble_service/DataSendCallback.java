package com.example.android.bluetoothlegatt.ble_service;

/**
 * @author Sopheak Tuon
 * @created on 02-Oct-17
 */

public interface DataSendCallback {
    void sendFailed();

    void sendFinished();

    void sendSuccess(byte[] bArr);
}
