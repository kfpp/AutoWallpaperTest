package com.ye.example.autowallpapper.components.live;

import android.content.Context;
import android.net.Uri;

import com.ye.example.autowallpapper.components.IWallPaperAPI;
import com.ye.example.autowallpapper.components.LiveWallpaperService;

/**
 * @author yezhihao 2020-03-23 16:15
 */
public class SimpleWallpaperAPI implements IWallPaperAPI {

    private static IWallPaperAPI sInstance;

    public static IWallPaperAPI getInstance() {
        if (sInstance == null) {
            sInstance = new SimpleWallpaperAPI();
        }

        return sInstance;
    }

    @Override
    public boolean setWallPaper(Context context, Uri uri) {
        if (LiveWallpaperManager.wallpaperIsUsed(context)) {
            LiveWallpaperService.wallpaperChanged(context, uri);
            return false;
        } else {
            LiveWallpaperService.setToWallPaper(context, uri);
            return true;
        }
    }

    @Override
    public boolean isLiveWallpaperServiceInUse(Context context) {
        return LiveWallpaperManager.wallpaperIsUsed(context);
    }
}
