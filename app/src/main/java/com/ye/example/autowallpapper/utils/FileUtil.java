package com.ye.example.autowallpapper.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yezhihao
 * @date 2019-07-08 16:01
 */
public class FileUtil {
    public static final String DEFAULT_IMAGE_DIRECTORY_PATH = "/storage/505C-D21C/Pictures/壁纸";

    public static int getSubFileCount(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists() || !dir.isDirectory() || !dir.canRead()) {
            return -1;
        }

        return dir.list().length;
    }

    public static List<String> imagePathList(String parentDir) {
        File dir = new File(parentDir);
        if (!dir.exists() || !dir.isDirectory() || !dir.canRead()) {
            throw new IllegalStateException("路径异常或无权限");
        }

        File[] childFiles = dir.listFiles(new ImageFileter());
        List<String> pathList = new ArrayList<>();
        for (File file : childFiles) {
            pathList.add(file.getAbsolutePath());
        }
        return pathList;
    }

    private static final class ImageFileter implements FilenameFilter {
        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(".png") || name.endsWith(".jpg");
        }

    }

}
