package com.example.android.bluetoothlegatt.ble_service;

import android.content.Context;
import android.util.Log;

import com.example.android.bluetoothlegatt.util.FormatUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author Sopheak Tuon
 * @created on 03-Oct-17
 */

public class BloodDataDealer {
    public final String TAG = "BloodDataDealer";

    public BloodDataDealer(Context contexts, byte[] bloodData) {
        int va = (bloodData.length - 3) / 9;
        Log.i("BloodDataDealer", "血压数据长度：" + va);
        for (int i = 0; i < va; i++) {
            parseBloodData((i * 9) + 1, bloodData, contexts);
        }
    }

//    public BloodDataDealer(Context context, String jsonObj) throws JSONException {
//        JSONObject jsonObject = JSONObjectInstrumentation.init(jsonObj);
//        Iterator<String> it = jsonObject.keys();
//        while (it.hasNext()) {
//            String bloodKey = (String) it.next();
//            JSONObject value = JSONObjectInstrumentation.init(jsonObject.getString(bloodKey));
//            Context context2 = context;
//            SaveData(context2, UserAccountUtil.getAccount(context), bloodKey, value.getInt("Systolic"), value.getInt("Diastolic"), value.getInt("HeartRate"), value.getInt("SPO2"), value.getInt("HRV"), "1");
//        }
//    }

    private void parseBloodData(int start, byte[] bloodData, Context context) {
        int highPre = bloodData[start + 4] & 255;
        int lowPre = bloodData[start + 5] & 255;
        int hr = bloodData[start + 6] & 255;
        int sp02 = bloodData[start + 7] & 255;
        int hrv = bloodData[start + 8] & 255;
        String dateS = FormatDate(1000 * ((long) (FormatUtils.byte2Int(bloodData, start) - (TimeZone.getDefault().getRawOffset() / 1000))));
        Log.i("BloodDataDealer", "血压数据血压日期：" + dateS + "高压：" + highPre + "低压：" + lowPre);
//        SaveData(context, UserAccountUtil.getAccount(context), dateS, highPre, lowPre, hr, sp02, hrv, "0");
    }

//    private void SaveData(Context context, String account, String dateS, int highPre, int lowPre, int hr, int sp02, int hrv, String flag) {
//        if (MyDBHelperForDayData.getInstance(context).selectBloodData(context, account, dateS.substring(0, 10), dateS.substring(11), DeviceTypeUtils.getDeviceType(context)).getCount() > 0) {
//            Log.i("BloodDataDealer", "重复血压数据到数据库");
//            return;
//        }
//        MyDBHelperForDayData.getInstance(context).insertBloodData(account, dateS.substring(0, 10), dateS.substring(11), highPre, lowPre, hr, sp02, hrv, DeviceTypeUtils.getDeviceType(context), flag);
//        Log.i("BloodDataDealer", "插入血压数据到数据库");
//    }

    private String FormatDate(long date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date(date));
    }
}