package com.chinatsp.dvrwlantransfer.file;

import android.os.Environment;
import android.text.TextUtils;

import com.chinatsp.dvrwlantransfer.utils.Constants;
import com.chinatsp.dvrwlantransfer.utils.IpAddressUtils;
import com.chinatsp.dvrwlantransfer.utils.Logcat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;

/**
 * @author chenzuohua
 * Created at 2020/5/22 17:23
 */
public class DvrFileUtils {
    //private static final String DVR_SDCARD_PATH = "/storage/udisk_dvr";
    private static final String DVR_SDCARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/udisk_dvr";
    public static final String DVR_PHOTO_FOLDER = DVR_SDCARD_PATH + "/image";
    public static final String DVR_LOOP_VIDEO_FOLDER = DVR_SDCARD_PATH + "/video/cycle";
    public static final String DVR_EMERGENCY_VIDEO_FOLDER = DVR_SDCARD_PATH + "/video/event";

    public static final String PHOTO_URL_MID = "/dvr/image";// 图片在线路径中间部分
    public static final String LOOP_VIDEO_MID = "/dvr/video/cycle";// 循环视频在线路径中间部分
    public static final String EMERGENCY_VIDEO_MID = "/dvr/video/event";// 紧急视频在线路径中间部分

    private static SimpleDateFormat mSimpleDateFormat;

    public static boolean isPicture(@NonNull String fileName) {
        return fileName.endsWith(".png") || fileName.endsWith(".jpg")
                || fileName.endsWith(".PNG") || fileName.endsWith(".jpeg");// 最常见的放前面，提高效率
    }

    public static boolean isVideo(@NonNull String fileName) {
        return fileName.endsWith(".mp4") || fileName.endsWith(".3gp");
    }

    /**
     * 获取文件列表（分组）
     * @param fileType 文件类型
     * @param index    开始位置
     * @param size     数量
     * @return
     */
    public static LinkedHashMap<String, List<FileBean>> getFileListByGroup(int fileType, int index, int size) {
        List<FileBean> list = getFileList(fileType, index, size);
        if (list != null && !list.isEmpty()) {
            // 按日期分组
            return list.stream().collect(Collectors.groupingBy(FileBean::getCreateDate, LinkedHashMap::new,
                    Collectors.toList()));
        }
        return null;
    }

    /**
     * 获取文件列表
     * @param fileType 文件类型
     * @param index    开始位置
     * @param size     数量
     * @return
     */
    public static List<FileBean> getFileList(final int fileType, int index, int size) {
        Logcat.i("fileType = " + fileType + ", index = " + index + ", size = " + size);
        File folder;
        switch (fileType) {
            case Constants.FILE_TYPE_PHOTO:
                folder = new File(DVR_PHOTO_FOLDER);
                break;
            case Constants.FILE_TYPE_LOOP_VIDEO:
                folder = new File(DVR_LOOP_VIDEO_FOLDER);
                break;
            case Constants.FILE_TYPE_EMERGENCY_VIDEO:
                folder = new File(DVR_EMERGENCY_VIDEO_FOLDER);
                break;
            default:
                Logcat.e("fileType invalid, return null");
                return null;
        }
        if (folder.exists()) {
            if (folder.isDirectory()) {
                // 过滤文件
                File[] files = folder.listFiles((dir, name) -> {// name为当前扫描到的文件名，dir为这个文件的父目录
                    File file = new File(dir, name);
                    if (!file.exists()) {
                        Logcat.e(file.getAbsolutePath() + " not exists, drop it");
                        return false;
                    }
                    if (file.isDirectory()) {
                        Logcat.e(file.getAbsolutePath() + " is a folder, drop it");
                        return false;
                    }
                    if (file.isHidden()) {
                        Logcat.e(file.getAbsolutePath() + " is hidden, drop it");
                        return false;
                    }
                    switch (fileType) {
                        case Constants.FILE_TYPE_PHOTO:
                            return isPicture(file.getName());
                        case Constants.FILE_TYPE_LOOP_VIDEO:
                        case Constants.FILE_TYPE_EMERGENCY_VIDEO:
                            return isVideo(file.getName());
                    }
                    return false;
                });
                if (files != null && files.length > 0) {
                    if (index < files.length) {
                        // 对列表进行排序
                        List<File> sortedList = Arrays.stream(files).sorted(Comparator.comparing(File::lastModified))
                                .collect(Collectors.toList());

                        List<FileBean> list = new ArrayList<>();
                        String ip = IpAddressUtils.getWlanApIpAddress();
                        FileBean bean;
                        int length = sortedList.size();
                        for (int i = index, j = 0; i < length && j < size; i++, j++) {
                            bean = buildFileBean(fileType, sortedList.get(i), ip, Constants.HTTP_PORT);
                            list.add(bean);
                        }
                        Logcat.i("return list.size = " + list.size());
                        return list;
                    } else {
                        Logcat.e("index out of range, return null");
                    }
                } else {
                    Logcat.e(folder.getAbsolutePath() + " is empty, return null");
                }
            } else {
                Logcat.e(folder.getAbsolutePath() + " is not a folder, return null");
            }
        } else {
            Logcat.e(folder.getAbsolutePath() + " is not exists, return null");
        }
        return null;
    }

