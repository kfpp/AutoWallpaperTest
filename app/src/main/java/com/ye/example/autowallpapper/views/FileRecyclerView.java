package com.ye.example.autowallpapper.views;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.ye.example.autowallpapper.R;
import com.ye.example.autowallpapper.data.entities.ImageDirectory;
import com.ye.example.autowallpapper.data.entities.ImageFile;
import com.ye.example.autowallpapper.viewmodels.MainViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author yezhihao 2019-07-31 15:53
 */
public class FileRecyclerView extends RecyclerView implements AdapterView.OnItemLongClickListener {

    private Adapter mAdapter;

    private IEditModeListener mEditModeListener;

    private boolean mIsInEditMode;

    public FileRecyclerView(Context context) {
        this(context, null);
    }

    public FileRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FileRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mIsInEditMode = false;
        mAdapter = new Adapter();
        setLayoutManager(new LinearLayoutManager(getContext()));
        setAdapter(mAdapter);
        mAdapter.setOnItemLongClickListener(this);
    }

    public void setmEditModeListener(IEditModeListener mEditModeListener) {
        this.mEditModeListener = mEditModeListener;
    }

    public void exitEditMode() {
        mIsInEditMode = false;
        if (mEditModeListener != null) {
            mEditModeListener.onExitEditMode();
        }
        mAdapter.setEditModeEnable(false);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (!mIsInEditMode){
            mIsInEditMode = true;
            if (mEditModeListener != null) {
                mEditModeListener.onEnterEditMode(position);
            }
            setEditModeChanged(mIsInEditMode);
            return true;
        }
        return false;
    }

    private void setEditModeChanged(boolean isInEditMode) {
        mAdapter.setEditModeEnable(isInEditMode);
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener itemClickListener) {
        mAdapter.setOnItemClickListener(itemClickListener);
    }

    public void setData(List<ImageDirectory> data) {
        final List<DataAdapter> emptyList = new ArrayList<>();
        if (data == null || data.size() == 0) {
            mAdapter.setList(emptyList);
            return;
        }
        Single.just(data)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(new Function<List<ImageDirectory>, List<DataAdapter>>() {
                    @Override
                    public List<DataAdapter> apply(List<ImageDirectory> imageDirectories) throws Exception {
                        return DataAdapterFactory.parseDirectory(imageDirectories);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<DataAdapter>>() {
                    @Override
                    public void accept(List<DataAdapter> dataAdapters) throws Exception {
                        mAdapter.setList(dataAdapters);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mAdapter.setList(emptyList);
                    }
                });
    }

    private static final class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvName, mTvDesc, mTvCount;
        private AppCompatCheckBox mCheckBox;
        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvName = itemView.findViewById(R.id.tv_name);
            mTvDesc = itemView.findViewById(R.id.tv_desc);
            mTvCount = itemView.findViewById(R.id.tv_count);
            mCheckBox = itemView.findViewById(R.id.check_box);
        }

        private void setName(String name) {
            mTvName.setText(name);
        }

        private void setDesc(String desc) {
            mTvDesc.setText(desc);
        }

        private void setCount(int count) {
            String text = "数量：" + count;
            mTvCount.setText(text);
        }

        private void setEditMode(boolean isEditMode) {
            mCheckBox.setVisibility(isEditMode ? VISIBLE : GONE);
            mTvCount.setVisibility(isEditMode ? GONE : VISIBLE);
            if (!isEditMode) {
                mCheckBox.setChecked(false);
            }
        }
    }

    private static final class Adapter extends RecyclerView.Adapter<ViewHolder> {

        private List<DataAdapter> mList;
        private Map<String, Integer> mCountMap;
        private AdapterView.OnItemClickListener mOnItemClickListener;
        private AdapterView.OnItemLongClickListener mOnItemLongClickListener;
        private boolean mIsInEditMode;
        private Adapter() {
            mList = new ArrayList<>();
            mCountMap = new HashMap<>();
        }

        private void setOnItemClickListener(AdapterView.OnItemClickListener mOnItemClickListener) {
            this.mOnItemClickListener = mOnItemClickListener;
        }

        private void setOnItemLongClickListener(AdapterView.OnItemLongClickListener mOnItemLongClickListener) {
            this.mOnItemLongClickListener = mOnItemLongClickListener;
        }

        private void setEditModeEnable(boolean isInEditMode) {
            mIsInEditMode = isInEditMode;
            notifyDataSetChanged();
        }

        public void setList(List<DataAdapter> list) {
            mList.clear();
            mList.addAll(list);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View root = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_file_detail, viewGroup, false);
            return new ViewHolder(root);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
            final int index = i;
            final DataAdapter data = mList.get(i);
            viewHolder.setName(data.mName);
            viewHolder.setDesc(data.mDesc);
            viewHolder.setEditMode(mIsInEditMode);

            if (mOnItemClickListener != null) {
                viewHolder.itemView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(null, v, index, getItemId(index));
                    }
                });
            }

            if (mOnItemLongClickListener != null) {
                viewHolder.itemView.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return mOnItemLongClickListener.onItemLongClick(null, v, index, getItemId(index));
                    }
                });
            }

            if (mCountMap.containsKey(data.mPath)) {
                Integer count = mCountMap.get(data.mPath);
                viewHolder.setCount(count != null ? count : 0);
            } else {
                AppCompatActivity activity = (AppCompatActivity) viewHolder.itemView.getContext();
                final MainViewModel viewModel = ViewModelProviders.of(activity).get(MainViewModel.class);
                Observer<Integer> observer = new Observer<Integer>() {
                    @Override
                    public void onChanged(@Nullable Integer integer) {
                        int count = integer == null ? -1 : integer;
                        mCountMap.put(data.mPath, count);
                        Log.d("yyyy", "directory : " + data.mName + " image count : " + integer);
                        viewHolder.setCount(count);
                        viewModel.getImageCountLiveData(data.mPath).removeObserver(this);
                    }
                };
                viewModel.getImageCountLiveData(data.mPath).observe(activity, observer);
                viewModel.loadDirectoryFileCount(data.mPath);
            }
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }

    private static final class DataAdapter {
        private String mName;
        private String mDesc;
        private String mPath;
    }

    private static final class DataAdapterFactory {
        private static List<DataAdapter> parseFile(List<ImageFile> files) {
            List<DataAdapter> list = new ArrayList<>();
            if (files == null || files.size() == 0) {
                return list;
            }

            for (ImageFile fileData : files) {
                DataAdapter adapter = new DataAdapter();
                File file = new File(fileData.getFilePath());

                adapter.mName = file.getName();
                adapter.mDesc = file.getAbsolutePath();
                adapter.mPath = adapter.mDesc;
                list.add(adapter);
            }

            return list;
        }

        private static List<DataAdapter> parseDirectory(List<ImageDirectory> directories) {
            List<DataAdapter> list = new ArrayList<>();
            if (directories == null || directories.size() == 0) {
                return list;
            }

            for (ImageDirectory directory : directories) {
                DataAdapter adapter = new DataAdapter();
                File file = new File(directory.getPath());
                adapter.mName = file.getName();
                adapter.mDesc = file.getAbsolutePath();
                adapter.mPath = adapter.mDesc;
                list.add(adapter);
            }

            return list;
        }
    }

    public interface IEditModeListener {
        void onEnterEditMode(int currentIndex);

        void onExitEditMode();
    }

}
