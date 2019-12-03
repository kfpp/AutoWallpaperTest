package com.ye.example.autowallpapper.utils;

import android.content.Context;
import android.content.res.Configuration;

/**
 * @author yezhihao 2019-12-03 19:11
 */
public class ScreenUtil {
    public static boolean isLandscape(Context context) {
        Configuration mConfiguration = context.getApplicationContext().getResources().getConfiguration(); //获取设置的配置信息
        int ori = mConfiguration.orientation; //获取屏幕方向
        return ori == Configuration.ORIENTATION_LANDSCAPE;
    }
}
