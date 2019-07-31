package com.ye.example.autowallpapper.base;

import android.app.Application;
import android.content.Context;

import com.ye.example.autowallpapper.common.Initializer;
import com.ye.example.autowallpapper.data.database.FileDataBase;
import com.ye.example.autowallpapper.utils.GlobalData;

/**
 * @author yezhihao
 * @date 2019-07-01 10:59
 */
public class YEApp extends Application {

    private static YEApp sInstance;

    public static YEApp getInstance() {
        return sInstance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        Context context = getApplicationContext();
        FileDataBase.initDataBase(context);
        Initializer.init(context);
        GlobalData.getInstance().initImages();
    }
}
