package com.ye.example.autowallpapper.views.dialogs;

import android.support.annotation.NonNull;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yezhihao
 * @date 2019-07-01 16:47
 */
public class SelectableAdapter extends CommonAdapter {
    private Map<String, Boolean> mPathSelectedMap;
    public SelectableAdapter(List<String> pathList) {
        super(pathList);
        mPathSelectedMap = new HashMap<>();
        for (String path : mPathList) {
            mPathSelectedMap.put(path, false);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageItemViewHolder holder, final int position) {
        final String path = mPathList.get(position);
        holder.setImagePath(path);

        holder.setCheck(mPathSelectedMap.get(path));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkItem(path, !holder.isCheck(), position);
                notifyItemChanged(position);
            }
        });
    }

    public List<String> getSelectedPath() {
        List<String> paths = new ArrayList<>();
        for (String key : mPathSelectedMap.keySet()) {
            if (mPathSelectedMap.get(key)) {
                paths.add(key);
            }
        }
        return paths;
    }

    private void checkItem(String path, boolean isCheck, int position) {
        mPathSelectedMap.put(path, isCheck);
    }

    public void checkAll(boolean check) {
        for (int i = 0; i < getItemCount(); i++) {
            checkItem(mPathList.get(i), check, i);
        }
        notifyDataSetChanged();
    }
}
