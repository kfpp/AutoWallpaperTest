package com.ye.example.autowallpapper.views.dialogs;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ye.example.autowallpapper.R;

import java.util.List;

/**
 * @author yezhihao
 * @date 2019-07-01 16:43
 */
public class CommonAdapter extends RecyclerView.Adapter<ImageItemViewHolder> {

    List<String> mPathList;

    public CommonAdapter(List<String> pathList) {
        mPathList = pathList;

    }

    @NonNull
    @Override
    public ImageItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View imageView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dialog_image_reommend, parent, false);
        return new ImageItemViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageItemViewHolder holder, final int position) {
        final String path = mPathList.get(position);
        holder.setImagePath(path);
        holder.setCheckVisiable(false);
    }

    @Override
    public int getItemCount() {
        return mPathList.size();
    }

    public final static class ItemDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            if (parent.getChildLayoutPosition(view) % 3 == 0) {
                //第一列
                outRect.right = 30;
                outRect.left = 30;
                outRect.bottom = 30;
            } else if (parent.getChildLayoutPosition(view) % 3 == 1) {
                //第二列
                outRect.left = 0;
                outRect.right = 0;
                outRect.bottom = 30;
            } else {
                outRect.left = 30;
                outRect.right = 30;
                outRect.bottom = 30;
            }
        }
    }
}
