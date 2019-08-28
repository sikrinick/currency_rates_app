package com.sikrinick.currencytestapp.data.remote.api

import java.util.*

class CurrencyApi(
    private val currencyRatesApi: CurrencyRatesApi
) {

    fun getRatesFor(currency: Currency) = currencyRatesApi
        .getRatesFor(currency.currencyCode)
}