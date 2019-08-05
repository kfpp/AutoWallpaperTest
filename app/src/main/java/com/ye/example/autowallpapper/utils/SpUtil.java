package com.ye.example.autowallpapper.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.ye.example.autowallpapper.base.YEApp;

/**
 * @author yezhihao 2019-08-05 14:52
 */
public class SpUtil {

    private static final String SP_FILE_NAME = "default_sp";

    private static SharedPreferences getSp() {
        return YEApp.getInstance().getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
    }

    public static void put(String key, long value) {
        getSp().edit().putLong(key, value).apply();
    }

    public static long get(String key, long defaultValue) {
        return getSp().getLong(key, defaultValue);
    }

}
