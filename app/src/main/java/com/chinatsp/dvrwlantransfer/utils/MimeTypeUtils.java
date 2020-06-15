package com.chinatsp.dvrwlantransfer.utils;

import android.text.TextUtils;

/**
 * @author chenzuohua
 * Created at 2020/5/26 14:06
 */
public class MimeTypeUtils {

    /**
     * 根据后缀获取对应的mimeType
     * @param suffix 后缀
     * @return
     */
    public static String getMimeType(String suffix) {
        if (!TextUtils.isEmpty(suffix)) {
            switch (suffix.toLowerCase()) {
                case ".png":
                    return "image/png";
                case ".jpg":
                case ".jpeg":
                    return "image/jpeg";
                case ".3gp":
                    return "video/3gpp";
                case ".mp4":
                case ".mpg4":
                    return "video/mp4";
            }
        }
        return "application/json";
    }
}
