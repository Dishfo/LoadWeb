package com.example.webcache.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

public final class NetWorkUtils {
    public static boolean isConnected(Context context){
        ConnectivityManager networkStatsManager=
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        Network network=networkStatsManager.getActiveNetwork();
        if(network==null){
            return false;
        }
        NetworkInfo info=networkStatsManager.getNetworkInfo(network);
        return info.isAvailable();


    }

}
