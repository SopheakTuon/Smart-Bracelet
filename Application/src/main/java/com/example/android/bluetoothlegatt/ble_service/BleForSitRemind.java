package com.example.android.bluetoothlegatt.ble_service;

/**
 * @author Sopheak Tuon
 * @created on 04-Oct-17
 */

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.android.bluetoothlegatt.ble_service.entity.sitRemindEntity;

public class BleForSitRemind extends BleBaseDataManage {
    private static BleForSitRemind bleForSitRemind = null;
    public static final byte excepteionDevice = (byte) -44;
    public static final byte fromDevice = (byte) -108;
    public static final byte toDevice = (byte) 20;
    private final int DELETE_DATA = 2;
    private final int GET_SIT_DATA = 0;
    private final int SETTING_SIT_DATA = 1;
    private final String SIT_ENTITY = "sit entity";
    private int count = 0;
    private DataSendCallback dataRecever;
    private boolean isSitDataResqonse = false;
    private Handler sitHandler = new C14731();

    class C14731 extends Handler {
        C14731() {
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (BleForSitRemind.this.isSitDataResqonse) {
                        BleForSitRemind.this.closeSendData(this);
                        return;
                    } else if (BleForSitRemind.this.count < 4) {
                        BleForSitRemind.this.continueSendData(this, msg);
                        BleForSitRemind.this.requsetDeviceSitData();
                        return;
                    } else {
                        BleForSitRemind.this.closeSendData(this);
                        return;
                    }
                case 1:
                    if (BleForSitRemind.this.isSitDataResqonse) {
                        BleForSitRemind.this.closeSendData(this);
                        return;
                    } else if (BleForSitRemind.this.count < 4) {
                        BleForSitRemind.this.continueSendSettingData(this, msg);
                        sitRemindEntity entity = (sitRemindEntity) msg.getData().getSerializable("sit entity");
                        BleForSitRemind.this.setAndOpenSitData(entity.getBeginTime(), entity.getEndTime(), entity.getDuration(), entity.getOpenOrno(), entity.getNumber());
                        return;
                    } else {
                        BleForSitRemind.this.closeSendData(this);
                        return;
                    }
                case 2:
                    if (BleForSitRemind.this.isSitDataResqonse) {
                        BleForSitRemind.this.closeSendData(this);
                        return;
                    } else if (BleForSitRemind.this.count < 4) {
                        BleForSitRemind.this.continueSendDeleteData(this, msg);
                        BleForSitRemind.this.deleteItem(msg.getData().getInt("delete_number"));
                        return;
                    } else {
                        BleForSitRemind.this.closeSendData(this);
                        return;
                    }
                default:
                    return;
            }
        }
    }

    private BleForSitRemind() {
    }

    public static BleForSitRemind getInstance() {
        if (bleForSitRemind == null) {
            synchronized (BleForSitRemind.class) {
                if (bleForSitRemind == null) {
                    bleForSitRemind = new BleForSitRemind();
                }
            }
        }
        return bleForSitRemind;
    }

    private void continueSendDeleteData(Handler handler, Message msg) {
        Message mzg = handler.obtainMessage();
        mzg.what = 2;
        mzg.arg1 = msg.arg1;
        mzg.arg2 = msg.arg2;
        mzg.setData(msg.getData());
        handler.sendMessageDelayed(mzg, (long) SendLengthHelper.getSendLengthDelay(msg.arg1, msg.arg2));
    }

    private void continueSendSettingData(Handler handler, Message msg) {
        Message mzg = handler.obtainMessage();
        mzg.what = 1;
        mzg.arg1 = msg.arg1;
        mzg.arg2 = msg.arg2;
        mzg.setData(msg.getData());
        handler.sendMessageDelayed(mzg, 800);
        this.count++;
    }

    private void continueSendData(Handler handler, Message mzg) {
        Message msg = handler.obtainMessage();
        msg.what = 0;
        msg.arg1 = mzg.arg1;
        msg.arg2 = mzg.arg2;
        handler.sendEmptyMessageDelayed(0, 800);
        this.count++;
    }

    private void closeSendData(Handler handler) {
        handler.removeMessages(0);
        this.isSitDataResqonse = false;
        this.count = 0;
    }

    public void sendToGetSitData() {
        int sendLg = requsetDeviceSitData();
        Message msg = this.sitHandler.obtainMessage();
        msg.what = 0;
        msg.arg1 = sendLg;
        msg.arg2 = 14;
        this.sitHandler.sendMessageDelayed(msg, 800);
    }

    private int requsetDeviceSitData() {
        byte[] bytes = new byte[]{(byte) 0, (byte) -1};
        return setMsgToByteDataAndSendToDevice(toDevice, bytes, bytes.length);
    }

    public void dealTheResponseData(byte[] bufferTmp) {
        this.isSitDataResqonse = true;
        this.dataRecever.sendSuccess(bufferTmp);
    }

    public void setOnSitDataRecever(DataSendCallback recever) {
        this.dataRecever = recever;
    }

    public void setSitData(sitRemindEntity en) {
        int sendLg = setAndOpenSitData(en.getBeginTime(), en.getEndTime(), en.getDuration(), en.getOpenOrno(), en.getNumber());
        Message msg = new Message();
        msg.what = 1;
        msg.arg1 = sendLg;
        msg.arg2 = 8;
        Bundle bundle = new Bundle();
        bundle.putSerializable("sit entity", en);
        msg.setData(bundle);
        this.sitHandler.sendMessageDelayed(msg, 800);
    }

    private int setAndOpenSitData(String begin, String end, int duration, int check, int num) {
        String[] beg = begin.split(":");
        String[] ends = end.split(":");
        int benginH = Integer.parseInt(beg[0]);
        int beginM = Integer.parseInt(beg[1]);
        int endH = Integer.parseInt(ends[0]);
        int endM = Integer.parseInt(ends[1]);
        byte[] bytes = new byte[]{(byte) 1, (byte) num, (byte) check, (byte) beginM, (byte) benginH, (byte) endM, (byte) endH, (byte) duration};
        return setMsgToByteDataAndSendToDevice(toDevice, bytes, bytes.length);
    }

    public void deleteThisItem(int number) {
        int sendLg = deleteItem(number);
        Message msg = Message.obtain();
        msg.what = 2;
        msg.arg1 = sendLg;
        msg.arg2 = 8;
        Bundle bundle = new Bundle();
        bundle.putInt("delete_number", number);
        msg.setData(bundle);
        this.sitHandler.sendMessageDelayed(msg, (long) SendLengthHelper.getSendLengthDelay(sendLg, 8));
    }

    private int deleteItem(int number) {
        byte[] deleteData = new byte[]{(byte) 2, (byte) number};
        return setMsgToByteDataAndSendToDevice(toDevice, deleteData, deleteData.length);
    }

    public void dealSitException(byte[] bufferTmp) {
        this.isSitDataResqonse = true;
        if (this.dataRecever != null) {
            this.dataRecever.sendSuccess(bufferTmp);
        }
    }
}
