package com.example.android.bluetoothlegatt.ble_service;

/**
 * @author Sopheak Tuon
 * @created on 03-Oct-17
 */

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;

import java.util.List;

public class BleGattHelper implements IServiceCallback {
    private static BleGattHelper bleGattHelper;
    private Context context;
    private BleGattHelperListener gattHelperListener;

    public static BleGattHelper getInstance(Context context, BleGattHelperListener listener) {
        if (bleGattHelper == null) {
            synchronized (BleGattHelper.class) {
                if (bleGattHelper == null) {
                    bleGattHelper = new BleGattHelper(context);
                }
            }
        }
        bleGattHelper.setBleGattListener(listener);
        return bleGattHelper;
    }

    private BleGattHelper(Context context) {
        this.context = context;
    }

    private void setBleGattListener(BleGattHelperListener listener) {
        this.gattHelperListener = listener;
    }

    public void onBLEServiceFound(LocalDeviceEntity device, BluetoothGatt gatt, List<BluetoothGattService> list) {
    }

    public void onBLEDeviceConnected(LocalDeviceEntity device, BluetoothGatt gatt) {
    }

    public void onBLEDeviceDisConnected(LocalDeviceEntity device, BluetoothGatt gatt) {
    }

    public void onCharacteristicRead(LocalDeviceEntity device, BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, boolean success) {
    }

    public void onCharacteristicChanged(LocalDeviceEntity device, BluetoothGatt gatt, String uuid, byte[] value) {
        if (uuid.equals(DeviceConfig.HEARTRATE_FOR_TIRED_NOTIFY.toString())) {
            this.gattHelperListener.onDeviceStateChangeUI(device, gatt, uuid, value);
        } else if (uuid.equals(DeviceConfig.UUID_CHARACTERISTIC_NOTIFY.toString())) {
            BleNotifyParse.getInstance().doParse(this.context, value);
        }
    }

    public void onCharacteristicWrite(LocalDeviceEntity device, BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, boolean success) {
    }

    public void onDescriptorRead(LocalDeviceEntity device, BluetoothGatt gatt, BluetoothGattDescriptor bd, boolean success) {
    }

    public void onDescriptorWrite(LocalDeviceEntity device, BluetoothGatt gatt, BluetoothGattDescriptor bd, boolean success) {
    }

    public void onNoBLEServiceFound() {
    }

    public void onBLEDeviceConnecError(LocalDeviceEntity device, boolean showToast, boolean fromServer) {
        this.gattHelperListener.onDeviceConnectedChangeUI(device, showToast, fromServer);
    }

    public void onReadRemoteRssi(LocalDeviceEntity device, BluetoothGatt gatt, int rssi, boolean success) {
    }

    public void onReliableWriteCompleted(LocalDeviceEntity device, BluetoothGatt gatt, boolean success) {
    }

    public void onMTUChange(BluetoothGatt gatt, int mtu, int status) {
    }
}

