package com.example.mealz.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;

public class NetworkManager {
    private final ConnectivityManager cm;
    private final ConnectivityManager.NetworkCallback callback;
    private final NetworkListener listener;
    public NetworkManager(Context context, NetworkListener listener) {
        this.listener = listener;
        cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        callback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                if (listener != null) {
                    listener.onNetworkAvailable();
                }
            }

            @Override
            public void onLost(Network network) {
                if (listener != null) {
                    listener.onNetworkLost();
                }
            }
        };
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            Network network = cm.getActiveNetwork();
            if (network != null) {
                NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);
                return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
            }
        }
        return false;
    }

    public void registerNetworkCallback() {
        if (cm != null) {
            NetworkRequest networkRequest = new NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .build();
            cm.registerNetworkCallback(networkRequest, callback);
        }
    }

    public void unregisterNetworkCallback() {
        if (cm != null) {
            cm.unregisterNetworkCallback(callback);
        }
    }

    public interface NetworkListener {
        void onNetworkAvailable();

        void onNetworkLost();
    }
}
