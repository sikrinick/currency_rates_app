package com.sikrinick.currencytestapp.domain.usecase.currency

import com.sikrinick.currencytestapp.domain.formatter.BigDecimalFormatter
import java.math.BigDecimal

class CalculateAmountUseCase(
    private val formatter: BigDecimalFormatter
) {

    fun execute(baseAmount: String, rate: String): String {
        val exchangedAmount = BigDecimal(baseAmount) * BigDecimal(rate)
        return formatter.format(exchangedAmount)
    }

}