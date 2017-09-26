package com.example.android.bluetoothlegatt.ble_service;

import android.text.TextUtils;

import com.example.android.bluetoothlegatt.util.FormatUtils;

import java.util.UUID;

/**
 * @author Sopheak Tuon
 * @created on 26-Sep-17
 */

public class DeviceConfig {
    public static final String DEVICE_ADDRESS = "device address";
    public static final String DEVICE_ADDRESS_COPY = "device address_copy";
    public static final String DEVICE_CONNECTE_AND_NOTIFY_SUCESSFUL = "device_connecte_and_notify_ok";
    public static final String DEVICE_CONNECTING_AUTO = "device_connecting_auto";
    public static final String[] DEVICE_FILTER_NAME = new String[]{"HR", "FishAlert"};
    public static final String DEVICE_NAME = "device name";
    public static final UUID HEARTRATE_FOR_TIRED_NOTIFY = UUID.fromString(FormatUtils.convert16to128UUID("2A37"));
    public static final UUID HEARTRATE_SERVICE_UUID = UUID.fromString(FormatUtils.convert16to128UUID("180D"));
    public static final UUID MAIN_SERVICE_UUID = UUID.fromString(FormatUtils.convert16to128UUID("FFF0"));
    public static final UUID UUID_CHARACTERISTIC_NOTIFY = UUID.fromString(FormatUtils.convert16to128UUID("FFF1"));
    public static final UUID UUID_CHARACTERISTIC_WR = UUID.fromString(FormatUtils.convert16to128UUID("FFF2"));

    public static boolean isDeviceNameOk(String devicename) {
        if (TextUtils.isEmpty(devicename)) {
            return false;
        }
        return true;
    }
}
