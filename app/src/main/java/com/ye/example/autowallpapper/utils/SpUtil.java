package com.ye.example.autowallpapper.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.StringDef;

import com.ye.example.autowallpapper.base.YEApp;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author yezhihao 2019-08-05 14:52
 */
public class SpUtil {

    private static SharedPreferences getSp() {
        return getSp(SPType.spDefault);
    }

    public static SharedPreferences getSp(@SPType String spType) {
        return YEApp.getInstance().getSharedPreferences(spType, Context.MODE_PRIVATE);
    }

    public static void put(String key, long value) {
        getSp().edit().putLong(key, value).apply();
    }

    public static long get(String key, long defaultValue) {
        return getSp().getLong(key, defaultValue);
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({SPType.spDefault, SPType.spSetting})
    public @interface SPType {
        String spDefault = "default_sp";
        String spSetting = "setting_sp";
    }
}
