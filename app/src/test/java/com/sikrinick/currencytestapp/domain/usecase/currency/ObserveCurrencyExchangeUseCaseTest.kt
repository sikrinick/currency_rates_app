package com.sikrinick.currencytestapp.domain.usecase.currency

import com.sikrinick.currencytestapp.domain.schedulers.AppSchedulers
import com.sikrinick.currencytestapp.presentation.model.CurrencyAmount
import com.sikrinick.currencytestapp.shared.model.CurrencyInfo
import com.sikrinick.currencytestapp.shared.model.CurrencyRate
import io.mockk.every
import io.mockk.mockk
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import java.util.*

class ObserveCurrencyExchangeUseCaseTest {

    private lateinit var calculateAmountUseCase: CalculateAmountUseCase
    private lateinit var observeCurrencyRatesUseCase: ObserveCurrencyRatesUseCase
    private lateinit var schedulers: AppSchedulers

    private lateinit var observeCurrencyExchangeUseCase: ObserveCurrencyExchangeUseCase

    private val currencyAmount = CurrencyAmount("Euro", "EUR", "1.00")

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

        every { calculateAmountUseCase.execute(any(), any()) } returns "535.45"

        every { observeCurrencyRatesUseCase.execute(any()) } returns Flowable.create({
            it.onNext(
                CurrencyInfo(
                    Currency.getInstance(currencyAmount.currencyCode),
                    rates = listOf(
                        CurrencyRate(Currency.getInstance("PLN"), "3.90"),
                        CurrencyRate(Currency.getInstance("USD"), "4.40")
                    )
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