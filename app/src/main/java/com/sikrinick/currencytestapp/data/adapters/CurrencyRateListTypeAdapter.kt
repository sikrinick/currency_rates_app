package com.sikrinick.currencytestapp.data.adapters

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.sikrinick.currencytestapp.shared.model.CurrencyRate
import java.util.*
import kotlin.collections.ArrayList

class CurrencyRateListTypeAdapter: TypeAdapter<List<CurrencyRate>>() {

    override fun write(output: JsonWriter?, value: List<CurrencyRate>?) {
        output?.apply {
            beginObject()
            value?.forEach {
                name(it.currency.currencyCode)
                value(it.ratePerOne)
            }
            endObject()
        }
    }

    override fun read(input: JsonReader?): List<CurrencyRate> = ArrayList<CurrencyRate>().also {
        input?.run {
            beginObject()
            while(hasNext()) {
                it.add(
                    CurrencyRate(
                        Currency.getInstance(nextName()),
                        nextString()
                    )
                )
            }
            endObject()
        }
    }

}