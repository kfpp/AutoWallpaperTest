package com.ye.example.autowallpapper.presenters;

import com.ye.example.autowallpapper.presenters.base.BaseShowPresenter;
import com.ye.example.autowallpapper.utils.GlobalData;

import java.util.List;
import java.util.Random;

/**
 * @author yezhihao 2019-08-05 15:04
 */
public class RandomNextPaperPresenter extends BaseShowPresenter {

    private String randomPath(List<String> images) {
        Random random = new Random(System.currentTimeMillis());
        int randomIndex = random.nextInt(images.size());
        return images.get(randomIndex);
    }

    @Override
    protected List<String> getDataList() {
        return GlobalData.getInstance().getImageList();
    }

    @Override
    protected String getNextShowImgPath() {
        List<String> images = getDataList();
        if (images == null || images.size() == 0) {
            return null;
        }

        return randomPath(images);
    }

}
