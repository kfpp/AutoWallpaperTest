package com.ye.example.autowallpapper.utils;

import com.ye.example.autowallpapper.data.database.FileDataBase;
import com.ye.example.autowallpapper.data.entities.ImageFile;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author yezhihao 2019-07-31 18:08
 */
public class GlobalData {
    private static GlobalData sInstance;
    public static GlobalData getInstance(){
        if (sInstance == null) {
            sInstance = new GlobalData();
        }
        return sInstance;
    }

    private List<String> mImageList;

    private GlobalData() {
        mImageList = new ArrayList<>();
    }

    public void initImages() {
        FileDataBase.getInstance().loadAllFiles().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<List<ImageFile>, Flowable<List<String>>>() {
                    @Override
                    public Flowable<List<String>> apply(List<ImageFile> imageFiles) throws Exception {
                        List<String> data = new ArrayList<>();
                        for (ImageFile imageFile : imageFiles) {
                            data.add(imageFile.getFilePath());
                        }
                        return Flowable.just(data);
                    }
                })
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> strings) throws Exception {
                        setImageList(strings);
                    }
                });
    }

    public List<String> getImageList() {
        return new ArrayList<>(mImageList);
    }

    private void setImageList(List<String> imageList) {
        mImageList.clear();
        mImageList.addAll(imageList);
    }
}
