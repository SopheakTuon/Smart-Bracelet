/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.bluetoothlegatt.ui;

import android.app.Activity;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import com.example.android.bluetoothlegatt.R;
import com.example.android.bluetoothlegatt.SampleGattAttributes;
import com.example.android.bluetoothlegatt.ble_service.BleDataForBattery;
import com.example.android.bluetoothlegatt.ble_service.BleDataForOnLineMovement;
import com.example.android.bluetoothlegatt.ble_service.BleGattHelper;
import com.example.android.bluetoothlegatt.ble_service.BleGattHelperListener;
import com.example.android.bluetoothlegatt.ble_service.BluetoothLeService;
import com.example.android.bluetoothlegatt.ble_service.DataSendCallback;
import com.example.android.bluetoothlegatt.ble_service.LocalDataSaveTool;
import com.example.android.bluetoothlegatt.ble_service.LocalDeviceEntity;
import com.example.android.bluetoothlegatt.ble_service.OutLineDataEntity;
import com.example.android.bluetoothlegatt.ble_service.util.DateUtils;
import com.example.android.bluetoothlegatt.manager.CommandManager;
import com.example.android.bluetoothlegatt.models.BroadcastData;
import com.example.android.bluetoothlegatt.util.FormatUtils;
import com.example.android.bluetoothlegatt.util.command.WriteCommand;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * For a given BLE device, this Activity provides the user interface to connect, display data,
 * and display GATT services and characteristics supported by the device.  The Activity
 * communicates with {@code BluetoothLeService}, which in turn interacts with the
 * Bluetooth LE API.
 */
public class DeviceControlActivity extends Activity {
    private final static String TAG = DeviceControlActivity.class.getSimpleName();

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    private TextView mConnectionState;
    private TextView mDataField, tvTimer, mByteData, textViewBattery;
    private String mDeviceName;
    private String mDeviceAddress;
    private ExpandableListView mGattServicesList;
    private BluetoothLeService mBluetoothLeService;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
            new ArrayList<>();
    private boolean mConnected = false;
    private BluetoothGattCharacteristic mNotifyCharacteristic;

    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";

    private Button buttonBP, buttonSP02, buttonHeart, buttonBreathRate;

    private boolean isMeasuring;

    private static int timeMeasure = 0;

    private boolean isHR = false;

    CommandManager mManager;

    private Handler mHandler = new GetDataHandler();
    private String showResult;

    DataSendCallback sendCallback = new C17047();
    private boolean isFirstData;
    private boolean isMovementing;
    private boolean isPauseing;
    private long beginTime;
    private Object movementBegginTime;

    private OutLineDataEntity movementEntity;


    private int eachMinuteCount;
    private int timesCount = 60;

    class GetDataHandler extends Handler {
        GetDataHandler() {
        }

