package com.ye.example.autowallpapper.components;

import android.content.Context;
import android.net.Uri;

/**
 * @author yezhihao 2020-03-23 16:12
 */
public interface IWallPaperAPI {
    boolean setWallPaper(Context context, Uri uri);

    boolean isLiveWallpaperServiceInUse(Context context);
}
