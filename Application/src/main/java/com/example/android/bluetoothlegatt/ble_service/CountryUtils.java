package com.example.android.bluetoothlegatt.ble_service;

/**
 * @author Sopheak Tuon
 * @created on 04-Oct-17
 */

import java.util.Locale;

public class CountryUtils {
    public static boolean getMonthAndDayFormate() {
        Locale locale = Locale.getDefault();
        String lang = locale.getLanguage();
        String contr = locale.getCountry();
        if (lang == null || (!lang.equals("zh") && !lang.equals("ja") && !lang.equals("ko") && (!lang.equals("en") || contr == null || !contr.equals("US")))) {
            return false;
        }
        return true;
    }

    public static boolean getLanguageFormate() {
        String language = Locale.getDefault().getLanguage();
        if (language == null || !language.equals("zh")) {
            return false;
        }
        return true;
    }
}
