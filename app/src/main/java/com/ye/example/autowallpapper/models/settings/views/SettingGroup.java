package com.ye.example.autowallpapper.models.settings.views;

import java.util.ArrayList;
import java.util.List;

public class SettingGroup {
    List<SettingItem> mItemList;

    public SettingGroup() {
        mItemList = new ArrayList<>();
    }

    public void addItem(SettingItem item) {
        mItemList.add(item);
    }

    public List<SettingItem> getItemList() {
        return mItemList;
    }
}
