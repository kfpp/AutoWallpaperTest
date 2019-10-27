package com.ye.example.autowallpapper.module.settings.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ye.example.autowallpapper.R;

import java.util.ArrayList;
import java.util.List;

public class SettingRecyclerView extends RecyclerView {
    private static final String GROUP_NAME = "_group";

    private Adapter mAdapter;
    public SettingRecyclerView(Context context) {
        this(context, null);
    }

    public SettingRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mAdapter = new Adapter();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setLayoutManager(new LinearLayoutManager(getContext()));
        setAdapter(mAdapter);
    }

    public void setSettingItems(List<SettingGroup> groupList) {
        List<SettingItem> itemList = new ArrayList<>();
        for (int i = 0; i < groupList.size(); i++) {
            SettingGroup group = groupList.get(i);
            if (group != null && group.getItemList() != null && group.getItemList().size() > 0) {
                itemList.add(generateGroupItem(i));
                itemList.addAll(group.getItemList());
            }
        }

        mAdapter.setGroups(itemList);
    }

    private SettingItem generateGroupItem(int groupIndex) {
        return new SettingItem(GROUP_NAME, groupIndex, null);
    }

    private static final class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvName;
        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvName = itemView.findViewById(R.id.tv_name);
        }

        private void setName(String name) {
            if (!TextUtils.isEmpty(name)) {
                mTvName.setText(name);
            } else {
                mTvName.setText("");
            }
        }
    }


    private static final class Adapter extends RecyclerView.Adapter<ViewHolder> {

        private static final int TYPE_GROUP = 11;
        private static final int TYPE_ITEM = 21;

        private List<SettingItem> mItemList;

        private Adapter() {
            mItemList = new ArrayList<>();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int itemType) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View root = null;
            if (itemType == TYPE_ITEM) {
                root = inflater.inflate(R.layout.simple_list_item_1, viewGroup, false);
            } else {
                root = inflater.inflate(R.layout.setting_group_split, viewGroup, false);
            }

            return new ViewHolder(root);
        }

        private void setGroups(List<SettingItem> items) {
            mItemList.clear();
            mItemList.addAll(items);
            notifyDataSetChanged();
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
            final int itemType = getItemViewType(position);
            if (itemType == TYPE_ITEM) {
                SettingItem item = mItemList.get(position);
                viewHolder.setName(item.getName());
                viewHolder.itemView.setOnClickListener(item.getOnClickListener());
            }
        }

        @Override
        public int getItemCount() {
            return mItemList.size();
        }

        @Override
        public int getItemViewType(int position) {
            SettingItem item = mItemList.get(position);
            if (item.getName().equals(GROUP_NAME)) {
                return TYPE_GROUP;
            } else {
                return TYPE_ITEM;
            }
        }
    }

}
