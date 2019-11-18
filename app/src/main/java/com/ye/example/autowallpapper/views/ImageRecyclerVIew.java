package com.ye.example.autowallpapper.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ye.example.autowallpapper.R;

/**
 * @author yezhihao 2019-11-18 15:07
 */
public class ImageRecyclerVIew extends RecyclerView {
    public ImageRecyclerVIew(Context context) {
        this(context, null);
    }

    public ImageRecyclerVIew(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageRecyclerVIew(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        addItemDecoration(new ItemDecoration(context));
    }

    private static final class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private static final class Adapter extends RecyclerView.Adapter<ViewHolder> {
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View root = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_image_list, viewGroup, false);
            return new ViewHolder(root);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }

    private final static class ItemDecoration extends RecyclerView.ItemDecoration {
        private int mItem1Margin, mItem2Margin, mItem3Margin;
        private int mCommonTopBottomMargin;
        private ItemDecoration(Context context) {
            Resources resources = context.getResources();
            mItem1Margin = resources.getDimensionPixelOffset(R.dimen.image_list_item_1_padding);
            mItem2Margin = resources.getDimensionPixelOffset(R.dimen.image_list_item_2_padding);
            mItem3Margin = resources.getDimensionPixelOffset(R.dimen.image_list_item_3_padding);
            mCommonTopBottomMargin = resources.getDimensionPixelOffset(R.dimen.image_list_common_item_top_bottom_margin);
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            if (parent.getChildLayoutPosition(view) % 3 == 0) {
                //第一列
                outRect.right = mItem1Margin;
                outRect.left = 0;
                outRect.bottom = mCommonTopBottomMargin;
                outRect.top = mCommonTopBottomMargin;
            } else if (parent.getChildLayoutPosition(view) % 3 == 1) {
                //第二列
                outRect.left = mItem2Margin;
                outRect.right = mItem2Margin;
                outRect.bottom = mCommonTopBottomMargin;
                outRect.top = mCommonTopBottomMargin;
            } else {
                outRect.left = mItem3Margin;
                outRect.right = 0;
                outRect.bottom = mCommonTopBottomMargin;
                outRect.top = mCommonTopBottomMargin;
            }
        }
    }



}
