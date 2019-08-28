package com.sikrinick.currencytestapp.data

import com.sikrinick.currencytestapp.data.local.db.CurrencyRatesDb
import com.sikrinick.currencytestapp.data.remote.api.CurrencyApi
import com.sikrinick.currencytestapp.shared.model.CurrencyInfo
import com.sikrinick.currencytestapp.shared.model.CurrencyRate
import io.reactivex.Single
import timber.log.Timber
import java.util.*


class CurrencyRatesRepository(
    private val currencyApiService: CurrencyApi,
    private val currencyRatesDb: CurrencyRatesDb
) {

    fun getRatesFor(currency: Currency): Single<CurrencyInfo> = currencyApiService.getRatesFor(currency)
        .map {
            CurrencyInfo(
                currency = it.baseCurrency,
                rates = it.rates
            )
        }
        .flatMap {
            currencyRatesDb.updateRatesFor(it)
                .toSingleDefault(it)
        }
        .onErrorResumeNext {
            Timber.e("Error on getting rates, repo will be used instead")
            currencyRatesDb.getRatesFor(currency)
        }
        .map { it.copy(rates = it.rates + CurrencyRate(currency, "1.00")) }

}