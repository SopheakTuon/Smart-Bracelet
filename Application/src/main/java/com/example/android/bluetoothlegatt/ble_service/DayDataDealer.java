package com.example.android.bluetoothlegatt.ble_service;

/**
 * @author Sopheak Tuon
 * @created on 05-Oct-17
 */

import android.content.Context;
import android.database.Cursor;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class DayDataDealer {
    public final String TAG = "DayDataDealer";
    private Context mContext;

    public DayDataDealer(Context context, String data) {
        this.mContext = context;
        try {
            JSONObject json = new JSONObject(data);
            String code = json.getString("code");
            if (code != null && code.equals("9003")) {
                JSONObject jsonObj = json.getJSONObject("data");
                saveIntoDatabase(1, jsonObj.getString("time"), jsonObj.getInt("step"), jsonObj.getInt("calorie"), jsonObj.getInt("mileage"), jsonObj.getInt("activityTime"), jsonObj.getInt("activityCalor"), jsonObj.getInt("sitTime"), jsonObj.getInt("sitCalor"), "1");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public DayDataDealer(Context mContext, byte[] bufferTmp) {
        this.mContext = mContext;
        int day = bufferTmp[0];
        int i = bufferTmp[2] + 2000;
        String dataDate = formatTheDate(i, bufferTmp[1], day);
        int stepAll = FormatUtils.byte2Int(bufferTmp, 4);
        int calorie = FormatUtils.byte2Int(bufferTmp, 8);
        int mileage = FormatUtils.byte2Int(bufferTmp, 12);
        int movementTime = FormatUtils.byte2Int(bufferTmp, 16);
        int moveCalorie = FormatUtils.byte2Int(bufferTmp, 20);
        int sitTime = FormatUtils.byte2Int(bufferTmp, 24);
        int sitCalorie = FormatUtils.byte2Int(bufferTmp, 28);
//        String dateC = getCurrentDate();
        saveIntoDatabase(0, dataDate, stepAll, calorie, mileage, movementTime, moveCalorie, sitTime, sitCalorie, "0");
    }

    public DayDataDealer(Context context, String key, String value) {
        saveIntoDatabase(key, value);
    }

    private void saveIntoDatabase(String key, String value) {
//        String userAccount = UserAccountUtil.getAccount(this.mContext);
//        MyDBHelperForDayData helper = MyDBHelperForDayData.getInstance(this.mContext);
//        if (helper.selecteDayData(this.mContext, userAccount, key, DeviceTypeUtils.getDeviceType(this.mContext)).getCount() != 0) {
//            helper.updateDayDataOnStep(this.mContext, DeviceTypeUtils.getDeviceType(this.mContext), userAccount, key, value, "1");
//            return;
//        }
//        helper.insertDayDataOnlySteps(this.mContext, userAccount, key, DeviceTypeUtils.getDeviceType(this.mContext), value, "1");
    }

    private void saveIntoDatabase(int where, String dataDate, int stepAll, int calorie, int mileage, int movementTime, int moveCalorie, int sitTime, int sitCalorie, String flag) {
//        String userAccount = UserAccountUtil.getAccount(this.mContext);
//        MyDBHelperForDayData helper = MyDBHelperForDayData.getInstance(this.mContext);
//        Cursor mCursor = helper.selecteDayData(this.mContext, userAccount, dataDate, DeviceTypeUtils.getDeviceType(this.mContext));
//        if (mCursor.getCount() == 0) {
//            helper.insert(this.mContext, DeviceTypeUtils.getDeviceType(this.mContext), userAccount, dataDate, stepAll, calorie, mileage, String.valueOf(movementTime), moveCalorie, String.valueOf(sitTime), sitCalorie, flag);
//        } else if (where == 0) {
//            helper.updateDayDataToday(this.mContext, DeviceTypeUtils.getDeviceType(this.mContext), userAccount, dataDate, stepAll, calorie, mileage, String.valueOf(movementTime), moveCalorie, String.valueOf(sitTime), sitCalorie, flag);
//        } else if (checkCanUpdate(mCursor)) {
//            helper.updateDayDataToday(this.mContext, DeviceTypeUtils.getDeviceType(this.mContext), userAccount, dataDate, stepAll, calorie, mileage, String.valueOf(movementTime), moveCalorie, String.valueOf(sitTime), sitCalorie, flag);
//        }
//        this.mContext.sendBroadcast(new Intent(MyConfingInfo.NOTIFY_MAINACTIVITY_TO_UPDATE_DAY_DATA));
    }

    private boolean checkCanUpdate(Cursor mCursor) {
        if (mCursor.moveToFirst()) {
            do {
                String dataSend = mCursor.getString(mCursor.getColumnIndex("dataSendOK"));
                if (dataSend != null && dataSend.equals("0")) {
                    return false;
                }
            } while (mCursor.moveToNext());
        }
        return true;
    }

    private String formatTheDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.set(year, month - 1, day);
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
    }

    private void uploadingTheStepAndSleepData(final String day) {
//        String loginType = UserAccountUtil.getType(this.mContext);
//        int i = -1;
//        switch (loginType.hashCode()) {
//            case 49:
//                if (loginType.equals("1")) {
//                    i = 1;
//                    break;
//                }
//                break;
//            case 50:
//                if (loginType.equals("2")) {
//                    i = 0;
//                    break;
//                }
//                break;
//            case 51:
//                if (loginType.equals("3")) {
//                    i = 2;
//                    break;
//                }
//                break;
//            case 52:
//                if (loginType.equals("4")) {
//                    i = 3;
//                    break;
//                }
//                break;
//        }
//        Executor executor;
//        switch (i) {
//            case 0:
//            case 1:
//            case 2:
//                if (NetStatus.isNetWorkConnected(this.mContext)) {
//                    checkThirdPartyTask tasks = new checkThirdPartyTask();
//                    tasks.setOnLoginBackListener(new OnAllLoginBack() {
//                        public void onLoginBack(String re) {
//                            UpdateAttionMovementAndSleepData updateAttionMovementAndSleepData = new UpdateAttionMovementAndSleepData(DayDataDealer.this.mContext);
//                            Executor executor = MyApplication.threadService;
//                            String[] strArr = new String[]{day};
//                            if (updateAttionMovementAndSleepData instanceof AsyncTask) {
//                                AsyncTaskInstrumentation.executeOnExecutor(updateAttionMovementAndSleepData, executor, strArr);
//                            } else {
//                                updateAttionMovementAndSleepData.executeOnExecutor(executor, strArr);
//                            }
//                        }
//                    });
//                    executor = MyApplication.threadService;
//                    String[] strArr = new String[]{UserAccountUtil.getAccount(this.mContext), loginType, null, null, null};
//                    if (tasks instanceof AsyncTask) {
//                        AsyncTaskInstrumentation.executeOnExecutor(tasks, executor, strArr);
//                        return;
//                    } else {
//                        tasks.executeOnExecutor(executor, strArr);
//                        return;
//                    }
//                }
//                return;
//            case 3:
//                if (NetStatus.isNetWorkConnected(this.mContext)) {
//                    LoginOnBackground backLogin = new LoginOnBackground(this.mContext);
//                    backLogin.setOnLoginBackListener(new OnAllLoginBack() {
//                        public void onLoginBack(String re) {
//                            UpdateAttionMovementAndSleepData updateAttionMovementAndSleepData = new UpdateAttionMovementAndSleepData(DayDataDealer.this.mContext);
//                            Executor executor = MyApplication.threadService;
//                            String[] strArr = new String[]{day};
//                            if (updateAttionMovementAndSleepData instanceof AsyncTask) {
//                                AsyncTaskInstrumentation.executeOnExecutor(updateAttionMovementAndSleepData, executor, strArr);
//                            } else {
//                                updateAttionMovementAndSleepData.executeOnExecutor(executor, strArr);
//                            }
//                        }
//                    });
//                    executor = MyApplication.threadService;
//                    Void[] voidArr = new Void[0];
//                    if (backLogin instanceof AsyncTask) {
//                        AsyncTaskInstrumentation.executeOnExecutor(backLogin, executor, voidArr);
//                        return;
//                    } else {
//                        backLogin.executeOnExecutor(executor, voidArr);
//                        return;
//                    }
//                }
//                return;
//            default:
//                return;
//        }
    }

    private String getFormetDay(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.set(year, month - 1, day);
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
    }

    public String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance(TimeZone.getDefault()).getTime());
    }
}
