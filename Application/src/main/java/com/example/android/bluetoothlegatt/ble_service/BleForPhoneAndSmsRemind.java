package com.example.android.bluetoothlegatt.ble_service;

/**
 * @author Sopheak Tuon
 * @created on 04-Oct-17
 */

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.io.UnsupportedEncodingException;

public class BleForPhoneAndSmsRemind extends BleBaseDataManage {
    private static String TAG = BleForPhoneAndSmsRemind.class.getSimpleName();
    private static BleForPhoneAndSmsRemind bleForPhoneAndSmsRemind = null;
    public static final byte formDevice = (byte) -123;
    public static final byte phoneRemindFromDevice = (byte) -127;
    public static final byte smsRemindFromDevice = (byte) -117;
    public static final byte startPhoneRemindToDevice = (byte) 1;
    public static final byte startSmsRemindToDevice = (byte) 11;
    public static final byte toDevice = (byte) 5;
    private final String CONTACTNAME = "contact name";
    private final String CONTENE = "content";
    private final int PHEONREMINDMSG = 3;
    private final String PHONEARGS = "phone args";
    private final String PHONECONTACTNAME = "phone contact name";
    private final String PHONENUMBER = "phone number";
    private final int PHONE_OPEN_MSG = 2;
    private final int PHONE_READ_STATE = 4;
    private final int SMSOPENMSG = 0;
    private final int SMSREMINDMSG = 1;
    private int count = 0;
    private byte[] datas = new byte[0];
    private boolean isAlreadyBack = false;
    private boolean isOpenOk = false;
    private boolean isReadStatus = false;
    public onPhoneHasRecever mOnPhoneHasRecever;
    private Handler phoneHandler = new C14711();
    private int readCount = 0;
    private DataSendCallback readListener;

