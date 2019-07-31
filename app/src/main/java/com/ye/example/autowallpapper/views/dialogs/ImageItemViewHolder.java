package com.ye.example.autowallpapper.views.dialogs;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ye.example.autowallpapper.R;

/**
 * @author yezhihao
 * @date 2019-07-01 16:43
 */
class ImageItemViewHolder extends RecyclerView.ViewHolder {
    private ImageView mIvCover;
    private CheckBox mCheckBox;
     ImageItemViewHolder(@NonNull View itemView) {
        super(itemView);
        mIvCover = itemView.findViewById(R.id.iv_cover);
        mCheckBox = itemView.findViewById(R.id.check_box);
        mCheckBox.setClickable(false);
    }

    void setImagePath(String path) {
        Glide.with(itemView).load(path).override(200).into(mIvCover);
    }

    void setCheck(boolean check) {
        setCheckVisiable(true);
        mCheckBox.setChecked(check);
    }

    boolean isCheck() {
        return mCheckBox.isChecked();
    }

    void setCheckVisiable(boolean visiable) {
        mCheckBox.setVisibility(visiable ? View.VISIBLE : View.GONE);
    }
}
