package com.example.android.bluetoothlegatt.ble_service;

/**
 * @author Sopheak Tuon
 * @created on 02-Oct-17
 */

public class HRDataDealer {
    public final String TAG = HRDataDealer.class.getSimpleName();

//    public HRDataDealer(byte[] hr, Context mContext) {
//        Log.i(this.TAG, "分时心率数据：" + FormatUtils.bytesToHexString(hr));
//        int day = hr[0] & 255;
//        int month = hr[1] & 255;
//        int year = (hr[2] & 255) + 2000;
//        Calendar calendarData = Calendar.getInstance();
//        calendarData.set(year, month - 1, day);
//        String dataDay = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendarData.getTime());
//        byte[] dataThree = new byte[180];
//        System.arraycopy(hr, 6, dataThree, 0, dataThree.length);
//        String userAccount = UserAccountUtil.getAccount(mContext);
//        createOneItemData(mContext, userAccount, dataDay);
//        switch (hr[5]) {
//            case (byte) 1:
//                insertEachPackage(mContext, "one", "two", "three", userAccount, dataDay, dataThree, "0");
//                return;
//            case (byte) 2:
//                insertEachPackage(mContext, "four", "five", "six", userAccount, dataDay, dataThree, "0");
//                return;
//            case (byte) 3:
//                insertEachPackage(mContext, "seven", "eight", "nine", userAccount, dataDay, dataThree, "0");
//                return;
//            case (byte) 4:
//                insertEachPackage(mContext, "ten", "one1", "two1", userAccount, dataDay, dataThree, "0");
//                return;
//            case (byte) 5:
//                insertEachPackage(mContext, "three1", "four1", "five1", userAccount, dataDay, dataThree, "0");
//                return;
//            case (byte) 6:
//                insertEachPackage(mContext, "six1", "seven1", "eight1", userAccount, dataDay, dataThree, "0");
//                return;
//            case (byte) 7:
//                insertEachPackage(mContext, "nine1", "ten1", "one2", userAccount, dataDay, dataThree, "0");
//                return;
//            case (byte) 8:
//                insertEachPackage(mContext, "two2", "three2", "four2", userAccount, dataDay, dataThree, "0");
//                return;
//            default:
//                return;
//        }
//    }
//
//    public HRDataDealer(Context context, String data) throws JSONException {
//        if (data != null) {
//            if (!data.equals("")) {
//                JSONObject json = JSONObjectInstrumentation.init(data);
//                String time = json.getString("time");
//                String heartRate = json.getString("heartRate");
//                if (heartRate == null || !heartRate.equals("")) {
//                    String account = UserAccountUtil.getAccount(context);
//                    if (createOneItemData(context, account, time) && heartRate != null && heartRate.contains(",")) {
//                        byte[] hexHR = com.huichenghe.xinlvsh01.Utils.FormatUtils.hexString2ByteArray(changeStringToHexString(heartRate));
//                        int countHRPack = hexHR.length / 180;
//                        for (int i = 0; i < countHRPack; i++) {
//                            byte[] eachBytes = getSubBytes(hexHR, i);
//                            switch (i) {
//                                case 0:
//                                    insertEachPackage(context, "one", "two", "three", account, time, eachBytes, "1");
//                                    break;
//                                case 1:
//                                    insertEachPackage(context, "four", "five", "six", account, time, eachBytes, "1");
//                                    break;
//                                case 2:
//                                    insertEachPackage(context, "seven", "eight", "nine", account, time, eachBytes, "1");
//                                    break;
//                                case 3:
//                                    insertEachPackage(context, "ten", "one1", "two1", account, time, eachBytes, "1");
//                                    break;
//                                case 4:
//                                    insertEachPackage(context, "three1", "four1", "five1", account, time, eachBytes, "1");
//                                    break;
//                                case 5:
//                                    insertEachPackage(context, "six1", "seven1", "eight1", account, time, eachBytes, "1");
//                                    break;
//                                case 6:
//                                    insertEachPackage(context, "nine1", "ten1", "one2", account, time, eachBytes, "1");
//                                    break;
//                                case 7:
//                                    insertEachPackage(context, "two2", "three2", "four2", account, time, eachBytes, "1");
//                                    break;
//                                default:
//                                    break;
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private byte[] getSubBytes(byte[] hexHR, int i) {
//        byte[] eachBytes = new byte[180];
//        System.arraycopy(hexHR, i * 180, eachBytes, 0, eachBytes.length);
//        return eachBytes;
//    }
//
//    private String changeStringToHexString(String heartRate) {
//        String[] hrData = heartRate.split(",");
//        StringBuffer sb = new StringBuffer();
//        for (String parseInt : hrData) {
//            String a = Integer.toHexString(Integer.parseInt(parseInt));
//            if (a.length() == 1) {
//                a = "0" + a;
//            }
//            sb.append(a);
//        }
//        Log.i(this.TAG, "服务器心率数据" + sb.toString());
//        return sb.toString();
//    }
//
//    private void insertEachPackage(Context context, String one, String two, String three, String userAccount, String day, byte[] dataThree, String flag) {
//        if (dataThree.length >= 180) {
//            for (int j = 0; j < dataThree.length / 60; j++) {
//                byte[] byteOne = new byte[60];
//                for (int i = j * 60; i < (byteOne.length * j) + 60; i++) {
//                    byteOne[i - (j * 60)] = dataThree[i];
//                }
//                if (j == 0) {
//                    MyDBHelperForDayData.getInstance(context).updateHr(context, userAccount, day, one, FormatUtils.bytesToHexString(byteOne), DeviceTypeUtils.getDeviceType(context), flag);
//                }
//                if (j == 1) {
//                    MyDBHelperForDayData.getInstance(context).updateHr(context, userAccount, day, two, FormatUtils.bytesToHexString(byteOne), DeviceTypeUtils.getDeviceType(context), flag);
//                }
//                if (j == 2) {
//                    MyDBHelperForDayData.getInstance(context).updateHr(context, userAccount, day, three, FormatUtils.bytesToHexString(byteOne), DeviceTypeUtils.getDeviceType(context), flag);
//                }
//            }
//        }
//    }
//
//    private boolean createOneItemData(Context mContext, String userAccount, String dataDay) {
//        Cursor mCursor = MyDBHelperForDayData.getInstance(mContext).selectHrAccount(mContext, userAccount, dataDay, DeviceTypeUtils.getDeviceType(mContext));
//        if (mCursor.getCount() == 0) {
//            MyDBHelperForDayData.getInstance(mContext).insertHrAccount(mContext, userAccount, dataDay, DeviceTypeUtils.getDeviceType(mContext));
//            mCursor.close();
//            return true;
//        }
//        boolean isOK = checkCanUpdate(mCursor);
//        mCursor.close();
//        return isOK;
//    }
//
//    private boolean checkCanUpdate(Cursor mCursor) {
//        if (mCursor.moveToFirst()) {
//            do {
//                String dataSend = mCursor.getString(mCursor.getColumnIndex("dataSendOK"));
//                if (dataSend != null && dataSend.equals("0")) {
//                    return false;
//                }
//            } while (mCursor.moveToNext());
//        }
//        return true;
//    }
}
