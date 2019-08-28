package com.sikrinick.currencytestapp.data.platform

import android.content.Context
import android.net.NetworkInfo
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import io.reactivex.Observable

//Could be done via broadcast receiver, but this is faster
class NetworkInfoProvider(
    private val context: Context
) {

    // deprecated, but it's faster to implement
    fun observeNetworkConnected(): Observable<Boolean> = ReactiveNetwork
        .observeNetworkConnectivity(context)
        .map { it.state() }
        .filter { it == NetworkInfo.State.CONNECTED || it == NetworkInfo.State.DISCONNECTED }
        .map { it == NetworkInfo.State.CONNECTED }

}