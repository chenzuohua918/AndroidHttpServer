package com.chinatsp.dvrwlantransfer.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;

import com.chinatsp.dvrwlantransfer.MyApp;
import com.chinatsp.dvrwlantransfer.utils.Logcat;

/**
 * @author chenzuohua
 * Created at 2020/5/22 13:52
 */
public class TetherModel {
    private static volatile TetherModel instance;
    private ConnectivityManager mConnectivityManager;
    private WifiManager mWifiManager;

//    private ConnectivityManager.OnStartTetheringCallback mOnStartTetheringCallback =
//            new ConnectivityManager.OnStartTetheringCallback() {
//                @Override
//                public void onTetheringStarted() {
//                    Logcat.i();
//                }
//
//                @Override
//                public void onTetheringFailed() {
//                    Logcat.e();
//                }
//    };
//
//    private TetherModel() {
//        mConnectivityManager = (ConnectivityManager) MyApp.getInstance().getSystemService(
//                Context.CONNECTIVITY_SERVICE);
//    }
//
//    private static synchronized void syncInit() {
//        if (null == instance) {
//            instance = new TetherModel();
//        }
//    }
//
//    public static TetherModel getInstance() {
//        if (null == instance) {
//            syncInit();
//        }
//        return instance;
//    }
//
//    /**
//     * WiFi热点是否已打开
//     * @return
//     */
//    public boolean isWifiApEnabled() {
//        if (mWifiManager == null) {
//            mWifiManager = (WifiManager) MyApp.getInstance().getSystemService(Context.WIFI_SERVICE);
//        }
//        return mWifiManager.getWifiApState() == WifiManager.WIFI_AP_STATE_ENABLED;
//    }
//
//    /**
//     * 打开热点
//     */
//    public void startTether() {
//        if (!isWifiApEnabled()) {
//            // 每次调用会重启一次
//            mConnectivityManager.startTethering(ConnectivityManager.TETHERING_WIFI, true,
//                    mOnStartTetheringCallback, new Handler(Looper.getMainLooper()));
//        }
//    }
//
//    /**
//     * 关闭热点
//     */
//    public void stopTether() {
//        mConnectivityManager.stopTethering(ConnectivityManager.TETHERING_WIFI);
//    }
}
