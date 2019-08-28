package com.sikrinick.currencytestapp.data.local.db

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sikrinick.currencytestapp.data.local.db.model.LocalDbCurrencyRates
import com.sikrinick.currencytestapp.shared.model.CurrencyInfo
import com.sikrinick.currencytestapp.shared.model.CurrencyRate
import java.util.*

class CurrencyModelsMapper(
    private val gson: Gson
) {

    private val type = TypeToken
        .getParameterized(List::class.java, CurrencyRate::class.java)
        .type


    fun toLocalDbCurrencyRates(it: CurrencyInfo) = LocalDbCurrencyRates(
        currencyCode = it.currency.currencyCode,
        rates = gson.toJson(it.rates, type)
    )

    fun toCurrencyInfo(it: LocalDbCurrencyRates) = CurrencyInfo(
        currency = Currency.getInstance(it.currencyCode),
        rates = gson.fromJson(it.rates, type)
    )



}