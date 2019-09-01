package com.sikrinick.currencytestapp.domain.usecase.currency

import com.sikrinick.currencytestapp.domain.formatter.BigDecimalFormatter
import java.math.BigDecimal

class CalculateAmountUseCase(
    private val formatter: BigDecimalFormatter
) {

    //EUR/USD = 1.61
    //EUR/PLN = 4.27
    //USD/PLN = 4.27 / 1.61

    fun execute(
        baseCurrencyAmount: String,
        baseCurrencyRate: String,
        targetCurrencyRate: String
    ): String {
        val exchangedAmount = BigDecimal(baseCurrencyAmount) *
                BigDecimal(targetCurrencyRate) /
                BigDecimal(baseCurrencyRate)
        return formatter.format(exchangedAmount)
    }

}