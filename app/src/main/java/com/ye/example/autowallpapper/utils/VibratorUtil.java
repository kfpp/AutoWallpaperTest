package com.ye.example.autowallpapper.utils;

        import android.content.Context;
        import android.os.Vibrator;

public class VibratorUtil {
    public static void VibratorOnce(Context context) {
        Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(30);
    }
}
