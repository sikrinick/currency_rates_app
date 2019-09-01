package com.sikrinick.currencytestapp.domain.usecase.currency

import com.sikrinick.currencytestapp.domain.schedulers.AppSchedulers
import com.sikrinick.currencytestapp.presentation.model.CurrencyAmount
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.rxkotlin.Flowables
import io.reactivex.subjects.BehaviorSubject

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

    fun execute() = Flowables
        .combineLatest(
            observeCurrencyRatesUseCase.execute(),
            currencyCodeSubject.toFlowable(BackpressureStrategy.LATEST),
            amountSubject.toFlowable(BackpressureStrategy.LATEST)
        )
        .observeOn(schedulers.computation)
        .map { (rates, baseCurrencyCode, baseCurrencyAmount) ->
            val baseCurrencyRate = rates.first { it.currencyCode == baseCurrencyCode }.ratePerOne
            Triple(rates, baseCurrencyRate, baseCurrencyAmount)
        }
        .switchMapSingle { (rates, baseCurrencyRate, baseCurrencyAmount) ->
            Flowable.fromIterable(rates)
                .parallel()
                .map { (currencyCode, ratePerOne) ->
                    currencyCode to calculateAmountUseCase.execute(
                        baseCurrencyAmount = baseCurrencyAmount,
                        baseCurrencyRate = baseCurrencyRate,
                        targetCurrencyRate = ratePerOne
                    )
                }
                .map { (currencyCode, amount) -> CurrencyAmount.from(currencyCode, amount) }
                .sequential()
                .toList()
        }

}