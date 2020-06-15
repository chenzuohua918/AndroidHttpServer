package com.chinatsp.dvrwlantransfer.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.chinatsp.dvrwlantransfer.http.HttpServer;
import com.chinatsp.dvrwlantransfer.utils.Logcat;

/**
 * @author chenzuohua
 * Created at 2020/5/15 10:18
 */
public class MainService extends Service {
    private HttpServer mHttpServer;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logcat.d();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logcat.d();
        if (intent != null && intent.getBooleanExtra("startHttpServer", false)) {
            Logcat.i("Intent to start http server!");
            startHttpServer();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void startHttpServer() {
        if (mHttpServer == null) {
            mHttpServer = new HttpServer();
            mHttpServer.start(7788);
            Logcat.i("HttpServer start success!");
        } else {
            Logcat.e("HttpServer already started!");
        }
    }

    private void stopHttpServer() {
        if (mHttpServer != null) {
            mHttpServer.stop();
            mHttpServer = null;
            Logcat.i("HttpServer stopped!");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logcat.d();
        stopHttpServer();
    }
}
