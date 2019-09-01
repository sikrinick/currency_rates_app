package com.sikrinick.currencytestapp.domain.usecase.currency

import com.sikrinick.currencytestapp.data.CurrencyRatesRepository
import com.sikrinick.currencytestapp.data.platform.NetworkStateRepository
import com.sikrinick.currencytestapp.domain.schedulers.AppSchedulers
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class ObserveCurrencyRatesUseCase(
    private val currencyRatesRepository: CurrencyRatesRepository,
    private val networkStateRepository: NetworkStateRepository,
    private val schedulers: AppSchedulers
) {

    fun execute() = networkStateRepository
        .observeNetworkConnected()
        .switchMap { connected ->
            if (connected) {
                Observable.interval(0, 1, TimeUnit.SECONDS, schedulers.computation)
            } else {
                Observable.just(1)
            }
        }
        .switchMapSingle {
            currencyRatesRepository.getRates()
        }
        .toFlowable(BackpressureStrategy.LATEST)
        .subscribeOn(schedulers.io)

}