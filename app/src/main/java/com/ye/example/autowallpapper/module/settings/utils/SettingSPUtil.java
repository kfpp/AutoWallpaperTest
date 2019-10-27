package com.ye.example.autowallpapper.module.settings.utils;

import android.content.SharedPreferences;

import com.ye.example.autowallpapper.common.Constant;
import com.ye.example.autowallpapper.utils.SpUtil;

public class SettingSPUtil {
    public static final int DEFAULT_DURATION = 5;
    private static SharedPreferences sSp;
    static {
        sSp = SpUtil.getSp(SpUtil.SPType.spSetting);
    }

    public static int getShowDuration() {
        return sSp.getInt(Constant.SettingSpId.duration, DEFAULT_DURATION);
    }

    public static void setShowDuration(int duration) {
        sSp.edit().putInt(Constant.SettingSpId.duration, duration).apply();
    }
}
