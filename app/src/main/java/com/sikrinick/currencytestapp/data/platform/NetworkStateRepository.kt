package com.sikrinick.currencytestapp.data.platform

class NetworkStateRepository(
    private val networkInfoProvider: NetworkInfoProvider
) {

    fun observeNetworkConnected() = networkInfoProvider.observeNetworkConnected()

}