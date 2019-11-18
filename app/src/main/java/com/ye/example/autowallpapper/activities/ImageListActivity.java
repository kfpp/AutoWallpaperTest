package com.ye.example.autowallpapper.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ye.example.autowallpapper.R;

public class ImageListActivity extends AppCompatActivity {

    public static final String EXTRA_PATH = "extra_path";

    public static void startActivity(Context context, String parentPathString) {
        Intent intent = new Intent(context, ImageListActivity.class);
        intent.putExtra(EXTRA_PATH, parentPathString);
        context.startActivity(intent);
    }

    private String mParentPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
        mParentPath = getIntent().getStringExtra(EXTRA_PATH);
    }
}
