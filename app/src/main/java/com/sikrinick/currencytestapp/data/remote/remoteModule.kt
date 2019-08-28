package com.sikrinick.currencytestapp.data.remote

import com.sikrinick.currencytestapp.BuildConfig
import com.sikrinick.currencytestapp.data.remote.api.CurrencyApi
import com.sikrinick.currencytestapp.data.remote.api.CurrencyRatesApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val remoteModule = module {

    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG)
                    HttpLoggingInterceptor.Level.BODY
                else
                    HttpLoggingInterceptor.Level.NONE
            })
            .build()
    }

    single {
        Retrofit.Builder()
            .client(get())
            .baseUrl(CurrencyRatesApi.BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
            .create(CurrencyRatesApi::class.java)
    }

    single {
        CurrencyApi(get())
    }

}