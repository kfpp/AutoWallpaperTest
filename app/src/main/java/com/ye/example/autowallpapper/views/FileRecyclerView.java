package com.ye.example.autowallpapper.views;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * @author yezhihao 2019-07-31 15:53
 */
public class FileRecyclerView extends RecyclerView {


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
        final Adapter adapter = new Adapter();
        setLayoutManager(new LinearLayoutManager(getContext()));
        setAdapter(adapter);

        AppCompatActivity activity = (AppCompatActivity) getContext();
        MainViewModel viewModel = ViewModelProviders.of(activity).get(MainViewModel.class);
        viewModel.getDirectoryLiveData().observe(activity, new Observer<List<ImageDirectory>>() {
            @Override
            public void onChanged(@Nullable List<ImageDirectory> directories) {
                adapter.setList(DataAdapterFactory.parseDirectory(directories));
            }
        });

        viewModel.loadDirectories();
    }

    private static final class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvName, mTvDesc, mTvCount;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvName = itemView.findViewById(R.id.tv_name);
            mTvDesc = itemView.findViewById(R.id.tv_desc);
            mTvCount = itemView.findViewById(R.id.tv_count);
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
    }

    private static final class Adapter extends RecyclerView.Adapter<ViewHolder> {

        private List<DataAdapter> mList;
        private Map<String, Integer> mCountMap;

        private Adapter() {
            mList = new ArrayList<>();
            mCountMap = new HashMap<>();
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
            final DataAdapter data = mList.get(i);
            viewHolder.setName(data.mName);
            viewHolder.setDesc(data.mDesc);

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


}
