package com.example.android.bluetoothlegatt.ble_service;

import android.bluetooth.BluetoothGatt;
import android.content.Context;

import java.util.HashMap;
import java.util.Vector;

/**
 * @author Sopheak Tuon
 * @created on 03-Oct-17
 */

public class Engine {
    private static Engine instance = null;
    private static final Object locker = new Object();
    private HashMap<BluetoothGatt, LocalDeviceEntity> mDeviceMap = new HashMap();
    private Vector<LocalDeviceEntity> mFindedDeviceList = new Vector();
    private Vector<LocalDeviceEntity> mNearbyList;

    public static Engine getInstance() {
        if (instance == null) {
            synchronized (locker) {
                if (instance == null) {
                    instance = new Engine();
                }
            }
        }
        return instance;
    }

    public void init(Context context) {
        if (this.mNearbyList == null) {
            this.mNearbyList = new Vector();
        }
    }

    public Vector<LocalDeviceEntity> getFindedDeviceList() {
        return this.mFindedDeviceList;
    }

    public void addFindedDeviceList(LocalDeviceEntity dev) {
        this.mFindedDeviceList.add(dev);
    }

    public Vector<LocalDeviceEntity> getNearbyDeviceList() {
        return this.mNearbyList;
    }

    public boolean addGatt2DeviceMap(BluetoothGatt gatt, LocalDeviceEntity device) {
        if (this.mDeviceMap.containsKey(gatt) || this.mDeviceMap.containsValue(device)) {
            return false;
        }
        this.mDeviceMap.put(gatt, device);
        return true;
    }

    public boolean removeGattFromDeviceMap(BluetoothGatt gatt) {
        if (!this.mDeviceMap.containsKey(gatt)) {
            return false;
        }
        this.mDeviceMap.remove(gatt);
        return true;
    }

    public synchronized LocalDeviceEntity getDeviceFromGatt(BluetoothGatt gatt) {
        return (LocalDeviceEntity) this.mDeviceMap.get(gatt);
    }

    public void close() {
        if (this.mFindedDeviceList != null) {
            this.mFindedDeviceList.clear();
        }
        if (this.mNearbyList != null) {
            this.mNearbyList.clear();
        }
        if (this.mDeviceMap != null) {
            this.mDeviceMap.clear();
        }
    }
}
