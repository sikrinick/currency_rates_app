package com.sikrinick.currencytestapp.domain.usecase.currency

import com.sikrinick.currencytestapp.domain.schedulers.AppSchedulers
import com.sikrinick.currencytestapp.presentation.model.toCurrencyAmount
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.rxkotlin.withLatestFrom
import io.reactivex.subjects.BehaviorSubject
import java.util.*

class ObserveCurrencyExchangeUseCase(
    private val observeCurrencyRatesUseCase: ObserveCurrencyRatesUseCase,
    private val calculateAmountUseCase: CalculateAmountUseCase,
    private val schedulers: AppSchedulers
) {

    private val currencyCodeSubject = BehaviorSubject.create<String>()
    private val amountSubject = BehaviorSubject.create<String>()

    fun setCurrency(currencyCode: String) {
        currencyCodeSubject.onNext(currencyCode)
    }

    fun setAmount(amount: String) {
        amountSubject.onNext(amount)
    }

    fun execute() = currencyCodeSubject
        .toFlowable(BackpressureStrategy.LATEST)
        .observeOn(schedulers.io)
        .switchMap { observeCurrencyRatesUseCase.execute(currency = Currency.getInstance(it)) }
        .observeOn(schedulers.computation)
        .withLatestFrom(amountSubject.toFlowable(BackpressureStrategy.LATEST))
        .switchMapSingle { (info, amount) ->
            Flowable.fromIterable(info.rates)
                .parallel()
                .map { rate ->
                    rate.currency to calculateAmountUseCase.execute(
                        baseAmount = amount,
                        rate = rate.ratePerOne
                    )
                }
                .map { (currency, amount) -> currency.toCurrencyAmount(amount) }
                .sequential()
                .toList()

        }

}