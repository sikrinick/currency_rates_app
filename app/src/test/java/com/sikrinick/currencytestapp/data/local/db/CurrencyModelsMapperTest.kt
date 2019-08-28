package com.sikrinick.currencytestapp.data.local.db

import com.sikrinick.currencytestapp.data.gson.gson
import com.sikrinick.currencytestapp.data.local.db.model.LocalDbCurrencyRates
import com.sikrinick.currencytestapp.shared.model.CurrencyInfo
import com.sikrinick.currencytestapp.shared.model.CurrencyRate
import org.junit.Before
import org.junit.Test
import java.util.*

class CurrencyModelsMapperTest {

    private lateinit var currencyModelsMapper: CurrencyModelsMapper

    private val testInfo = CurrencyInfo(
        currency = Currency.getInstance("EUR"),
        rates = listOf(
            CurrencyRate(Currency.getInstance("PLN"), "4.20"),
            CurrencyRate(Currency.getInstance("SEK"), "13.37")
        )
    )
    private val testDbModel = LocalDbCurrencyRates(
        currencyCode = "EUR",
        rates = """{"PLN":"4.20","SEK":"13.37"}""".trim()
    )

    @Before
    fun before() {
        currencyModelsMapper = CurrencyModelsMapper(gson)
    }

    @Test
    fun toLocalDbCurrencyRates() {
        //WHEN
        val dbModel = currencyModelsMapper.toLocalDbCurrencyRates(testInfo)

        //THEN
        assert(dbModel == testDbModel)
    }

    @Test
    fun toCurrencyInfo() {
        //WHEN
        val info = currencyModelsMapper.toCurrencyInfo(testDbModel)

        //THEN
        assert(info == testInfo)
    }
}