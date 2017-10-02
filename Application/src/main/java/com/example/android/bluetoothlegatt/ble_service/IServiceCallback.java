package com.example.android.bluetoothlegatt.ble_service;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;

import java.util.List;

/**
 * @author Sopheak Tuon
 * @created on 02-Oct-17
 */

public interface IServiceCallback {
    void onBLEDeviceConnecError(LocalDeviceEntity localDeviceEntity, boolean z, boolean z2);

    void onBLEDeviceConnected(LocalDeviceEntity localDeviceEntity, BluetoothGatt bluetoothGatt);

    void onBLEDeviceDisConnected(LocalDeviceEntity localDeviceEntity, BluetoothGatt bluetoothGatt);

    void onBLEServiceFound(LocalDeviceEntity localDeviceEntity, BluetoothGatt bluetoothGatt, List<BluetoothGattService> list);

    void onCharacteristicChanged(LocalDeviceEntity localDeviceEntity, BluetoothGatt bluetoothGatt, String str, byte[] bArr);

    void onCharacteristicRead(LocalDeviceEntity localDeviceEntity, BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, boolean z);

    void onCharacteristicWrite(LocalDeviceEntity localDeviceEntity, BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, boolean z);

    void onDescriptorRead(LocalDeviceEntity localDeviceEntity, BluetoothGatt bluetoothGatt, BluetoothGattDescriptor bluetoothGattDescriptor, boolean z);

    void onDescriptorWrite(LocalDeviceEntity localDeviceEntity, BluetoothGatt bluetoothGatt, BluetoothGattDescriptor bluetoothGattDescriptor, boolean z);

    void onMTUChange(BluetoothGatt bluetoothGatt, int i, int i2);

    void onNoBLEServiceFound();

    void onReadRemoteRssi(LocalDeviceEntity localDeviceEntity, BluetoothGatt bluetoothGatt, int i, boolean z);

    void onReliableWriteCompleted(LocalDeviceEntity localDeviceEntity, BluetoothGatt bluetoothGatt, boolean z);
}
