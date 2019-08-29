package com.sikrinick.currencytestapp.domain.usecase.currency

import com.sikrinick.currencytestapp.data.CurrencyRatesRepository
import com.sikrinick.currencytestapp.data.platform.NetworkStateRepository
import com.sikrinick.currencytestapp.shared.model.CurrencyInfo
import com.sikrinick.currencytestapp.shared.model.CurrencyRate
import com.sikrinick.currencytestapp.testSchedulers
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*
import java.util.concurrent.TimeUnit

@RunWith(JUnit4::class)
class ObserveCurrencyRatesUseCaseTest {

    private lateinit var mockedRatesRepository: CurrencyRatesRepository
    private lateinit var mockedNetworkStateRepository: NetworkStateRepository
    private lateinit var observeCurrencyRatesUseCase: ObserveCurrencyRatesUseCase
    private lateinit var computationScheduler: TestScheduler

    @Before
    fun beforeEach() {
        mockedRatesRepository = mockk()
        mockedNetworkStateRepository = mockk()
        computationScheduler = TestScheduler()
        observeCurrencyRatesUseCase = ObserveCurrencyRatesUseCase(
            currencyRatesRepository = mockedRatesRepository,
            networkStateRepository = mockedNetworkStateRepository,
            schedulers = testSchedulers(
                computation = computationScheduler
            )
        )
    }

    @Test
    fun `returns multiple values if connected to internet`() {
        val currency = Currency.getInstance("EUR")

        val testInfo = CurrencyInfo(
            currency,
            listOf(
                CurrencyRate(Currency.getInstance("PLN"), "4.20"),
                CurrencyRate(Currency.getInstance("CZK"), "4.20")
            )
        )

        // Always return same test info
        every { mockedRatesRepository.getRatesFor(currency) } returns Single.just(testInfo)

        // Mocking network connected infinite observer
        every { mockedNetworkStateRepository.observeNetworkConnected() } returns Observable.create { it.onNext(true)  }

        // Should return at least twice and not stop
        observeCurrencyRatesUseCase.execute(currency)
            .test()
            .apply { computationScheduler.triggerActions() }
            .assertValueCount(1)
            .assertValueAt(0, testInfo)                                             // returning immediately
            .apply { computationScheduler.advanceTimeBy(1, TimeUnit.SECONDS) }  // waiting for 1 sec
            .assertValueCount(2)                                                          // returning second element
            .assertValueAt(1, testInfo)
            .assertNotComplete()
            .dispose()
    }

    @Test
    fun `returns single value if not connected to internet`() {
        val currency = Currency.getInstance("EUR")
        val testInfo = CurrencyInfo(
            currency,
            listOf(
                CurrencyRate(Currency.getInstance("PLN"), "4.20"),
                CurrencyRate(Currency.getInstance("CZK"), "4.20")
            )
        )

        // Always return same test info
        every { mockedRatesRepository.getRatesFor(currency) } returns Single.just(testInfo)

        // Mocking network connected infinite observer
        every { mockedNetworkStateRepository.observeNetworkConnected() } returns Observable.create { it.onNext(false)  }


        // Should return only one value and not stop
        observeCurrencyRatesUseCase.execute(currency)
            .test()
            .apply { computationScheduler.triggerActions() }                             // start
            .apply { computationScheduler.advanceTimeBy(1, TimeUnit.SECONDS) } // waiting for 1 sec
            .assertValueCount(1)                                                         // returns only one value
            .assertValues(testInfo)
            .assertNotComplete()
            .dispose()
    }
}