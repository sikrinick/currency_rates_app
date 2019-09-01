package com.sikrinick.currencytestapp.data.remote.api

class CurrencyApi(
    private val currencyRatesApi: CurrencyRatesApi
) {

    fun getRates() = currencyRatesApi.getRates()
}