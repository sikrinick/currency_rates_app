package com.sikrinick.currencytestapp.data.gson

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.sikrinick.currencytestapp.data.adapters.CurrencyRateListTypeAdapter
import com.sikrinick.currencytestapp.shared.model.CurrencyRate

val gson: Gson = GsonBuilder()
    .registerTypeAdapter(
        TypeToken
            .getParameterized(
                List::class.java,
                CurrencyRate::class.java
            )
            .type,
        CurrencyRateListTypeAdapter()
    )
    .create()