    private static FileBean buildFileBean(int fileType, File file, String ip, int port) {
        if (mSimpleDateFormat == null) {
            mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        }
        String fileUrl = null;
        switch (fileType) {
            case Constants.FILE_TYPE_PHOTO:
                fileUrl = "http://" + ip + ":" + port + PHOTO_URL_MID + File.separator + file.getName();
                break;
            case Constants.FILE_TYPE_LOOP_VIDEO:
                fileUrl = "http://" + ip + ":" + port + LOOP_VIDEO_MID + File.separator + file.getName();
                break;
            case Constants.FILE_TYPE_EMERGENCY_VIDEO:
                fileUrl = "http://" + ip + ":" + port + EMERGENCY_VIDEO_MID + File.separator + file.getName();
                break;
        }
        if (fileUrl == null) {
            return null;
        } else {
            return new FileBean(file.getName(), file.getName(), file.length(), "", fileUrl,
                    fileType, file.lastModified(), mSimpleDateFormat.format(new Date(file.lastModified())));
        }
    }

    /**
     * 删除文件
     * @param fileType 文件类型
     * @param ids      文件id数组
     * @return
     */
    public static boolean deleteFiles(int fileType, @NonNull String[] ids) {
        File folder = null;
        switch (fileType) {
            case Constants.FILE_TYPE_PHOTO:
                folder = new File(DVR_PHOTO_FOLDER);
                break;
            case Constants.FILE_TYPE_LOOP_VIDEO:
                folder = new File(DVR_LOOP_VIDEO_FOLDER);
                break;
            case Constants.FILE_TYPE_EMERGENCY_VIDEO:
                folder = new File(DVR_EMERGENCY_VIDEO_FOLDER);
                break;
        }
        boolean result = false;
        if (folder != null && folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null && files.length > 0) {
                for (File file : files) {
                    for (String id : ids) {
                        if (!TextUtils.isEmpty(id) && id.equals(file.getName())) {
                            Logcat.i("delete " + file.getAbsolutePath());
                            file.delete();
                            result = true;
                        }
                    }
                }
            }
        }
        Logcat.i("result = " + result);
        return result;
    }

    /**
     * 保存循环视频到紧急视频
     * @param id 视频文件id
     * @return
     */
    public static boolean saveFileToEmergency(@NonNull String id) {
        File folder = new File(DVR_LOOP_VIDEO_FOLDER);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null && files.length > 0) {
                File matchFile = null;
                for (File file : files) {
                    if (id.equals(file.getName())) {
                        matchFile = file;
                        Logcat.i("find matched file:" + file.getAbsolutePath());
                        break;
                    }
                }
                if (matchFile != null) {
                    return moveFile(matchFile.getAbsolutePath(),
                            DVR_EMERGENCY_VIDEO_FOLDER + File.separator + matchFile.getName());
                }
            }
        }
        return false;
    }

    private static boolean moveFile(String sourcePath, String targetPath) {
        if (!TextUtils.isEmpty(sourcePath) && !TextUtils.isEmpty(targetPath)) {
            try {
                Files.move(Paths.get(sourcePath), Paths.get(targetPath), StandardCopyOption.REPLACE_EXISTING);
                Logcat.i("move " + sourcePath + " success!");
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Logcat.e("move "+ sourcePath +" failed!");
        return false;
    }
}
