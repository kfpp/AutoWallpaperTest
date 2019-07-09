package com.ye.example.autowallpapper.base;

import android.app.Application;

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
    }


    public List<String> getImageList() {
        return mImageList;
    }

    public void setImageList(List<String> imageList) {
        mImageList = imageList;
    }
}
