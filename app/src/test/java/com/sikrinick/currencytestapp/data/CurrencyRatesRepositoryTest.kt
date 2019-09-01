package com.sikrinick.currencytestapp.data

import com.sikrinick.currencytestapp.data.local.db.CurrencyRatesDb
import com.sikrinick.currencytestapp.data.remote.api.CurrencyApi
import com.sikrinick.currencytestapp.data.remote.model.GetCurrencyRatesResponse
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
        val testInfo = listOf(
            CurrencyRate("PLN", "3.58"),
            CurrencyRate("USD", "1.18")
        )

        every { currencyApiService.getRates() } returns Single.just(
            GetCurrencyRatesResponse(
                "EUR",
                Date(),
                testInfo
            )
        )

        every { currencyRatesDb.updateRates(any()) } returns Completable.complete()
        every { currencyRatesDb.getRates() } returns Single.just(testInfo + CurrencyRate("EUR", "1.00"))

        currencyRatesRepository.getRates()
            .test()
            .assertValue(
                listOf(
                    CurrencyRate("PLN", "3.58"),
                    CurrencyRate("USD", "1.18"),
                    CurrencyRate("EUR", "1.00")
                )
            )
            .assertComplete()
            .dispose()
    }

    @Test
    fun `on any api error get from db`() {
        every { currencyApiService.getRates() } returns Single.error(RuntimeException("Any network error"))

        every { currencyRatesDb.getRates() } returns Single.just(
            listOf(
                CurrencyRate("PLN", "3.58"),
                CurrencyRate("USD", "1.18"),
                CurrencyRate("EUR", "1.00")
            )
        )


        currencyRatesRepository.getRates()
            .test()
            .assertValue(
                listOf(
                    CurrencyRate("PLN", "3.58"),
                    CurrencyRate("USD", "1.18"),
                    CurrencyRate("EUR", "1.00")
                )
            )
            .assertComplete()
            .dispose()
    }
}