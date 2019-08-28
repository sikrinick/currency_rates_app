package com.sikrinick.currencytestapp.data

import com.sikrinick.currencytestapp.data.platform.NetworkInfoProvider
import com.sikrinick.currencytestapp.data.platform.NetworkStateRepository
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class NetworkStateRepositoryTest {

    private lateinit var provider: NetworkInfoProvider
    private lateinit var networkStateRepository: NetworkStateRepository


    @Before
    fun before() {
        provider = mockk(relaxed = true)
        networkStateRepository = NetworkStateRepository(provider)
    }

    @Test
    fun `test should pass`() {
        every { provider.observeNetworkConnected() } returns Observable.just(true)

        networkStateRepository.observeNetworkConnected()
            .test()
            .assertSubscribed()
            .assertResult(true)
            .assertComplete()

    }

    @Test(expected = AssertionError::class) // remove expected for exception
    fun `test should pass with exception`() {
        every { provider.observeNetworkConnected() } returns Observable.just(false)

        networkStateRepository.observeNetworkConnected()
            .test()
            .assertSubscribed()
            .assertResult(true)
            .assertComplete()

    }
}