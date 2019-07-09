package com.ye.example.autowallpapper;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.ye.example.autowallpapper.base.BaseActivity;
import com.ye.example.autowallpapper.common.Initializer;
import com.ye.example.autowallpapper.utils.FileUtil;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    private TextView mTvMain;
    private boolean mIsServiceStarted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mIsServiceStarted = false;
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);
        mTvMain = findViewById(R.id.tv_main);

        final long timeStart = System.currentTimeMillis();
        Single<Integer> observable = Single.create(new SingleOnSubscribe<Integer>() {
            @Override
            public void subscribe(SingleEmitter<Integer> emitter) throws Exception {
                int count = FileUtil.getSubFileCount(FileUtil.DEFAULT_IMAGE_DIRECTORY_PATH);
                emitter.onSuccess(count);
            }
        });
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        long timeEnd = System.currentTimeMillis();
                        mTvMain.setText(FileUtil.DEFAULT_IMAGE_DIRECTORY_PATH + " , count: " + integer + " cost time : " + (timeEnd - timeStart));
                        if (integer.intValue() > 0) {
                            mIsServiceStarted = true;
                            Initializer.init(MainActivity.this);
                        }
                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
//        Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show();
        if (!mIsServiceStarted) {
            mIsServiceStarted = true;
            startService(new Intent(this, WallPaperService.class));
        } else {
            mIsServiceStarted = false;
            stopService(new Intent(this, WallPaperService.class));
        }
    }

}