        public void handleMessage(Message msg) {
//            GetEachHourTask task = new GetEachHourTask();
            Integer[] numArr;
            switch (msg.what) {
                case 0:
//                    MainActivity.this.synchronizeTime();
//                    MainActivity.this.listenerOfDeviceUI();
                    return;
                case 1:
//                    MainActivity.this.getHardVerwion();
                    return;
                case 2:
//                    MainActivity.this.getAndShowBattary(msg);
                    return;
                case 3:
//                    final int arg = msg.arg1;
//                    if (!MainActivity.this.closeTheRequest && BluetoothLeService.getInstance() != null && BluetoothLeService.getInstance().isConnectedDevice()) {
//                        BleDataForDayData.getDayDataInstance(MainActivity.this.getApplicationContext()).setOnDayDataListener(new DataSendCallback() {
//                            public void sendSuccess(byte[] receveData) {
//                                DayDataDealer dayDataDealer = new DayDataDealer(MainActivity.this, receveData);
//                                if (MainActivity.this.conectState != null && MainActivity.this.conectState.getText().toString().equals(MainActivity.this.getString(C1560R.string.datasynchronize)) && MainActivity.this.conectState.getVisibility() == 0) {
//                                    MainActivity.this.showConnectState(8);
//                                }
//                            }
//
//                            public void sendFailed() {
//                                if (MainActivity.this.conectState != null && MainActivity.this.conectState.getText().toString().equals(MainActivity.this.getString(C1560R.string.datasynchronize)) && MainActivity.this.conectState.getVisibility() == 0) {
//                                    MainActivity.this.showConnectState(9);
//                                }
//                            }
//
//                            public void sendFinished() {
//                                if (MainActivity.this.state == 8 || MainActivity.this.state == 9) {
//                                    MainActivity.this.showConnectState(10);
//                                }
//                                if (arg == 0) {
//                                    Message msg = MainActivity.this.mHandler.obtainMessage();
//                                    msg.what = 4;
//                                    msg.arg1 = 0;
//                                    MainActivity.this.mHandler.sendMessageDelayed(msg, 0);
//                                }
//                            }
//                        });
//                        BleDataForDayData.getDayDataInstance(MainActivity.this.getApplicationContext()).getDayData();
//                        return;
//                    }
                    return;
                case 4:
//                    numArr = new Integer[]{Integer.valueOf(2), Integer.valueOf(msg.arg1)};
//                    if (task instanceof AsyncTask) {
//                        AsyncTaskInstrumentation.execute(task, numArr);
//                        return;
//                    } else {
//                        task.execute(numArr);
//                        return;
//                    }
                case 5:
//                    numArr = new Integer[]{Integer.valueOf(1), Integer.valueOf(msg.arg1)};
//                    if (task instanceof AsyncTask) {
//                        AsyncTaskInstrumentation.execute(task, numArr);
//                        return;
//                    } else {
//                        task.execute(numArr);
//                        return;
//                    }
                case 6:
//                    numArr = new Integer[]{Integer.valueOf(4), Integer.valueOf(0)};
//                    if (task instanceof AsyncTask) {
//                        AsyncTaskInstrumentation.execute(task, numArr);
//                        return;
//                    } else {
//                        task.execute(numArr);
//                        return;
//                    }
                case 9:
//                    MainActivity.this.requestHrWarningData();
//                    MainActivity.this.getCheckFrameData();
                    return;
                case 10:
//                    MainActivity.this.getFatigueData();
                    return;
                case 11:
//                    MainActivity.this.settingArges();
//                    MainActivity.this.checkInfo();
                    return;
                case 12:
//                    MainActivity.this.connectTheSaveDevice(true);
                    return;
                case 13:
//                    MainActivity.this.setDeviceLangue();
                    return;
                default:
                    return;
            }
        }
    }


    private Handler movementHandler = new C16941();

