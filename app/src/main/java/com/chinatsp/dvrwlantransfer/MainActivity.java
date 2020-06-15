package com.chinatsp.dvrwlantransfer;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chinatsp.dvrwlantransfer.service.MainService;
import com.chinatsp.dvrwlantransfer.utils.IpAddressUtils;
import com.chinatsp.dvrwlantransfer.utils.Logcat;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * @author chenzuohua
 * Created at 2020/5/15 10:27
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CHANGE_WIFI_STATE,
                    Manifest.permission.ACCESS_WIFI_STATE}, 1);
        }

        TextView tv_ip = findViewById(R.id.tv_ip);
        tv_ip.setText("本机IP：" + IpAddressUtils.getWlanApIpAddress()
                + "\n热点路由IP：" + IpAddressUtils.getWlanRouteIpAddress(this));
    }

    public void startHttp(View view) {
        Logcat.i();
        Intent intent = new Intent(this, MainService.class);
        intent.putExtra("startHttpServer", true);
        startService(intent);
    }

    public void stopHttp(View view) {
        Logcat.i();
        stopService(new Intent(this, MainService.class));
    }
}
