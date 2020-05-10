package com.ye.example.autowallpapper.common;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import com.ye.example.autowallpapper.components.WallPaperService;

/**
 * @author yezhihao
 * @date 2019-07-08 17:13
 */
public class Initializer {
    public static void init(Context context) {
        Context applicationContext = context.getApplicationContext();
        applicationContext.startService(new Intent(applicationContext, WallPaperService.class));
    }

    public static void initWithStoragePermission(Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            init(context);
        }
    }
}
