package com.ye.example.autowallpapper.presenters.main;

import android.content.Context;

import com.ye.example.autowallpapper.components.live.SimpleWallpaperAPI;

public class LiveGuidePresenter {
    private Context mContext;

    public LiveGuidePresenter(Context context) {
        mContext = context;
    }

    public boolean checkAndShowGuidePage() {
        if (!SimpleWallpaperAPI.getInstance().isLiveWallpaperServiceInUse(mContext)) {
            SimpleWallpaperAPI.getInstance().setWallPaper(mContext, null);
            return true;
        }

        return false;
    }
}
