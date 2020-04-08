package com.ye.example.autowallpapper.components.live;

import android.app.WallpaperInfo;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.ye.example.autowallpapper.R;
import com.ye.example.autowallpapper.components.LiveWallpaperService;

/*
 * Created by yuzhicong on 2020/3/5.
 */
public class LiveWallpaperManager {

    public static Bitmap getTestWallPaper(Context context) {
        return BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_app);
    }

    /**
     * 判断是否是使用我们的壁纸
     *
     * @param paramContext
     * @return
     */
    public static boolean wallpaperIsUsed(Context paramContext) {
        WallpaperInfo localWallpaperInfo = WallpaperManager.getInstance(paramContext).getWallpaperInfo();
        return ((localWallpaperInfo != null) && (localWallpaperInfo.getPackageName().equals(paramContext.getPackageName())) &&
                (localWallpaperInfo.getServiceName().equals(LiveWallpaperService.class.getCanonicalName())));
    }



    public static Bitmap getDevicesLocalWallpaper(Context context) {
        // 获取壁纸管理器
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
        // 获取当前壁纸
        Drawable wallpaperDrawable = wallpaperManager.getDrawable();
        // 将Drawable,转成Bitmap
        Bitmap bm = ((BitmapDrawable) wallpaperDrawable).getBitmap();
        return bm;
    }

}
