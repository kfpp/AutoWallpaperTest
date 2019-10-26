package com.ye.example.autowallpapper.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.ye.example.autowallpapper.R;
import com.ye.example.autowallpapper.base.BaseActivity;
import com.ye.example.autowallpapper.common.Initializer;
import com.ye.example.autowallpapper.data.database.FileDataBase;
import com.ye.example.autowallpapper.data.entities.ImageDirectory;
import com.ye.example.autowallpapper.data.entities.ImageFile;
import com.ye.example.autowallpapper.utils.FileBrowserUtils;
import com.ye.example.autowallpapper.utils.FileRecommendUtil;
import com.ye.example.autowallpapper.utils.FileUtil;
import com.ye.example.autowallpapper.viewmodels.MainViewModel;
import com.ye.example.autowallpapper.views.FileRecyclerView;
import com.ye.example.autowallpapper.views.dialogs.PathResultDialog;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements View.OnClickListener, PathResultDialog.IResultBackListener {

    private FileBrowserUtils mFileBrowserUtils;
    private FileRecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);
        mFileBrowserUtils = new FileBrowserUtils(this);

        mRecyclerView = findViewById(R.id.recycler_view);

        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getDirectoryLiveData().observe(this, new Observer<List<ImageDirectory>>() {
            @Override
            public void onChanged(@Nullable List<ImageDirectory> directories) {
                mRecyclerView.setData(directories);
            }
        });

        viewModel.loadDirectories();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            SettingActivity.startActivity(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mFileBrowserUtils.isResult(requestCode)) {
            String path = mFileBrowserUtils.onActivityResult(requestCode, resultCode, data);
            if (!TextUtils.isEmpty(path)) {
                Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
                PathResultDialog.show(this, path, FileRecommendUtil.getRecommendFiles(path), this);
            }
        }
    }

    @Override
    public void onClick(View v) {
        mFileBrowserUtils.showFileBorwser();
    }

    @Override
    public void onPathSelected(List<String> pathList) {
        if (pathList.size() > 0) {
            String parentPath = new File(pathList.get(0)).getParentFile().getAbsolutePath();
            FileDataBase.getInstance().insertDirectoryAndFiles(parentPath, pathList);
        }
    }

}
