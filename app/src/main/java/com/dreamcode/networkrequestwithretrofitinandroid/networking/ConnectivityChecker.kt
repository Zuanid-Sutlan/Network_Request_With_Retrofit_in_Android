package com.dreamcode.networkrequestwithretrofitinandroid.networking

import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class ConnectivityChecker(private val connectivityManager: ConnectivityManager?) {

    fun hasNetworkConnected(): Boolean {
        val network  = connectivityManager?.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
            || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
    }

}