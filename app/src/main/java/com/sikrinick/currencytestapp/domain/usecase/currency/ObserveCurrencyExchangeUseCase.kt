package com.sikrinick.currencytestapp.domain.usecase.currency

import com.sikrinick.currencytestapp.domain.schedulers.AppSchedulers
import com.sikrinick.currencytestapp.presentation.model.CurrencyAmount
import com.sikrinick.currencytestapp.presentation.model.toCurrencyAmount
import io.reactivex.Flowable
import java.util.*

class ObserveCurrencyExchangeUseCase(
    private val observeCurrencyRatesUseCase: ObserveCurrencyRatesUseCase,
    private val calculateAmountUseCase: CalculateAmountUseCase,
    private val schedulers: AppSchedulers
) {

    fun execute(currencyAmount: CurrencyAmount): Flowable<List<CurrencyAmount>> = observeCurrencyRatesUseCase
        .execute(currency = Currency.getInstance(currencyAmount.currencyCode))
        .observeOn(schedulers.computation)
        .switchMapSingle {  info ->
            Flowable.fromIterable(info.rates)
                .parallel()
                .map { rate ->
                    rate.currency to calculateAmountUseCase.execute(
                        baseAmount = currencyAmount.amount,
                        rate = rate.ratePerOne
                    )
                }
                .map { (currency, amount) -> currency.toCurrencyAmount(amount) }
                .sequential()
                .toList()
        }
}