    class C14711 extends Handler {
        C14711() {
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (BleForPhoneAndSmsRemind.this.isOpenOk) {
                        BleForPhoneAndSmsRemind.this.closeTheHandler(this);
                        return;
                    } else if (BleForPhoneAndSmsRemind.this.count < 4) {
                        BleForPhoneAndSmsRemind.this.continueSendHandler(this);
                        BleForPhoneAndSmsRemind.this.openThePhoneAndSmsRemind((byte) msg.arg1, (byte) msg.arg2);
                        return;
                    } else {
                        BleForPhoneAndSmsRemind.this.closeTheHandler(this);
                        return;
                    }
                case 1:
                    if (BleForPhoneAndSmsRemind.this.isAlreadyBack) {
                        BleForPhoneAndSmsRemind.this.closeTheSmsRemindHandler(this);
                        return;
                    } else if (BleForPhoneAndSmsRemind.this.count < 4) {
                        Bundle bun = msg.getData();
                        String contactName = bun.getString("contact name");
                        byte content = bun.getByte("content");
                        BleForPhoneAndSmsRemind.this.startSmsRemind(contactName, content);
                        BleForPhoneAndSmsRemind.this.continueSendSmsHandler(this, contactName, content);
                        return;
                    } else {
                        BleForPhoneAndSmsRemind.this.closeTheSmsRemindHandler(this);
                        return;
                    }
                case 2:
                    Log.i(BleForPhoneAndSmsRemind.TAG, "isOpen:" + BleForPhoneAndSmsRemind.this.isOpenOk);
                    if (BleForPhoneAndSmsRemind.this.isOpenOk) {
                        BleForPhoneAndSmsRemind.this.closeThePhoenHandler(this);
                        return;
                    } else if (BleForPhoneAndSmsRemind.this.count < 4) {
                        BleForPhoneAndSmsRemind.this.continueSendPhoneHandler(this, msg);
                        BleForPhoneAndSmsRemind.this.openThePhoneAndSmsRemind((byte) msg.arg1, (byte) msg.arg2);
                        return;
                    } else {
                        BleForPhoneAndSmsRemind.this.closeThePhoenHandler(this);
                        return;
                    }
                case 3:
                    if (BleForPhoneAndSmsRemind.this.isAlreadyBack) {
                        BleForPhoneAndSmsRemind.this.closeThePhoneRemindHandler(this);
                        return;
                    } else if (BleForPhoneAndSmsRemind.this.count < 4) {
                        Bundle bundle = msg.getData();
                        String num = bundle.getString("phone number");
                        String conName = bundle.getString("phone contact name");
                        byte ba = bundle.getByte("phone args");
                        BleForPhoneAndSmsRemind.this.startPhoneRemind(num, conName, ba);
                        BleForPhoneAndSmsRemind.this.continueSendPhoneRemindHandler(this, num, conName, ba);
                        return;
                    } else {
                        BleForPhoneAndSmsRemind.this.closeThePhoneRemindHandler(this);
                        return;
                    }
                case 4:
                    if (BleForPhoneAndSmsRemind.this.isReadStatus) {
                        BleForPhoneAndSmsRemind.this.closeReadPhoneStatus(this);
                        return;
                    } else if (BleForPhoneAndSmsRemind.this.readCount < 4) {
                        BleForPhoneAndSmsRemind.this.readPhoneRemind();
                        BleForPhoneAndSmsRemind.this.continueReadPhoneStatus(this, msg);
                        return;
                    } else {
                        BleForPhoneAndSmsRemind.this.closeReadPhoneStatus(this);
                        return;
                    }
                default:
                    return;
            }
        }
    }

    public interface onPhoneHasRecever {
        void onReceverLisener();
    }

    private void continueReadPhoneStatus(Handler handler, Message msg) {
        Message mzg = handler.obtainMessage();
        mzg.what = 4;
        mzg.arg1 = msg.arg1;
        mzg.arg2 = msg.arg2;
        handler.sendMessageDelayed(mzg, (long) SendLengthHelper.getSendLengthDelay(msg.arg1, msg.arg2));
        this.readCount++;
    }

    private void closeReadPhoneStatus(Handler handler) {
        handler.removeMessages(4);
        this.isReadStatus = false;
        this.readCount = 0;
    }

    private void continueSendHandler(Handler handler) {
        handler.sendEmptyMessageDelayed(0, 60);
        this.count++;
    }

    private void continueSendSmsHandler(Handler handler, String contactName, byte content) {
        Message msg = Message.obtain();
        msg.what = 1;
        Bundle bundle = new Bundle();
        bundle.putString("contact name", contactName);
        bundle.putByte("content", content);
        msg.setData(bundle);
        handler.sendMessageDelayed(msg, 60);
        this.count++;
    }

    private void continueSendPhoneHandler(Handler handler, Message msg) {
        Message mzg = handler.obtainMessage();
        mzg.what = 2;
        mzg.arg1 = msg.arg1;
        mzg.arg2 = msg.arg2;
        Bundle bundle = msg.getData();
        mzg.setData(bundle);
        handler.sendMessageDelayed(mzg, (long) SendLengthHelper.getSendLengthDelay(bundle.getInt("send_length"), bundle.getInt("rece_length")));
        this.count++;
    }

    private void continueSendPhoneRemindHandler(Handler handler, String num, String conName, byte ba) {
        Message msg = Message.obtain();
        msg.what = 3;
        Bundle bundle = new Bundle();
        bundle.putString("phone number", num);
        bundle.putString("phone contact name", conName);
        bundle.putByte("phone args", ba);
        msg.setData(bundle);
        handler.sendMessageDelayed(msg, 60);
        this.count++;
    }

    private void closeTheHandler(Handler handler) {
        handler.removeMessages(0);
        this.isOpenOk = false;
        this.count = 0;
    }

    private void closeTheSmsRemindHandler(Handler handler) {
        handler.removeMessages(1);
        this.isAlreadyBack = false;
        this.count = 0;
    }

    private void closeThePhoenHandler(Handler handler) {
        handler.removeMessages(2);
        this.isOpenOk = false;
        this.count = 0;
    }

    private void closeThePhoneRemindHandler(Handler handler) {
        handler.removeMessages(3);
        this.isAlreadyBack = false;
        this.count = 0;
    }

    public void addCallback(onPhoneHasRecever phoneHasRecever) {
        this.mOnPhoneHasRecever = phoneHasRecever;
    }

    public static BleForPhoneAndSmsRemind getInstance() {
        if (bleForPhoneAndSmsRemind == null) {
            bleForPhoneAndSmsRemind = new BleForPhoneAndSmsRemind();
        }
        return bleForPhoneAndSmsRemind;
    }

    private BleForPhoneAndSmsRemind() {
    }

    public void openSmsRemind(byte ba, byte sw) {
        openThePhoneAndSmsRemind(ba, sw);
        Message msg = Message.obtain();
        msg.what = 0;
        msg.arg1 = ba;
        this.phoneHandler.sendMessageDelayed(msg, 80);
    }

    public void openPhoneRemine(byte ba, byte sw) {
        int sendLg = openThePhoneAndSmsRemind(ba, sw);
        Message msg = this.phoneHandler.obtainMessage();
        msg.what = 2;
        msg.arg1 = ba;
        msg.arg2 = sw;
        Bundle bundle = new Bundle();
        bundle.putInt("send_length", sendLg);
        bundle.putInt("rece_length", 8);
        this.phoneHandler.sendMessageDelayed(msg, (long) SendLengthHelper.getSendLengthDelay(sendLg, 8));
    }

    private int openThePhoneAndSmsRemind(byte ba, byte sw) {
        byte[] data = new byte[]{(byte) 0, ba, sw};
        return setMsgToByteDataAndSendToDevice((byte) 5, data, data.length);
    }

    public void startSMSRemine(String contactName, byte content) {
        startSmsRemind(contactName, content);
        Message msg = Message.obtain();
        msg.what = 1;
        Bundle bun = new Bundle();
        bun.putString("contact name", contactName);
        bun.putByte("content", content);
        msg.setData(bun);
        this.phoneHandler.sendMessageDelayed(msg, 80);
    }

    private void startSmsRemind(String contactName, byte content) {
        if (contactName == null || !contactName.equals("")) {
            compareTheLength(contactName);
            if (this.datas != null) {
                byte[] data;
                if (this.datas.length == 36) {
                    data = new byte[(this.datas.length + 1)];
                } else {
                    data = new byte[(this.datas.length + 2)];
                }
                System.arraycopy(this.datas, 0, data, 1, this.datas.length);
                setMsgToByteDataAndSendToDevice((byte) 11, data, data.length);
            }
        }
    }

    private void compareTheLength(String contactName) {
        try {
            if (contactName.getBytes("utf-8").length > 36) {
                compareTheLength(contactName.substring(0, contactName.length() - 1));
            } else {
                this.datas = contactName.getBytes("utf-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void closeTheRemind() {
        setMsgToByteDataAndSendToDevice((byte) 1, new byte[]{(byte) 1}, 1);
    }

    public void readPhoneRemindStatus() {
        int sendLg = readPhoneRemind();
        Message msg = this.phoneHandler.obtainMessage();
        msg.what = 4;
        msg.arg1 = sendLg;
        msg.arg2 = 9;
        this.phoneHandler.sendMessageDelayed(msg, (long) SendLengthHelper.getSendLengthDelay(sendLg, 9));
    }

    private int readPhoneRemind() {
        byte[] readData = new byte[]{(byte) 1, (byte) 3};
        return setMsgToByteDataAndSendToDevice((byte) 5, readData, readData.length);
    }

    public void startPhoneAndSmsRemind(String smsBody, String contactName, byte ba) {
        if (contactName != null && !contactName.equals("") && contactName.getBytes().length < 16) {
            byte[] bytesName = null;
            byte[] body = null;
            try {
                bytesName = contactName.getBytes("UTF-8");
                if (smsBody.length() > 48) {
                    smsBody.substring(0, 47);
                }
                body = smsBody.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            byte[] allData = new byte[(body.length + 16)];
            int i = 0;
            while (i < allData.length) {
                if (i >= 0 && i < bytesName.length) {
                    allData[i + 1] = bytesName[i];
                }
                if (i > 15) {
                    allData[i] = body[i - 16];
                }
                i++;
            }
            setMsgToByteDataAndSendToDevice((byte) 11, allData, allData.length);
        }
    }

    public void beginPhoneRemind(String phoneNumber, String contactName, byte ba) {
        startPhoneRemind(phoneNumber, contactName, ba);
        Message msg = Message.obtain();
        msg.what = 3;
        Bundle bundle = new Bundle();
        bundle.putString("phone number", phoneNumber);
        bundle.putString("phone contact name", contactName);
        bundle.putByte("phone args", ba);
        msg.setData(bundle);
        this.phoneHandler.sendMessageDelayed(msg, 80);
    }

    private void startPhoneRemind(String phoneNumber, String contactName, byte ba) {
        int i;
        if (phoneNumber.startsWith("+86")) {
            phoneNumber = phoneNumber.substring(3);
        }
        char[] chars = phoneNumber.toCharArray();
        byte[] pnum = new byte[chars.length];
        for (i = 0; i < chars.length; i++) {
            pnum[i] = (byte) (chars[i] & 255);
        }
        if (contactName == null || contactName.equals("")) {
            byte[] phoneData = new byte[16];
            i = 0;
            while (i < phoneData.length && i < pnum.length) {
                phoneData[i + 1] = pnum[i];
                i++;
            }
            pnum = phoneData;
            setMsgToByteDataAndSendToDevice((byte) 1, pnum, pnum.length);
        } else if (contactName.getBytes().length < 32) {
            byte[] bytesName = null;
            try {
                bytesName = contactName.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            byte[] allData = new byte[(bytesName.length + 16)];
            i = 0;
            while (i < allData.length) {
                if (i >= 0 && i < pnum.length) {
                    allData[i + 1] = pnum[i];
                }
                if (i > 15) {
                    allData[i] = bytesName[i - 16];
                }
                i++;
            }
            setMsgToByteDataAndSendToDevice((byte) 1, allData, allData.length);
        }
    }

    public void dealTheResponse(boolean b) {
        this.isAlreadyBack = true;
        if (this.mOnPhoneHasRecever != null) {
            this.mOnPhoneHasRecever.onReceverLisener();
        }
    }

    public void dealOpenResponse(byte[] bufferTmp) {
        if (bufferTmp[0] == (byte) 0) {
            Log.i(TAG, "isOpen:" + bufferTmp);
            this.isOpenOk = true;
        } else if (bufferTmp[0] == (byte) 1) {
            this.isReadStatus = true;
            this.readListener.sendSuccess(bufferTmp);
        }
    }

    public void dealSmsDataBack(byte[] bufferTmp) {
        if (bufferTmp[0] == (byte) 0) {
            this.isAlreadyBack = true;
        }
    }

    public void setOnDataListener(DataSendCallback onReadDataListener) {
        this.readListener = onReadDataListener;
    }
}
