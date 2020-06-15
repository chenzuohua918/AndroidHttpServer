package com.chinatsp.dvrwlantransfer.utils;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @author chenzuohua
 * Created at 2020/5/26 10:30
 */
public class IpAddressUtils {

    /**
     * 获取本机热点IP地址
     * @return
     */
    public static String getWlanApIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface ni = en.nextElement();
                if (ni.getName().contains("wlan")) {
                    for (Enumeration<InetAddress> enAddr = ni.getInetAddresses(); enAddr.hasMoreElements(); ) {
                        InetAddress ia = enAddr.nextElement();
                        if (!ia.isLoopbackAddress() && ia.getAddress().length == 4) {
                            Logcat.i("IP = " + ia.getHostAddress());
                            return ia.getHostAddress();
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "192.168.43.1";// 一般Android设备默认的路由地址
    }

    /**
     * 获取本机连接的热点路由IP地址
     * @param context
     * @return
     */
    public static String getWlanRouteIpAddress(Context context) {
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcpInfo = wm.getDhcpInfo();
        String routeIp = Formatter.formatIpAddress(dhcpInfo.gateway);
        Logcat.i("route ip", "wifi route ip：" + routeIp);
        return routeIp;
    }
}
