package com.sikrinick.currencytestapp.data

import com.sikrinick.currencytestapp.data.local.db.CurrencyRatesDb
import com.sikrinick.currencytestapp.data.remote.api.CurrencyApi
import com.sikrinick.currencytestapp.shared.model.CurrencyRate
import com.sikrinick.currencytestapp.shared.model.MainCurrency
import io.reactivex.Single
import timber.log.Timber


class CurrencyRatesRepository(
    private val currencyApiService: CurrencyApi,
    private val currencyRatesDb: CurrencyRatesDb
) {

    fun getRates(): Single<List<CurrencyRate>> = currencyApiService.getRates()
        .flatMapCompletable {
            currencyRatesDb.updateRates(it.rates + CurrencyRate(it.currencyCode, MainCurrency.amount))
        }
        .doOnError { Timber.e("Error on getting rates, repo will be used instead") }
        .onErrorComplete()
        .andThen(currencyRatesDb.getRates())

}