    class C16941 extends Handler {
        C16941() {
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                if (msg.arg1 == 6) {
                    DeviceControlActivity.this.isFirstData = true;
                } else {
//                    Movement_Fragment.this.endTime = (long) DateUtils.currentTimeSeconds();
//                    Movement_Fragment.this.movementEndTime = Movement_Fragment.this.formateTime(Movement_Fragment.this.endTime);
//                    Movement_Fragment.this.keepMovementTime = (Movement_Fragment.this.endTime - Movement_Fragment.this.beginTime) - Movement_Fragment.this.keepPauseTime;
//                    Log.i("Movement_Fragment", "运动数据：beginTime:" + Movement_Fragment.this.beginTime + "\t\tendTime:" + Movement_Fragment.this.endTime + "\t\tkeepPauseTime:" + Movement_Fragment.this.keepMovementTime);
//                    Log.i("Movement_Fragment", "运动数据：pauseBeginTime:" + Movement_Fragment.this.pauseBeginTime + "\t\tpauseEndTime:" + Movement_Fragment.this.pauseEndTime);
//                    Movement_Fragment.this.move_keep_time.setText(Movement_Fragment.this.formatKeepTime(Movement_Fragment.this.keepMovementTime));
                }
                BleDataForOnLineMovement.getBleDataForOutlineInstance().sendHRDataToDevice((byte) 0);
                removeMessages(0);
                Message mzg = obtainMessage();
                mzg.what = 0;
                mzg.arg1 = 1;
//                if (Movement_Fragment.this.isHide) {
//                    sendMessageDelayed(mzg, 20000);
//                } else {
//                    sendMessageDelayed(mzg, 1000);
//                }
                sendMessageDelayed(mzg, 1000);
            }
        }
    }


    class C17047 implements DataSendCallback {
        C17047() {
        }

        public void sendSuccess(final byte[] receveData) {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (receveData[0] == (byte) 0) {
                        int hrValue = receveData[1] & 255;
                        int stepValue = FormatUtils.byte2Int(receveData, 2);
                        int mileValue = FormatUtils.byte2Int(receveData, 6);
                        int kcalValue = FormatUtils.byte2Int(receveData, 10);
                        int paceValue = receveData[14] & 255;
                        Log.d("Value", hrValue + " " + stepValue + " " + kcalValue + " " + mileValue + " " + paceValue);

                        StringBuilder stringBuilder = new StringBuilder("");
                        stringBuilder.append("Heart Rate : ");
                        stringBuilder.append(hrValue + "\n");
                        stringBuilder.append("Step : ");
                        stringBuilder.append(stepValue + "\n");
                        stringBuilder.append("Calories : ");
                        stringBuilder.append(kcalValue);

                        textViewBattery.setText(stringBuilder.toString());

                        if (DeviceControlActivity.this.isFirstData) {
//                            StringBuilder stringBuilder = new StringBuilder("");
//                            stringBuilder.append("Heart Rate : ");
//                            stringBuilder.append(hrValue + "\n");
//                            stringBuilder.append("Step : ");
//                            stringBuilder.append(stepValue + "\n");
//                            stringBuilder.append("Calories : ");
//                            stringBuilder.append(kcalValue);
//
//                            textViewBattery.setText(stringBuilder.toString());

                            LocalDataSaveTool.getInstance(DeviceControlActivity.this).writeSp("movement_step", String.valueOf(stepValue));
                            LocalDataSaveTool.getInstance(DeviceControlActivity.this).writeSp("movement_kcal", String.valueOf(kcalValue));
                            LocalDataSaveTool.getInstance(DeviceControlActivity.this).writeSp("movement_hr", String.valueOf(hrValue));
                            DeviceControlActivity.this.beginTime = (long) DateUtils.currentTimeSeconds();
                            DeviceControlActivity.this.movementBegginTime = DeviceControlActivity.this.formateTime(DeviceControlActivity.this.beginTime);
                            DeviceControlActivity.this.isFirstData = false;
                            DeviceControlActivity.this.isMovementing = true;
                            DeviceControlActivity.this.isPauseing = false;
                            return;
                        }
//                        if (DeviceControlActivity.this.eachMinuteCount >= DeviceControlActivity.this.timesCount) {
//                            DeviceControlActivity.this.eachMinuteCount = 0;
//                        }
//                        DeviceControlActivity.this.eachMinuteCount = DeviceControlActivity.this.eachMinuteCount + 1;
//                        int steps = Integer.valueOf(LocalDataSaveTool.getInstance(DeviceControlActivity.this).readSp("movement_step")).intValue();
//                        int kcals = Integer.valueOf(LocalDataSaveTool.getInstance(DeviceControlActivity.this).readSp("movement_kcal")).intValue();
//                        int hrs = Integer.valueOf(LocalDataSaveTool.getInstance(DeviceControlActivity.this).readSp("movement_hr")).intValue();
//                        Log.i("Movement_Fragment", "movement::steps原始" + steps + "新" + stepValue + "--原始kcals;" + kcals + "新" + kcalValue);
//                        String moveSteps = String.valueOf(stepValue - steps);
//                        String moveKcal = String.valueOf(kcalValue - kcals);
//                        String moveHr = String.valueOf(hrValue);
//                        DeviceControlActivity.this.movementEntity.setStepCount(Integer.valueOf(moveSteps).intValue());
//                        DeviceControlActivity.this.movementEntity.setCalorie(Integer.valueOf(moveKcal).intValue());
//                        if (DeviceControlActivity.this.eachMinuteCount == 1) {
//                            String hr;
//                            if (moveHr == null || moveHr.equals("") || moveHr.equals("null")) {
//                                hr = FormatUtils.byteToHexStringUn0X((byte) 0);
//                            } else {
//                                hr = FormatUtils.byteToHexStringUn0X(receveData[1]);
//                            }
//                            DeviceControlActivity.this.movementEntity.setHeartReat(DeviceControlActivity.this.movementEntity.getHeartReat() + hr);
//                            Log.i("Movement_Fragment", "movement::hr:" + DeviceControlActivity.this.movementEntity.getHeartReat() + hr);
//                        }
//                        DeviceControlActivity.this.move_step.setText(moveSteps);
//                        DeviceControlActivity.this.move_kcal.setText(moveKcal);
//                        DeviceControlActivity.this.move_hr.setText(moveHr);
//                        DeviceControlActivity.this.pro.setProgress((int) Movement_Fragment.this.getProgress(DeviceControlActivity.this.keepMovementTime));

//                        StringBuilder stringBuilder1 = new StringBuilder("");
//                        stringBuilder1.append("Heart Rate : ");
//                        stringBuilder1.append(moveHr + "\n");
//                        stringBuilder1.append("Step : ");
//                        stringBuilder1.append(moveSteps + "\n");
//                        stringBuilder1.append("Calories : ");
//                        stringBuilder1.append(moveKcal);
//
//                        textViewBattery.setText(stringBuilder1.toString());
                    } else if (receveData[0] == (byte) 1) {
                        Message mzg = DeviceControlActivity.this.movementHandler.obtainMessage();
                        mzg.what = 0;
                        mzg.arg1 = 6;
                        DeviceControlActivity.this.movementHandler.sendMessage(mzg);
                    }
                }
            });
        }

        public void sendFailed() {
        }

        public void sendFinished() {
        }
    }

    private String formateTime(long beginTime) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date(1000 * beginTime));
    }


    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };


    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                updateConnectionState(R.string.connected);
                invalidateOptionsMenu();
                enableElements(true);
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                updateConnectionState(R.string.disconnected);
                invalidateOptionsMenu();
                clearUI();
                enableElements(false);
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                BluetoothLeService.getInstance().addCallback(BleGattHelper.getInstance(DeviceControlActivity.this.getApplicationContext(), new GattHelperListener()));
//                displayGattServices(mBluetoothLeService.getSupportedGattServices());
//                getAndShowBattary(null);

                DeviceControlActivity.this.movementEntity = new OutLineDataEntity();
                BleDataForOnLineMovement.getBleDataForOutlineInstance().setOnSendRecever(DeviceControlActivity.this.sendCallback);
                BleDataForOnLineMovement.getBleDataForOutlineInstance().sendHRDataToDevice((byte) 1);
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA) + " BPM");
            }
        }


    };

    private void displayByteDate(BroadcastData broadcastData) {
        mByteData.setText(bytesToByteString(broadcastData.getReceives()));
    }

    private void displayByteDate(String data) {
        mByteData.setText(data);
    }

    private String bytesToByteString(byte[] bytes) {
        String btyesString = "";
        for (int i = 0; i < bytes.length; i++) {
            btyesString += " " + bytes[i];
        }
        return btyesString;
    }


    private final ExpandableListView.OnChildClickListener servicesListClickListner =
            new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                            int childPosition, long id) {
                    if (mGattCharacteristics != null) {
                        final BluetoothGattCharacteristic characteristic =
                                mGattCharacteristics.get(groupPosition).get(childPosition);
                        final int charaProp = characteristic.getProperties();
                        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                            // If there is an active notification on a characteristic, clear
                            // it first so it doesn't update the data field on the user interface.
                            if (mNotifyCharacteristic != null) {
                                mBluetoothLeService.setCharacteristicNotification(mNotifyCharacteristic, false);
                                mNotifyCharacteristic = null;
                            }
                            mBluetoothLeService.readCharacteristic(characteristic);
                        }
                        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                            mNotifyCharacteristic = characteristic;
                            mBluetoothLeService.setCharacteristicNotification(characteristic, true);
                        }
                        return true;
                    }
                    return false;
                }
            };

    private void clearUI() {
        mGattServicesList.setAdapter((SimpleExpandableListAdapter) null);
        mDataField.setText(R.string.no_data);
        mByteData.setText(R.string.no_data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gatt_services_characteristics);
        mManager = CommandManager.getInstance(this);
        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);

        // Sets up UI references.
        ((TextView) findViewById(R.id.device_address)).setText(mDeviceAddress);
        mGattServicesList = (ExpandableListView) findViewById(R.id.gatt_services_list);
        mGattServicesList.setOnChildClickListener(servicesListClickListner);
        mConnectionState = (TextView) findViewById(R.id.connection_state);
        mDataField = (TextView) findViewById(R.id.data_value);
        textViewBattery = (TextView) findViewById(R.id.textViewBattery);
        mByteData = (TextView) findViewById(R.id.byte_data_value);
        tvTimer = (TextView) findViewById(R.id.tvTimer);

        getActionBar().setTitle(mDeviceName);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

        init();
        initEventListener();


    }

    private void enableElements(final boolean enable) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                buttonBP.setEnabled(enable);
                buttonSP02.setEnabled(enable);
                buttonHeart.setEnabled(enable);
                buttonBreathRate.setEnabled(enable);
            }
        });
    }


    private void init() {
        buttonBP = (Button) findViewById(R.id.buttonBP);
        buttonSP02 = (Button) findViewById(R.id.buttonSPO2);
        buttonHeart = (Button) findViewById(R.id.buttonHeart);
        buttonBreathRate = (Button) findViewById(R.id.buttonBreathRate);
        enableElements(false);
    }

    private void initEventListener() {
        buttonBP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startMeasureBP();
//                enableElements(false);
            }
        });

        buttonSP02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startMeasureSPO2();
