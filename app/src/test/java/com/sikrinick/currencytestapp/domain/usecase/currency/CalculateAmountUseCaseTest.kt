package com.sikrinick.currencytestapp.domain.usecase.currency

import com.sikrinick.currencytestapp.domain.formatter.BigDecimalFormatter
import io.mockk.spyk
import org.junit.Before
import org.junit.Test

class CalculateAmountUseCaseTest {

    private lateinit var formatter: BigDecimalFormatter
    private lateinit var calculateAmountUseCase: CalculateAmountUseCase

    @Before
    fun before() {
        formatter = spyk()
        calculateAmountUseCase = CalculateAmountUseCase(formatter)
    }

    @Test
    fun execute() {
        val result = calculateAmountUseCase.execute("2.00", "1.24531")
        assert(result == "2.49")
    }
}