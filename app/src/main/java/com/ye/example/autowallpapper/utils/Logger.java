package com.ye.example.autowallpapper.utils;

import android.util.Log;

import com.ye.example.autowallpapper.BuildConfig;

/**
 * @author yezhihao
 * @date 2019-07-31 15:01
 */
public class Logger {

    private static boolean enableLog = BuildConfig.DEBUG;

    public static void d(String tag, String text) {
        if (enableLog) {
            Log.d(tag, text);
        }
    }

    public static void w(String tag, String text) {
        if (enableLog) {
            Log.w(tag, text);
        }
    }

    public static void e(String tag, String text) {
        if (enableLog) {
            Log.e(tag, text);
        }
    }

    public static void i(String tag, String text) {
        if (enableLog) {
            Log.i(tag, text);
        }
    }
}