//                enableElements(false);
            }
        });
        buttonHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        startMeasureHr();
//                        enableElements(false);
                    }
                });
            }
        });
        buttonBreathRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unBindDevice();
        } catch (Exception e) {
            e.printStackTrace();
        }
        BleDataForOnLineMovement.getBleDataForOutlineInstance().sendHRDataToDevice((byte) 2);
        mManager.realTimeAndOnceMeasure(10, 0);
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gatt_services, menu);
        if (mConnected) {
            menu.findItem(R.id.menu_connect).setVisible(false);
            menu.findItem(R.id.menu_disconnect).setVisible(true);
        } else {
            menu.findItem(R.id.menu_connect).setVisible(true);
            menu.findItem(R.id.menu_disconnect).setVisible(false);
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_connect:
                mBluetoothLeService.connect(mDeviceAddress);
                return true;
            case R.id.menu_disconnect:
                mBluetoothLeService.disconnect();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateConnectionState(final int resourceId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mConnectionState.setText(resourceId);
            }
        });
    }


    private void displayTimer(final String data) {
        if (data != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvTimer.setText(data);
                }
            });
        }
    }

    private void displayData(final String data) {
        if (data != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDataField.setText(data);
                }
            });
        }
    }

    // Demonstrates how to iterate through the supported GATT Services/Characteristics.
    // In this sample, we populate the data structure that is bound to the ExpandableListView
    // on the UI.
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) {
            return;
        }
        String uuid = null;
        String unknownServiceString = getResources().getString(R.string.unknown_service);
        String unknownCharaString = getResources().getString(R.string.unknown_characteristic);
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<>();
        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData = new ArrayList<>();
        mGattCharacteristics = new ArrayList<>();
