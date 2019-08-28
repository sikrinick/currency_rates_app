package com.sikrinick.currencytestapp.domain

import com.sikrinick.currencytestapp.domain.formatter.BigDecimalFormatter
import com.sikrinick.currencytestapp.domain.schedulers.defaultSchedulers
import com.sikrinick.currencytestapp.domain.usecase.currency.CalculateAmountUseCase
import com.sikrinick.currencytestapp.domain.usecase.currency.ObserveCurrencyExchangeUseCase
import com.sikrinick.currencytestapp.domain.usecase.currency.ObserveCurrencyRatesUseCase
import org.koin.dsl.module

val domainModule = module {

    single { defaultSchedulers }

    factory { BigDecimalFormatter() }

    factory { CalculateAmountUseCase(get()) }
    factory { ObserveCurrencyRatesUseCase(get(), get(), get()) }
    factory { ObserveCurrencyExchangeUseCase(get(), get(), get()) }

}