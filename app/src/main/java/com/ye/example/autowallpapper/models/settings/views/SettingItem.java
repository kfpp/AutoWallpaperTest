package com.ye.example.autowallpapper.models.settings.views;

import android.view.View;

public class SettingItem {
    private String mName;
    private int mId;
    private View.OnClickListener mOnClickListener;

    public SettingItem(String name, int id, View.OnClickListener onClickListener) {
        mName = name;
        mId = id;
        mOnClickListener = onClickListener;
    }

    public String getName() {
        return mName;
    }

    public int getId() {
        return mId;
    }

    public View.OnClickListener getOnClickListener() {
        return mOnClickListener;
    }
}