//        String allServiceUUID = "";
        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData = new HashMap<>();
            uuid = gattService.getUuid().toString();
//            Log.d("Service", "UUID : " + uuid + "\n" + "CMD : " + gattService.getType() + "\n" + "InstanceId : " + gattService.getInstanceId());
//            allServiceUUID += uuid + "\n";
            currentServiceData.put(LIST_NAME, SampleGattAttributes.lookup(uuid, unknownServiceString));
            currentServiceData.put(LIST_UUID, uuid);
            gattServiceData.add(currentServiceData);

            ArrayList<HashMap<String, String>> gattCharacteristicGroupData = new ArrayList<>();
            List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
            ArrayList<BluetoothGattCharacteristic> charas = new ArrayList<>();

            String stringChar = "Service UUID  = " + uuid + "\n";
            // Loops through available Characteristics.
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                HashMap<String, String> currentCharaData = new HashMap<>();
                uuid = gattCharacteristic.getUuid().toString();
                stringChar += uuid + "\n";
                currentCharaData.put(LIST_NAME, SampleGattAttributes.lookup(uuid, unknownCharaString));
                currentCharaData.put(LIST_UUID, uuid);
                gattCharacteristicGroupData.add(currentCharaData);
            }
            Log.d("UUID", stringChar);
            mGattCharacteristics.add(charas);
            gattCharacteristicData.add(gattCharacteristicGroupData);
        }

        SimpleExpandableListAdapter gattServiceAdapter = new SimpleExpandableListAdapter(
                this,
                gattServiceData,
                android.R.layout.simple_expandable_list_item_2,
                new String[]{LIST_NAME, LIST_UUID},
                new int[]{android.R.id.text1, android.R.id.text2},
                gattCharacteristicData,
                android.R.layout.simple_expandable_list_item_2,
                new String[]{LIST_NAME, LIST_UUID},
                new int[]{android.R.id.text1, android.R.id.text2}
        );
        mGattServicesList.setAdapter(gattServiceAdapter);
//        mManager.setTimeSync();
    }

    /**
     * @return return 1 : - 1
     */
    private int unBindDevice() throws Exception {
        return WriteCommand.unbindDevice(mBluetoothLeService);
    }


//    private void startMeasureHr() {
//        isHR = true;
//        displayByteDate("Collecting data...");
//        displayData("Collecting data...");
//        enableElements(false);
////        mManager.realTimeAndOnceMeasure(10, 1);
////        WriteCommand.openHeartRateMonitor();
////        WriteCommand.realTimeHeartRateMonitor();
//        isMeasuring = true;
//    }

