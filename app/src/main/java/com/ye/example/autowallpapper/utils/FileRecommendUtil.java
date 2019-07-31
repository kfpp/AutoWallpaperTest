package com.ye.example.autowallpapper.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yezhihao
 * @date 2019-07-01 14:32
 */
public class FileRecommendUtil {
    public static final String PNG = ".png";
    public static final String JPG = ".jpg";

    public static List<String> getRecommendFiles(String pathFile) {
        File fileSource = new File(pathFile);
        if (!fileSource.exists()) {
            return null;
        }

        File[] images = fileSource.getParentFile().listFiles(new ImageNameFileter());
        if (images == null || images.length == 0) {
            return null;
        }
        List<String> resultList = new ArrayList<>();
        for (File image : images) {
            String filePath = image.getAbsolutePath();
            if (!filePath.equals(pathFile)) {
                resultList.add(filePath);
            }
        }
        return resultList;
    }

    private static final class ImageNameFileter implements FilenameFilter {

        @Override
        public boolean accept(File dir, String name) {
            if (name.endsWith(PNG) || name.endsWith(JPG)) {
                return true;
            }
            return false;
        }
    }

}
