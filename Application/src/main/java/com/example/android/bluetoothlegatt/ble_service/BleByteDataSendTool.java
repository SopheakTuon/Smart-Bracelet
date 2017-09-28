package com.example.android.bluetoothlegatt.ble_service;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattService;

import java.util.ArrayList;

/**
 * @author Sopheak Tuon
 * @created on 28-Sep-17
 */

public class BleByteDataSendTool {
    private static final String TAG = "BleByteDataSendTool";
    public static final int TASK_SENDED_CONFORM = 2;
    public static final int TASK_SENDING = 1;
    public static final int TASK_WAITING = 0;
    private static BleByteDataSendTool mBleByteDataSendTool;
    private final ArrayList<stTask> mConformList = new ArrayList();
    private final ArrayList<stTask> mTaskList = new ArrayList();

    public class BeginSendTaskThread extends Thread {
        public void run() {
            super.run();
            while (true) {
                try {
                    synchronized (BleByteDataSendTool.this.mTaskList) {
                        if (BleByteDataSendTool.this.mTaskList.size() == 0) {
                            BleByteDataSendTool.this.mTaskList.wait();
                        }
                        if (BleByteDataSendTool.this.mTaskList.size() > 0) {
                            stTask task = (stTask) BleByteDataSendTool.this.mTaskList.get(0);
                            if (task.status == 0) {
                                task.status = 1;
                            }
                            boolean isSendOk = BleByteDataSendTool.this.writeDelayValue(task);
                            synchronized (BleByteDataSendTool.this.mConformList) {
                                BleByteDataSendTool.this.mConformList.add(task);
                                BleByteDataSendTool.this.mConformList.notifyAll();
                            }
                            BleByteDataSendTool.this.mTaskList.remove(0);
                        }
                    }
                    sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    public class BeginTaskWatchThread extends Thread {
        public void run() {
            super.run();
            while (true) {
                synchronized (BleByteDataSendTool.this.mConformList) {
                    if (BleByteDataSendTool.this.mConformList.size() == 0) {
                        try {
                            BleByteDataSendTool.this.mConformList.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                try {
                    sleep(10000);
                    synchronized (BleByteDataSendTool.this.mConformList) {
                        for (int i = 0; i < BleByteDataSendTool.this.mConformList.size(); i++) {
                            stTask task = (stTask) BleByteDataSendTool.this.mConformList.get(i);
                            if (1 == task.status) {
                                task.status++;
                            } else {
                                BleByteDataSendTool.this.mConformList.remove(i);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    public class stTask {
        int status;
        byte[] value;

        public stTask(byte[] mValue) {
            this.value = mValue;
        }
    }

    private BleByteDataSendTool() {
        new BeginSendTaskThread().start();
        new BeginTaskWatchThread().start();
    }

    public static BleByteDataSendTool getInstance() {
        if (mBleByteDataSendTool == null) {
            mBleByteDataSendTool = new BleByteDataSendTool();
        }
        return mBleByteDataSendTool;
    }

    public void judgeTheDataLengthAndAddToSendByteArray(byte[] value) {
        int count = (value.length + 19) / 20;
        if (count == 1) {
            synchronized (this.mTaskList) {
                this.mTaskList.add(new stTask(value));
                this.mTaskList.notifyAll();
            }
            return;
        }
        int alreadyUsed = 0;
        for (int i = 0; i < count; i++) {
            int length;
            int lastValue = value.length - alreadyUsed;
            if (lastValue > 20) {
                length = 20;
            } else {
                length = lastValue;
            }
            byte[] bytes = new byte[length];
            for (int j = 0; j < length; j++) {
                bytes[j] = value[alreadyUsed + j];
            }
            alreadyUsed += length;
            synchronized (this.mTaskList) {
                this.mTaskList.add(new stTask(bytes));
                this.mTaskList.notifyAll();
            }
        }
    }

    private boolean writeDelayValue(final stTask task) {
        final boolean[] booleenR = new boolean[1];
        BluetoothLeService serviceMain = BluetoothLeService.getInstance();
        if (serviceMain == null) {
            return false;
        }
        BluetoothGatt gatt = serviceMain.getBluetoothGatt();
        if (gatt == null) {
            return false;
        }
        BluetoothGattService service = gatt.getService(DeviceConfig.MAIN_SERVICE_UUID);
        if (service == null) {
            return false;
        }
        BluetoothLeService.getInstance().writeDelayValue(task.value, service.getCharacteristic(DeviceConfig.UUID_CHARACTERISTIC_WR), new BluetoothLeService.WriteCallBack() {
            public void onWrite(boolean result) {
                task.status = 2;
                booleenR[0] = result;
            }
        });
        try {
            Thread.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return booleenR[0];
    }
}