//    private void stopMeasureHr() {
//        mManager.realTimeAndOnceMeasure(10, 0);
//        enableElements(true);
//    }

//    private void startMeasureBP() {
//        displayByteDate("Collecting data...");
//        displayData("Collecting data...");
//        enableElements(false);
//        mManager.realTimeAndOnceMeasure(34, 1);
//    }

//    private void stopMeasureBP() {
//        mManager.realTimeAndOnceMeasure(34, 0);
//        enableElements(true);
//    }

//    private void startMeasureSPO2() {
//        displayByteDate("Collecting data...");
//        displayData("Collecting data...");
//        enableElements(false);
//        mManager.realTimeAndOnceMeasure(18, 1);
//    }

//    private void stopMeasureSPO2() {
//        mManager.realTimeAndOnceMeasure(18, 0);
//        enableElements(true);
//    }

    /**
     * Stop measure
     *
     * @return
     */
//    private int stopMeasure() {
//        timeMeasure = 0;
//        isMeasuring = false;
//        enableElements(true);
//        int result = WriteCommand.stopMeasuring(mBluetoothLeService);
//        /**
//         * Turn off notification of PW
//         */
//
//        return result;
//    }

    /**
     * Stop Measure PW
     *
     * @return return 1 : -1
     */
//    private int stopMeasurePW() {
//        /**
//         * Turn off notification of PW
//         */
//        BluetoothGattCharacteristic bluetoothGattCharacteristic = mBluetoothLeService.getBluetoothGatt().getService(UUID.fromString("0aabcdef-1111-2222-0000-facebeadaaaa")).getCharacteristic(UUID.fromString("facebead-ffff-eeee-0002-facebeadaaaa"));
//        mBluetoothLeService.getBluetoothGatt().setCharacteristicNotification(bluetoothGattCharacteristic, false);
//        BluetoothGattService bluetoothGattService = mBluetoothLeService.getBluetoothGatt().getService(UUID.fromString("0aabcdef-1111-2222-0000-facebeadaaaa"));
//        BluetoothGattCharacteristic bluetoothGattCharacteristic1 = bluetoothGattService.getCharacteristic(UUID.fromString("facebead-ffff-eeee-0005-facebeadaaaa"));
//        mBluetoothLeService.getBluetoothGatt().setCharacteristicNotification(bluetoothGattCharacteristic1, false);
//        BluetoothGattCharacteristic bluetoothGattCharacteristic2 = bluetoothGattService.getCharacteristic(UUID.fromString("ffacebead-ffff-eeee-0004-facebeadaaaa"));
//        mBluetoothLeService.getBluetoothGatt().setCharacteristicNotification(bluetoothGattCharacteristic2, false);
//        return stopMeasure();
//    }
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    class GattHelperListener implements BleGattHelperListener {
        GattHelperListener() {
        }

        public void onDeviceStateChangeUI(LocalDeviceEntity device, BluetoothGatt gatt, String uuid, final byte[] value) {
            DeviceControlActivity.this.mHandler.post(new Runnable() {
                public void run() {
                    int liveHR = value[1] & 255;
                    if (liveHR == 0) {
                        DeviceControlActivity.this.showResult = "--";
                    } else {
                        DeviceControlActivity.this.showResult = String.valueOf(liveHR);
                    }
                    displayData("HR : " + showResult + " BPM");
                }
            });
        }

        public void onDeviceConnectedChangeUI(final LocalDeviceEntity device, boolean showToast, final boolean fromServer) {
        }
    }

    private void getAndShowBattary(Message msg) {
//        if (!this.closeTheRequest && BluetoothLeService.getInstance() != null && BluetoothLeService.getInstance().isConnectedDevice()) {
//            final int ar = msg.arg1;
//            Log.i(TAG, "message.arg1:" + ar);
        BleDataForBattery getB = BleDataForBattery.getInstance();
        getB.setBatteryListener(new DataSendCallback() {
            public void sendSuccess(final byte[] receveData) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textViewBattery.setText(String.valueOf(receveData[0] & 255) + "%");
                    }
                });
            }

            public void sendFailed() {
            }

            public void sendFinished() {
//                    if (ar == 0) {
//                        MainActivity.this.mHandler.sendEmptyMessageDelayed(9, 0);
//                    }
            }
        });
        getB.getBatteryPx();
//        }
    }

}
