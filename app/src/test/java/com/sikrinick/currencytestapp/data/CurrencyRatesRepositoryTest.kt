package com.sikrinick.currencytestapp.data

import com.sikrinick.currencytestapp.data.local.db.CurrencyRatesDb
import com.sikrinick.currencytestapp.data.remote.api.CurrencyApi
import com.sikrinick.currencytestapp.data.remote.model.GetCurrencyRatesResponse
import com.sikrinick.currencytestapp.shared.model.CurrencyInfo
import com.sikrinick.currencytestapp.shared.model.CurrencyRate
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import java.util.*

class CurrencyRatesRepositoryTest {

    private lateinit var currencyRatesDb: CurrencyRatesDb
    private lateinit var currencyApiService: CurrencyApi
    private lateinit var currencyRatesRepository: CurrencyRatesRepository

    @Before
    fun before() {
        currencyRatesDb = mockk()
        currencyApiService = mockk()
        currencyRatesRepository = CurrencyRatesRepository(
            currencyRatesDb = currencyRatesDb,
            currencyApiService = currencyApiService
        )
    }

    @Test
    fun `get from api and save to db`() {
        val currency = Currency.getInstance("EUR")
        every { currencyApiService.getRatesFor(currency) } returns Single.just(
            GetCurrencyRatesResponse(
                currency,
                Date(),
                listOf(
                    CurrencyRate(Currency.getInstance("PLN"), "3.58"),
                    CurrencyRate(Currency.getInstance("USD"), "1.18")
                )
            )
        )

        every { currencyRatesDb.updateRatesFor(any()) } returns Completable.complete()

        currencyRatesRepository.getRatesFor(currency)
            .test()
            .assertValue(
                CurrencyInfo(
                    currency,
                    listOf(
                        CurrencyRate(Currency.getInstance("PLN"), "3.58"),
                        CurrencyRate(Currency.getInstance("USD"), "1.18"),
                        CurrencyRate(Currency.getInstance("EUR"), "1.00")
                    )
                )
            )
            .assertComplete()
            .dispose()
    }

    @Test
    fun `on any api error get from db`() {
        val currency = Currency.getInstance("EUR")

        every { currencyApiService.getRatesFor(any()) } returns Single.error(RuntimeException("Any network error"))

        every { currencyRatesDb.getRatesFor(any()) } returns Single.just(
            CurrencyInfo(
                currency,
                listOf(
                    CurrencyRate(Currency.getInstance("PLN"), "3.58"),
                    CurrencyRate(Currency.getInstance("USD"), "1.18")
                )
            )
        )


        currencyRatesRepository.getRatesFor(Currency.getInstance("EUR"))
            .test()
            .assertValue(
                CurrencyInfo(
                    currency,
                    listOf(
                        CurrencyRate(Currency.getInstance("PLN"), "3.58"),
                        CurrencyRate(Currency.getInstance("USD"), "1.18"),
                        CurrencyRate(Currency.getInstance("EUR"), "1.00")
                    )
                )
            )
            .assertComplete()
            .dispose()
    }
}