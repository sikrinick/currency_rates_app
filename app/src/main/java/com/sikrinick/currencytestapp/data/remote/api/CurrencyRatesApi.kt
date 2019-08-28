package com.sikrinick.currencytestapp.data.remote.api

import com.sikrinick.currencytestapp.data.remote.model.GetCurrencyRatesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyRatesApi {

    //https://currency.duckdns.org/latest?base=EUR
    @GET("latest")
    fun getRatesFor(
        @Query("base") currencyCode: String
    ): Single<GetCurrencyRatesResponse>

    companion object {
        const val BASE_URL = "https://revolut.duckdns.org/"
    }
}