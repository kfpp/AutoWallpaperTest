package com.ye.example.autowallpapper.views.dialogs;

import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.ye.example.autowallpapper.R;
import com.ye.example.autowallpapper.base.dialog.FixHeightBottomSheetDialog;

import java.io.File;
import java.util.List;

/**
 * @author yezhihao
 * @date 2019-07-01 14:51
 */
public class PathResultDialog extends LinearLayout {

    public static void show(Context context,String pathSelected, List<String> pathRecommendList, IResultBackListener listener) {
        FixHeightBottomSheetDialog dialog = new FixHeightBottomSheetDialog(context);
        PathResultDialog dialogView = new PathResultDialog(context);
        dialog.setContentView(dialogView);
        dialogView.setData(pathSelected, pathRecommendList);
        dialogView.mTarget = dialog;
        dialogView.mBackListener = listener;
        dialog.show();
    }

    public interface IResultBackListener {
        void onPathSelected(List<String> pathList);
    }


    private RecyclerView mRecyclerView;
    private CheckBox mCheckBox;
    private ImageView mIvImage;
    private TextView mTvName;
    private List<String> mPathList;
    private String mPathShow;
    private BottomSheetDialog mTarget;
    private IResultBackListener mBackListener;
    private SelectableAdapter mAdapter;
    public PathResultDialog(Context context) {
        this(context, null);
    }

    public PathResultDialog(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PathResultDialog(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.dialog_path_result, this);
        mRecyclerView = findViewById(R.id.recycler_view);
        mCheckBox = findViewById(R.id.check_box);
        mIvImage = findViewById(R.id.iv_image);
        mTvName = findViewById(R.id.tv_name);
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mAdapter.checkAll(isChecked);
            }
        });

        findViewById(R.id.tv_btn_submit).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBackListener != null) {
                    List<String> pathList = mAdapter.getSelectedPath();
                    pathList.add(mPathShow);
                    mBackListener.onPathSelected(pathList);
                }
                mTarget.dismiss();
            }
        });

    }

    public void setData(String pathShow, List<String> data) {
        mPathList = data;
        mPathShow = pathShow;
        init();
    }

    private void init() {
        Glide.with(this).load(mPathShow).thumbnail(0.2f).into(mIvImage);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mAdapter = new SelectableAdapter(mPathList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new CommonAdapter.ItemDecoration());
        File file = new File(mPathShow);
        if (file.exists()) {
            String name = file.getName();
            mTvName.setText(name);
        }
    }

}

