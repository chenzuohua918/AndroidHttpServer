package com.chinatsp.dvrwlantransfer.utils;

import android.text.TextUtils;

/**
 * @author chenzuohua
 * Created at 2020/5/21 14:26
 */
public class StringUtils {
    /**
     * 判断是不是正整数
     * @param str
     * @return
     */
    public static boolean isPositiveNum(String str) {
        return !TextUtils.isEmpty(str) && str.matches("^\\d+$");
    }
}
