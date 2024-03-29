package com.sikrinick.currencytestapp.data.adapters

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.sikrinick.currencytestapp.shared.model.CurrencyRate

class CurrencyRateListTypeAdapter: TypeAdapter<List<CurrencyRate>>() {

    override fun write(output: JsonWriter?, value: List<CurrencyRate>?) {
        output?.apply {
            beginObject()
            value?.forEach {
                name(it.currencyCode)
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
                        nextName(),
                        nextString()
                    )
                )
            }
            endObject()
        }
    }

}