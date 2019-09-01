package com.sikrinick.currencytestapp.data.local.db

import com.sikrinick.currencytestapp.data.local.db.model.LocalDbCurrencyRate
import com.sikrinick.currencytestapp.shared.model.CurrencyRate

class CurrencyRatesDb(
    private val currencyRatesDao: CurrencyRatesDao
) {

    fun updateRates(currencyRates: List<CurrencyRate>) =
        currencyRatesDao.updateAll(
            currencyRates.map {
                LocalDbCurrencyRate(
                    currencyCode = it.currencyCode,
                    rate = it.ratePerOne
                )
            }
        )

    fun getRates() =
        currencyRatesDao.getAll()
            .map {  list ->
                list.map {
                    CurrencyRate(
                        it.currencyCode,
                        it.rate
                    )
                }
            }

}