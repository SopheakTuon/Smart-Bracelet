package com.example.android.bluetoothlegatt.ble_service;

/**
 * @author Sopheak Tuon
 * @created on 05-Oct-17
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class LocalDataSaveTool {
    private static LocalDataSaveTool mLocalDataSaveTool;
    private Editor editor;
    private Context mContext;
    private SharedPreferences sp;
    private String spName = "LoaclData";

    private LocalDataSaveTool(Context mContext) {
        this.sp = mContext.getSharedPreferences(this.spName, 0);
        this.editor = this.sp.edit();
    }

    public static LocalDataSaveTool getInstance(Context mContext) {
        if (mLocalDataSaveTool == null) {
            mLocalDataSaveTool = new LocalDataSaveTool(mContext);
        }
        return mLocalDataSaveTool;
    }

    public void writeSp(String key, String value) {
        if (this.editor != null) {
            this.editor.putString(key, value);
            this.editor.commit();
        }
    }

    public String readSp(String key) {
        if (this.sp != null) {
            return this.sp.getString(key, "");
        }
        return null;
    }
}
