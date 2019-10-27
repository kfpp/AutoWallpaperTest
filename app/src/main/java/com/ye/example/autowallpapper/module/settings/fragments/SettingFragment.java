package com.ye.example.autowallpapper.module.settings.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ye.example.autowallpapper.R;
import com.ye.example.autowallpapper.module.settings.utils.SettingSPUtil;
import com.ye.example.autowallpapper.module.settings.views.DurationDialog;
import com.ye.example.autowallpapper.module.settings.views.SettingGroup;
import com.ye.example.autowallpapper.module.settings.views.SettingItem;
import com.ye.example.autowallpapper.module.settings.views.SettingRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SettingFragment extends Fragment {
    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SettingRecyclerView recyclerView = view.findViewById(R.id.recycler_view_setting);
        List<SettingGroup> list = new ArrayList<>();
        SettingGroup group = new SettingGroup();
        SettingItem item = new SettingItem(getResources().getString(R.string.show_duration, SettingSPUtil.getShowDuration()), 11, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DurationDialog.newInstance().show(getChildFragmentManager(), DurationDialog.class.getSimpleName());
            }
        });
        group.addItem(item);
        list.add(group);
        recyclerView.setSettingItems(list);
    }
}
