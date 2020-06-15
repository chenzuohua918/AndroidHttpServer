package com.chinatsp.dvrwlantransfer;

import android.app.Application;

/**
 * @author chenzuohua
 * Created at 2020/5/22 14:32
 */
public class MyApp extends Application {
    private static MyApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static MyApp getInstance() {
        return instance;
    }
}
