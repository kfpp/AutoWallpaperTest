package com.ye.example.autowallpapper.base;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yezhihao
 * @date 2019-07-01 10:59
 */
public class BaseActivity extends AppCompatActivity {
    public static final String[] PERMISSIONS = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
    public static final int PERMISSION_REQUEST_CODE = 1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissions();
    }

    private void checkPermissions() {
        List<String> permissionNeed = new ArrayList<>();
        for (String permission : PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this,permission) != PackageManager.PERMISSION_GRANTED) {
                permissionNeed.add(permission);
            }
        }

        if (permissionNeed.size() > 0) {
            String[] permissionArr = permissionNeed.toArray(new String[permissionNeed.size()]);
            ActivityCompat.requestPermissions(this, permissionArr, PERMISSION_REQUEST_CODE);
        }
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (PERMISSION_REQUEST_CODE == requestCode) {

        }
    }*/
}
