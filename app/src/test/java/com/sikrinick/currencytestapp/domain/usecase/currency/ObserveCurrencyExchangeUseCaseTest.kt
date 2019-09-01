package com.sikrinick.currencytestapp.domain.usecase.currency

import com.sikrinick.currencytestapp.domain.schedulers.AppSchedulers
import com.sikrinick.currencytestapp.shared.model.CurrencyRate
import io.mockk.every
import io.mockk.mockk
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test

class ObserveCurrencyExchangeUseCaseTest {

    private lateinit var calculateAmountUseCase: CalculateAmountUseCase
    private lateinit var observeCurrencyRatesUseCase: ObserveCurrencyRatesUseCase
    private lateinit var schedulers: AppSchedulers

    private lateinit var observeCurrencyExchangeUseCase: ObserveCurrencyExchangeUseCase

    @Before
    fun before() {
        schedulers = AppSchedulers(
            computation = Schedulers.trampoline(),
            io = Schedulers.trampoline(),
            ui = Schedulers.trampoline()
        )
        calculateAmountUseCase = mockk()
        observeCurrencyRatesUseCase = mockk()

        observeCurrencyExchangeUseCase = ObserveCurrencyExchangeUseCase(
            schedulers = schedulers,
            calculateAmountUseCase = calculateAmountUseCase,
            observeCurrencyRatesUseCase = observeCurrencyRatesUseCase
        )
    }

    @Test
    fun execute() {

        every { calculateAmountUseCase.execute(any(), any(), any()) } returns "535.45"

        every { observeCurrencyRatesUseCase.execute() } returns Flowable.create({
            it.onNext(
                listOf(
                    CurrencyRate("PLN", "3.90"),
                    CurrencyRate("USD", "4.40"),
                    CurrencyRate("EUR", "1.00")
                )
            )
        }, BackpressureStrategy.LATEST)

        observeCurrencyExchangeUseCase.setAmount("1.00")
        observeCurrencyExchangeUseCase.setCurrency("EUR")
        observeCurrencyExchangeUseCase.execute()
            .subscribeOn(Schedulers.trampoline())
            .test()
            .assertValue {
                it[0].run {
                    currencyCode == "PLN" && amount == "535.45"
                }
            }
            .assertValue {
                it[1].run {
                    currencyCode == "USD" && amount == "535.45"
                }
            }
            .assertNotComplete()
            .dispose()
    }
}