package com.ye.example.autowallpapper.common;

import android.content.Context;
import android.content.Intent;

import com.ye.example.autowallpapper.WallPaperService;

/**
 * @author yezhihao
 * @date 2019-07-08 17:13
 */
public class Initializer {
    public static void init(Context context) {
        Context applicationContext = context.getApplicationContext();
        applicationContext.startService(new Intent(applicationContext, WallPaperService.class));
    }
}
