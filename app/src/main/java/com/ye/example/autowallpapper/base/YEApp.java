package com.ye.example.autowallpapper.base;

import android.app.Application;
import android.content.Context;

import com.ye.example.autowallpapper.MainActivity;
import com.ye.example.autowallpapper.common.Initializer;
import com.ye.example.autowallpapper.data.database.FileDataBase;

import java.util.List;

/**
 * @author yezhihao
 * @date 2019-07-01 10:59
 */
public class YEApp extends Application {

    private List<String> mImageList;

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
    }


    public List<String> getImageList() {
        return mImageList;
    }

    public void setImageList(List<String> imageList) {
        mImageList = imageList;
    }
}
