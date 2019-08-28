package com.sikrinick.currencytestapp.domain.formatter

import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import java.util.*

class BigDecimalFormatterTest {

    private lateinit var bigDecimalFormatter: BigDecimalFormatter

    @Before
    fun before() {
        Locale.setDefault(Locale.ENGLISH)
        bigDecimalFormatter = BigDecimalFormatter()
    }

    @Test
    fun `format 1`() {
        val result = bigDecimalFormatter.format(BigDecimal(1))
        assert(result == "1.00") { result }
    }

    @Test
    fun `format long decimal`() {
        val result = bigDecimalFormatter.format(BigDecimal(1.884834213874))
        assert(result == "1.88") { result }
    }

    @Test
    fun `format long decimal scale up`() {
        val result = bigDecimalFormatter.format(BigDecimal(1.887834213874))
        assert(result == "1.89") { result }
    }

    @Test
    fun `big int`() {
        val result = bigDecimalFormatter.format(BigDecimal(1884834213874))
        assert(result == "1884834213874.00") { result }
    }

    @Test
    fun `big int with decimal`() {
        val result = bigDecimalFormatter.format(BigDecimal(1884834213874.48473))
        assert(result == "1884834213874.48") { result }
    }

    @Test
    fun `int underscored`() {
        val result = bigDecimalFormatter.format(BigDecimal(18_483_42_13_874.4354))
        assert(result == "184834213874.44") { result }
    }

}