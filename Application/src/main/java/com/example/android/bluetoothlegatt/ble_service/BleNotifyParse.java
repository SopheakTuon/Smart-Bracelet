package com.example.android.bluetoothlegatt.ble_service;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Trace;
import android.util.Log;

import com.example.android.bluetoothlegatt.util.FormatUtils;

public class BleNotifyParse {
    private static final int BUFFER_MAX_LEN = 8192;
    private static String MESSAGE_FOR_HANDLER = "byte array for handler";
    private static final String TAG = "BleNotifyParse";
    public static BleNotifyParse mBleNotifyParse;
    private static Context mContext;
    private static Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            byte[] data = msg.getData().getByteArray(BleNotifyParse.MESSAGE_FOR_HANDLER);
            switch (msg.what) {
                case 0:
//                    BleBaseDataForOutlineMovement.getOutlineInstance().dealTheData(BleNotifyParse.mContext, data, data.length);
                    return;
                default:
                    return;
            }
        }
    };
    private byte[] buffer = new byte[8192];
    private int bufferLength;
    private int bufferReadB;
    private byte[] bufferTmp = new byte[1024];
    private final String mNotifyLock = "mNotifyLock";

    private class DealTheOutLineData extends AsyncTask<byte[], Void, Boolean> {
        public Trace _nr_trace;
        private byte[] dataByte;

        public void _nr_setTrace(Trace trace) {
            try {
                this._nr_trace = trace;
            } catch (Exception e) {
            }
        }

        private DealTheOutLineData() {
            this.dataByte = null;
        }

        protected Boolean doInBackground(byte[]... params) {
            this.dataByte = params[0];
//            BleDataForDayHeartReatData.getHRDataInstance(BleNotifyParse.mContext).requestHeartReatDataAll();
            for (int ta = 0; ta < 30; ta++) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return Boolean.valueOf(true);
        }

        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean.booleanValue()) {
                Bundle bundle = new Bundle();
                bundle.putByteArray(BleNotifyParse.MESSAGE_FOR_HANDLER, this.dataByte);
                Message message = new Message();
                message.what = 0;
                message.setData(bundle);
                BleNotifyParse.mHandler.sendMessageDelayed(message, 1500);
            }
        }
    }

    private BleNotifyParse() {
    }

    public static BleNotifyParse getInstance() {
        if (mBleNotifyParse == null) {
            mBleNotifyParse = new BleNotifyParse();
        }
        return mBleNotifyParse;
    }

    public void doParse(Context mContext, byte[] notifyData) {
        Log.i(TAG, "writeDelay receveData: " + FormatUtils.bytesToHexString(notifyData));
        mContext = mContext;
        synchronized ("mNotifyLock") {
            executeParse(mContext, notifyData);
        }
    }

    private void executeParse(Context mContext, byte[] notifyData) {
        for (int i = 0; i < notifyData.length; i++) {
            this.buffer[((this.bufferReadB + this.bufferLength) + i) % 8192] = notifyData[i];
        }
        this.bufferLength += notifyData.length;
        boolean isFindNotifyBegin = false;
        int notifyIndex = 0;
        int msgLen = 0;
        int read = this.bufferReadB;
        while (read < this.bufferReadB + this.bufferLength) {
            int pos = read % 8192;
            if (this.buffer[pos] == (byte) 104 && !isFindNotifyBegin) {
                isFindNotifyBegin = true;
                notifyIndex = 0;
                msgLen = 6;
            }
            if (isFindNotifyBegin) {
                this.bufferTmp[notifyIndex] = this.buffer[pos];
                notifyIndex++;
            }
            if (notifyIndex == 4) {
                msgLen = (bytes2Char(this.bufferTmp, 2) + 4) + 2;
                if (msgLen > 1000) {
                    isFindNotifyBegin = false;
                    read = (read - notifyIndex) + 1;
                    notifyIndex = 0;
                }
            }
            if (isFindNotifyBegin && notifyIndex == msgLen) {
                if (this.buffer[pos] == (byte) 22) {
                    executeParse(mContext, this.bufferTmp, notifyIndex);
                    this.bufferLength -= read - this.bufferReadB;
                    this.bufferReadB = pos;
                    isFindNotifyBegin = false;
                    notifyIndex = 0;
                } else {
                    isFindNotifyBegin = false;
                    read = (read - notifyIndex) + 1;
                }
            }
            read++;
        }
    }

    private boolean checkData(byte[] bufferTmp, byte[] totalData) {
        byte addd = (byte) 0;
        int a = totalData.length - 1;
        int b = totalData.length - 2;
        int i = 0;
        while (i < totalData.length) {
            totalData[i] = bufferTmp[i];
            if (!(i == b || i == a)) {
                addd = (byte) (bufferTmp[i] + addd);
            }
            i++;
        }
        if (((byte) (addd % 255)) == totalData[totalData.length - 2]) {
            return true;
        }
        return false;
    }

    private void executeParse(Context mContext, byte[] bufferTmp, int notifyIndex) {
        byte head = bufferTmp[0];
        byte cmd = (byte) (bufferTmp[1] & 255);
        int dataLen = bytes2Char(bufferTmp, 2);
        if (dataLen <= 1000 && bufferTmp.length >= dataLen) {
            byte[] theData = new byte[(dataLen + 2)];
            if (checkData(bufferTmp, new byte[(dataLen + 6)])) {
                for (int i = 0; i < theData.length; i++) {
                    theData[i] = bufferTmp[i + 4];
                }
                bufferTmp = theData;
                if (cmd == BleDataForBattery.fromCmd) {
                    BleDataForBattery.getInstance().dealReceData(mContext, bufferTmp, dataLen);
                } else if (cmd == BleDataForHardVersion.fromDevice) {
                    BleDataForHardVersion.getInstance().dealReceData(mContext, bufferTmp, dataLen);
                } else if (cmd == BleDataForSettingParams.fromDevice) {
                } else {
                    if (cmd == BleDataForCustomRemind.fromDevice) {
                        BleDataForCustomRemind.getCustomRemindDataInstance().dealTheValidData(cmd, mContext, bufferTmp);
                    } else if (cmd == BleDataForHRWarning.fromDevice) {
                        BleDataForHRWarning.getInstance().dealTheHRData(bufferTmp, mContext);
                    } else if (cmd == BleDataForRingDelay.fromDevice) {
                        BleDataForRingDelay.getDelayInstance().dealTheDelayData(mContext, bufferTmp);
                    } else if (cmd == BleDataForFactoryReset.fromDevice) {
                        BleDataForFactoryReset.getBleDataInstance().dealTheResult(mContext, bufferTmp);
                    } else if (cmd == BleDataForUpLoad.fromDevice) {
                        BleDataForUpLoad.getUpLoadInstance().dealUpLoadBackData(cmd, bufferTmp);
                    } else if (cmd == BleForPhoneAndSmsRemind.phoneRemindFromDevice) {
                        BleForPhoneAndSmsRemind.getInstance().dealTheResponse(true);
                    } else if (cmd == (byte) -123) {
                        if ((bufferTmp[0] == (byte) 0 && bufferTmp[1] == (byte) 3) || ((bufferTmp[0] == (byte) 0 && bufferTmp[1] == (byte) 2) || (bufferTmp[0] == (byte) 1 && bufferTmp[1] == (byte) 3))) {
                            BleForPhoneAndSmsRemind.getInstance().dealOpenResponse(bufferTmp);
                        } else if (bufferTmp[1] == (byte) 2 && bufferTmp[0] == (byte) 1) {
                            BleForQQWeiChartFacebook.getInstance().dealTheResuponse(bufferTmp);
                        } else if (bufferTmp[1] == (byte) 10 || bufferTmp[1] == (byte) 11 || bufferTmp[1] == (byte) 12 || bufferTmp[1] == BleDataForTakePhoto.startToDevice || bufferTmp[1] == (byte) 9 || bufferTmp[1] == BleDataForTakePhoto.takeToDevice) {
                            BleForQQWeiChartFacebook.getInstance().dealOpenOrCloseRequese(bufferTmp);
                        } else if (bufferTmp[1] == (byte) 1) {
                            BleForLostRemind.getInstance().dealTheLostResqonse(bufferTmp);
                        } else if (bufferTmp[1] == (byte) 6) {
                            BleForLIftUpRemind.getInstance().dealLiftUpResqonse(bufferTmp);
                        } else if (bufferTmp[0] == (byte) 2) {
                            BleForQQWeiChartFacebook.getInstance().dealOpenOrCloseRequese(bufferTmp);
                        }
                    } else if (cmd == (byte) -117) {
                        if (bufferTmp[0] == (byte) 0) {
                            BleForPhoneAndSmsRemind.getInstance().dealSmsDataBack(bufferTmp);
                        } else {
                            BleDataForQQAndOtherRemine.getIntance().dealRemindResponse(bufferTmp);
                        }
                    } else if (cmd == BleForFindDevice.fromDevice) {
                        BleForFindDevice.getBleForFindDeviceInstance().dealTheResponseData(bufferTmp);
                    } else if (cmd == BleDataForDayData.fromDevice) {
                        if (bufferTmp.length >= 34) {
                            byte pagType = bufferTmp[3];
                            if (pagType == (byte) 0 && bufferTmp.length == 34) {
                                BleDataForDayData.getDayDataInstance(mContext).dealDayData(mContext, bufferTmp);
                            } else if (pagType == (byte) 0) {
                                BleDataForDayData.getDayDataInstance(mContext).juestResponse(bufferTmp);
                            } else if (pagType == (byte) 1 && bufferTmp.length >= 198) {
                                BleDataForEachHourData.getEachHourDataInstance().dealTheEachData(bufferTmp);
                            } else if (pagType == (byte) 2 && bufferTmp.length > 40) {
                                BleDataForSleepData.getInstance(mContext).dealTheSleepData(bufferTmp);
                            } else if (pagType == (byte) 3 && bufferTmp.length > 180) {
                                BleDataForDayHeartReatData.getHRDataInstance(mContext).dealTheHeartRateData(bufferTmp);
                            }
                        }
                    } else if (cmd == BleBaseDataForOutlineMovement.mNotify_cmd) {
                        BleBaseDataForOutlineMovement.getOutlineInstance().requstOutlineData(bufferTmp);
                        BleNotifyParse bleNotifyParse = this;
                        DealTheOutLineData dealTheOutLineData = new DealTheOutLineData();
                        Object[] objArr = new byte[][]{bufferTmp};
                        if (dealTheOutLineData instanceof AsyncTask) {
//                            AsyncTaskInstrumentation.execute(dealTheOutLineData, objArr);
                        } else {
//                            dealTheOutLineData.execute(objArr);
                        }
                    } else if (cmd == BleDataforSyn.back_cmd) {
                        BleDataforSyn.getSynInstance().dealTheResult();
                    } else if (cmd == BleDataForCustomRemind.fromDeviceNull) {
                        BleDataForCustomRemind.getCustomRemindDataInstance().dealTheValidData(cmd, mContext, bufferTmp);
                    } else if (cmd == BleForGetFatigueData.fromDevice) {
                        BleForGetFatigueData.getInstance(mContext).dealTheResbonseData(bufferTmp);
                    } else if (cmd == BleForGetFatigueData.exceptionDevice) {
                        BleForGetFatigueData.getInstance(mContext).dealException();
                    } else if (cmd == BleForSitRemind.fromDevice) {
                        BleForSitRemind.getInstance().dealTheResponseData(bufferTmp);
                    } else if (cmd == BleForSitRemind.excepteionDevice) {
                        BleForSitRemind.getInstance().dealSitException(bufferTmp);
                    } else if (cmd == BleDataForSettingArgs.fromDevice) {
                        BleDataForSettingArgs.getInstance(mContext).dealTheBack(bufferTmp);
                    } else if (cmd == BleDataForTakePhoto.startFromDevice) {
                        BleDataForTakePhoto.getInstance().dealOpenResponse(bufferTmp);
                    } else if (cmd == BleDataForTakePhoto.takeFromDevice) {
                        BleDataForTakePhoto.getInstance().backMessageToDevice();
                    } else if (cmd == DeviceExceptionDeal.fromDevice) {
                        DeviceExceptionDeal.getExceptionInstance(mContext).dealExceptionInfo(bufferTmp);
                    } else if (cmd == DeviceExceptionDeal.testFromDevice) {
                        DeviceExceptionDeal.getExceptionInstance(mContext).dealTextData(bufferTmp);
                    } else if (cmd == BleDataForOnLineMovement.fromDevice) {
                        BleDataForOnLineMovement.getBleDataForOutlineInstance().dealOnlineHRMonitor(bufferTmp);
                    } else if (cmd == BleBaseDataForBlood.mNotify_cmd) {
                        Log.i(TAG, "writeDelay receveData: " + bufferTmp[0]);
                        if (bufferTmp[0] == (byte) 4) {
                            SharedPreferences preferences = mContext.getSharedPreferences("user", 0);
                            String strlow = preferences.getString("short_ed", "");
                            String strhig = preferences.getString("height_ed", "");
                            if (strlow.length() > 1 && strhig.length() > 1) {
                                int int_s = Integer.parseInt(strlow);
                                BleBaseDataForBlood.getBloodInstance().requstOutlineData(Integer.parseInt(strhig), int_s);
                            }
                            BleBaseDataForBlood.getBloodInstance().dealTheData(bufferTmp);
                        } else if (bufferTmp[0] == (byte) 2) {
                            BleBaseDataForBlood.getBloodInstance().requestdevice();
                        }
                        BleBaseDataForBlood.getBloodInstance().requstOutlineData();
                        BleBaseDataForBlood.getBloodInstance().dealTheData(bufferTmp);
                    } else if (cmd == BleDataForPhoneComm.fromDevice) {
                        BleDataForPhoneComm.getInstance().dealDeviceComm(bufferTmp);
                    } else if (cmd == BleReadDeviceMenuState.fromDevice) {
                        BleReadDeviceMenuState.getInstance().sendSuccess(bufferTmp);
                    } else if (cmd == BleDataForWeather.fromDevice || cmd == BleDataForWeather.fromDeviceNew) {
                        BleDataForWeather.getIntance().dealWeatherBack(bufferTmp);
                    } else if (cmd == BleDataForFrame.fromDevice) {
                        Log.i(TAG, "writeDelay receveData222: " + FormatUtils.bytesToHexString(bufferTmp));
                        BleDataForFrame.getInstance().dealReceData(bufferTmp);
                    } else if (cmd == BleDataForTarget.fromDevice) {
                        BleDataForTarget.getInstance().dealComm(bufferTmp);
                    } else if (cmd == BleDataForFrame.fromDevice3) {
                        BleDataForFrame.getInstance().dealReceB3(bufferTmp);
                    }
                }
            }
        }
    }

    int bytes2Char(byte[] data, int offset) {
        return (data[offset] & 255) + ((data[offset + 1] << 8) & 65535);
    }
}
