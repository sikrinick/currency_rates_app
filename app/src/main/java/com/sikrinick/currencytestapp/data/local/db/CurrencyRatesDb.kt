package com.sikrinick.currencytestapp.data.local.db

import com.sikrinick.currencytestapp.shared.model.CurrencyInfo
import java.util.*

class CurrencyRatesDb(
    private val currencyRatesDao: CurrencyRatesDao,
    private val currencyModelsMapper: CurrencyModelsMapper
) {

    fun updateRatesFor(currencyInfo: CurrencyInfo) =
        currencyRatesDao.update(
            currencyModelsMapper.toLocalDbCurrencyRates(currencyInfo)
        )

    fun getRatesFor(currency: Currency) =
        currencyRatesDao.get(currencyCode = currency.currencyCode)
            .map {
                currencyModelsMapper.toCurrencyInfo(it)
            }

}