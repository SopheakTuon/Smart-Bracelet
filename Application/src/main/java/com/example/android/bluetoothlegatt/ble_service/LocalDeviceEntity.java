package com.example.android.bluetoothlegatt.ble_service;

import java.io.Serializable;

/**
 * @author Sopheak Tuon
 * @created on 02-Oct-17
 */

public class LocalDeviceEntity implements Serializable {
    private static final long serialVersionUID = 6349262396991397672L;
    private String mAddress;
    private String mName = "";
    private byte[] mScanRecord;
    private int rssi;

    public LocalDeviceEntity(String name, String addr, int rssi, byte[] scanRecord) {
        this.mName = name;
        this.mAddress = addr;
        this.rssi = rssi;
        this.mScanRecord = scanRecord;
        setDefaultConfig();
    }

    public int getRssi() {
        return this.rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public String getName() {
        return this.mName;
    }

    public String getAddress() {
        return this.mAddress;
    }

    public byte[] getmScanRecord() {
        return this.mScanRecord;
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (this == other) {
            return true;
        }
        if (!(other instanceof LocalDeviceEntity)) {
            return false;
        }
        if (this.mAddress.equals(((LocalDeviceEntity) other).getAddress())) {
            return true;
        }
        return false;
    }

    private void setDefaultConfig() {
    }

    public String toString() {
        return this.mName;
    }
}
