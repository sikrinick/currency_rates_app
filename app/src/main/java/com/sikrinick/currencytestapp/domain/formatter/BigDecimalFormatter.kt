package com.sikrinick.currencytestapp.domain.formatter

import java.math.BigDecimal
import java.text.DecimalFormat

class BigDecimalFormatter {

    private val decimalFormat = DecimalFormat().apply {
        maximumFractionDigits = 2
        minimumFractionDigits = 2
        isDecimalSeparatorAlwaysShown = true
        groupingSize = 0
    }

    fun format(bigDecimal: BigDecimal): String = decimalFormat.format(bigDecimal)

}