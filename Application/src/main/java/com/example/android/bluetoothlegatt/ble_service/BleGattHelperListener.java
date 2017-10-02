package com.example.android.bluetoothlegatt.ble_service;

import android.bluetooth.BluetoothGatt;

/**
 * @author Sopheak Tuon
 * @created on 02-Oct-17
 */

public interface BleGattHelperListener {
    void onDeviceConnectedChangeUI(LocalDeviceEntity localDeviceEntity, boolean z, boolean z2);

    void onDeviceStateChangeUI(LocalDeviceEntity localDeviceEntity, BluetoothGatt bluetoothGatt, String str, byte[] bArr);
}
