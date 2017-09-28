package com.example.android.bluetoothlegatt.ble_service;

/**
 * @author Sopheak Tuon
 * @created on 28-Sep-17
 */

public class BleDataForFrame extends BleBaseDataManage {
    private static BleDataForFrame bleBAttery;
    public static byte fromDevice = (byte) -78;
    public static byte fromDevice3 = (byte) -77;
    private static int mCurrentBattery = -1;
    public static byte toDevice = (byte) 50;
    public static byte toDevice3 = (byte) 51;
    private final int GET_BLE_FRAME = 0;
    private final int GET_BLE_PARAMS = 1;
    private final int GET_BLE_WEATHER = 2;
//    private DataSendCallback battListerer;
//    private DataSendCallback battListerer3;
//    private Handler batteryHandler = new C14521();
    private boolean hasComm = false;
    private boolean isSendOk = false;
    private boolean isSendOk1 = false;
    private int sendCount = 0;

//    class C14521 extends Handler {
//        C14521() {
//        }
//
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 0:
//                    if (BleDataForFrame.this.isSendOk) {
//                        BleDataForFrame.this.stopSendData(this, 0);
//                        return;
//                    } else if (BleDataForFrame.this.sendCount < 4) {
//                        BleDataForFrame.this.continueSend(this, msg);
//                        BleDataForFrame.this.getCheckFrameFromBr();
//                        return;
//                    } else {
//                        BleDataForFrame.this.stopSendData(this, 0);
//                        return;
//                    }
//                case 1:
//                    if (BleDataForFrame.this.isSendOk1) {
//                        BleDataForFrame.this.stopSendData(this, 1);
//                        return;
//                    } else if (BleDataForFrame.this.sendCount < 4) {
//                        BleDataForFrame.this.continueSend(this, msg);
//                        BleDataForFrame.this.getSupportParamFromBr();
//                        return;
//                    } else {
//                        BleDataForFrame.this.stopSendData(this, 1);
//                        return;
//                    }
//                default:
//                    return;
//            }
//        }
//    }
//
//    public static BleDataForFrame getInstance() {
//        if (bleBAttery == null) {
//            synchronized (BleDataForFrame.class) {
//                if (bleBAttery == null) {
//                    bleBAttery = new BleDataForFrame();
//                }
//            }
//        }
//        return bleBAttery;
//    }
//
//    private void continueSend(Handler handler, Message msges) {
//        Message msg = handler.obtainMessage();
//        msg.what = msges.what;
//        msg.arg1 = msges.arg1;
//        msg.arg2 = msges.arg2;
//        handler.sendMessageDelayed(msg, (long) SendLengthHelper.getSendLengthDelay(msges.arg1, msges.arg2));
//        this.sendCount++;
//    }
//
//    private void stopSendData(Handler handler, int comm) {
//        handler.removeMessages(comm);
//        if (!(this.isSendOk && this.isSendOk1)) {
//            this.battListerer.sendFailed();
//        }
//        this.battListerer.sendFinished();
//        this.isSendOk = false;
//        this.isSendOk1 = false;
//        this.sendCount = 0;
//    }
//
//    private BleDataForFrame() {
//    }
//
//    public void getCheckFrame() {
//        int sendLength = getCheckFrameFromBr();
//        Message msg = this.batteryHandler.obtainMessage();
//        msg.what = 0;
//        msg.arg1 = sendLength;
//        msg.arg2 = 7;
//        this.batteryHandler.sendMessageDelayed(msg, (long) SendLengthHelper.getSendLengthDelay(sendLength, 20));
//    }
//
//    public void getSupportParam() {
//        int sendLength = getSupportParamFromBr();
//        Message msg = this.batteryHandler.obtainMessage();
//        msg.what = 1;
//        msg.arg1 = sendLength;
//        msg.arg2 = 20;
//        this.batteryHandler.sendMessageDelayed(msg, (long) SendLengthHelper.getSendLengthDelay(sendLength, 20));
//    }
//
//    private int getCheckFrameFromBr() {
//        byte[] bytes = new byte[]{(byte) 1, (byte) 3, (byte) 4, (byte) 5};
//        return setMsgToByteDataAndSendToDevice(toDevice, bytes, bytes.length);
//    }
//
//    private int getSupportParamFromBr() {
//        byte[] bytes = new byte[]{(byte) 2, (byte) 1, (byte) 2};
//        return setMsgToByteDataAndSendToDevice(toDevice, bytes, bytes.length);
//    }
//
//    public void dealReceData(byte[] data) {
//        System.out.println("333333333333" + FormatUtils.bytesToHexString(data));
//        if (data[0] == (byte) 1) {
//            this.isSendOk = true;
//        }
//        if (data[0] == (byte) 2) {
//            this.isSendOk1 = true;
//        }
//        this.battListerer.sendSuccess(data);
//    }
//
//    public void dealReceB3(byte[] data) {
//        byte[] copyData = new byte[(data.length - 2)];
//        byte[] reqData = new byte[(data.length - 2)];
//        System.arraycopy(data, 0, copyData, 0, data.length - 2);
//        for (int i = 0; i < copyData.length; i += 2) {
//            reqData[i] = copyData[i];
//        }
//        Log.i("", "dealReceData :" + FormatUtils.bytesToHexString(reqData));
//        setMsgToByteDataAndSendToDevice(toDevice3, reqData, reqData.length);
//        if (this.battListerer != null) {
//            this.battListerer3.sendSuccess(data);
//        }
//    }
//
//    public static int getmCurrentBattery() {
//        return mCurrentBattery;
//    }
//
//    public void setCheckFrameListener(DataSendCallback batteryCallback) {
//        this.battListerer = batteryCallback;
//    }
//
//    public void setCheckFrameListener3(DataSendCallback batteryCallback) {
//        this.battListerer3 = batteryCallback;
//    }
}
