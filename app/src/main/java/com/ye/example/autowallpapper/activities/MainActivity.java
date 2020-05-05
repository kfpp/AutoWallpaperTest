package com.ye.example.autowallpapper.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.ye.example.autowallpapper.R;
import com.ye.example.autowallpapper.base.BaseActivity;
import com.ye.example.autowallpapper.data.database.FileDataBase;
import com.ye.example.autowallpapper.data.entities.ImageDirectory;
import com.ye.example.autowallpapper.presenters.main.EditModePresenter;
import com.ye.example.autowallpapper.presenters.main.IModePresenter;
import com.ye.example.autowallpapper.presenters.main.LiveGuidePresenter;
import com.ye.example.autowallpapper.presenters.main.NormalModePresenter;
import com.ye.example.autowallpapper.utils.FileBrowserUtils;
import com.ye.example.autowallpapper.utils.FileRecommendUtil;
import com.ye.example.autowallpapper.viewmodels.MainViewModel;
import com.ye.example.autowallpapper.views.FileRecyclerView;
import com.ye.example.autowallpapper.views.dialogs.PathResultDialog;

import java.io.File;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener, PathResultDialog.IResultBackListener {

    private FileBrowserUtils mFileBrowserUtils;
    private FileRecyclerView mRecyclerView;
    private FloatingActionButton mFab;
    private Toolbar mToolbar;
    private IModePresenter mNormalModePresenter;
    private IModePresenter mEditModePresenter;
    private IModePresenter mCurrentModePresenter;
    private LiveGuidePresenter mGuidePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(this);
        mFileBrowserUtils = new FileBrowserUtils(this);
        mNormalModePresenter = new NormalModePresenter(this);
        mEditModePresenter = new EditModePresenter(this);
        mGuidePresenter = new LiveGuidePresenter(this);
        mCurrentModePresenter = mNormalModePresenter;
        mRecyclerView = findViewById(R.id.recycler_view);

        mRecyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("yyyy", " position : " + position + ", id : " + id);
            }
        });

        mRecyclerView.setmEditModeListener(new FileRecyclerView.IEditModeListener() {
            @Override
            public void onEnterEditMode(int currentIndex) {
                mCurrentModePresenter = mEditModePresenter;
                mCurrentModePresenter.enterMode();
            }

            @Override
            public void onExitEditMode() {
                mCurrentModePresenter = mNormalModePresenter;
                mCurrentModePresenter.enterMode();
            }
        });

        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getDirectoryLiveData().observe(this, new Observer<List<ImageDirectory>>() {
            @Override
            public void onChanged(@Nullable List<ImageDirectory> directories) {
                mRecyclerView.setData(directories);
            }
        });

        viewModel.loadDirectories();
        mGuidePresenter.checkAndShowGuidePage();

        viewModel.preloadTestData();
//        viewModel.showImages();
//        ImageView ivCover = findViewById(R.id.iv_cover);
//        Glide.with(this).load(Uri.parse("content://media/external/images/media/302929")).into(ivCover);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mCurrentModePresenter.onBackPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            mCurrentModePresenter.onMenuClicked();
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
        mCurrentModePresenter.onFloatBtnClicked();
    }

    public FloatingActionButton getFab() {
        return mFab;
    }

    public FileBrowserUtils getFileBrowserUtils() {
        return mFileBrowserUtils;
    }

    public MenuItem getSettingMenuItem() {
        Menu menu = mToolbar.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.getItemId() == R.id.action_settings) {
                return item;
            }
        }
        return null;
    }

    public FileRecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    @Override
    public void onPathSelected(List<String> pathList) {
        if (pathList.size() > 0) {
            String parentPath = new File(pathList.get(0)).getParentFile().getAbsolutePath();
            FileDataBase.getInstance().insertDirectoryAndFiles(parentPath, pathList);
        }
    }

}
