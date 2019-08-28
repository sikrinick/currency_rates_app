package com.sikrinick.currencytestapp.presentation.main

import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.sikrinick.currencytestapp.R
import com.sikrinick.currencytestapp.presentation.main.utils.withRecyclerView
import com.sikrinick.currencytestapp.presentation.model.CurrencyAmount
import io.mockk.every
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.stopKoin
import org.koin.dsl.module




@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val rule = ActivityTestRule(MainActivity::class.java, false, false)

    private lateinit var ratesLiveData: MutableLiveData<List<CurrencyAmount>>

    @Before
    fun before() {
        val mainViewModel = mockk<MainViewModel>(relaxUnitFun = true)
        loadKoinModules(
            module(override = true) {
                viewModel { mainViewModel } }
        )
        ratesLiveData = MutableLiveData()

        every { mainViewModel.ratesObservable } returns ratesLiveData
        every { mainViewModel.eventsObservable } returns MutableLiveData()
    }


    @Test
    fun should_show_everything_on_correct_fields() {
        rule.launchActivity(null)

        ratesLiveData.postValue(
                listOf(
                CurrencyAmount(
                    displayName = "Euro",
                    currencyCode = "EUR",
                    amount = "1.00"
                )
            )
        )

        onView(withRecyclerView(R.id.currency_list)
            .atPositionOnView(0, R.id.country_flag))
            .check(matches(withText("\uD83C\uDDEA\uD83C\uDDFA")))

        onView(withRecyclerView(R.id.currency_list)
            .atPositionOnView(0, R.id.currency_code))
            .check(matches(withText("EUR")))

        onView(withRecyclerView(R.id.currency_list)
            .atPositionOnView(0, R.id.currency_name))
            .check(matches(withText("Euro")))

        onView(withRecyclerView(R.id.currency_list)
            .atPositionOnView(0, R.id.currency_amount))
            .check(matches(withText("1.00")))
    }

    @After
    fun after() {
        stopKoin()
    }

}