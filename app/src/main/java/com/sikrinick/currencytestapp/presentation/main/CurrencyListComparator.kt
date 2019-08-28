package com.sikrinick.currencytestapp.presentation.main

import com.sikrinick.currencytestapp.presentation.model.CurrencyAmount

class CurrencyListComparator: Comparator<CurrencyAmount> {

    private var priority = 0

    private val priorityMap = mutableMapOf(
        CurrencyAmount.GBP to --priority,
        CurrencyAmount.USD to --priority,
        CurrencyAmount.EUR to --priority
    )

    override fun compare(first: CurrencyAmount, second: CurrencyAmount): Int {
        val firstCurrencyCode = first.currencyCode
        val secondCurrencyCode = second.currencyCode

        val firstIdx = priorityMap[firstCurrencyCode]
        val secondIdx = priorityMap[secondCurrencyCode]

        return when {
            firstCurrencyCode == secondCurrencyCode -> 0
            firstIdx != null && secondIdx != null -> firstIdx - secondIdx
            firstIdx != null -> -1
            secondIdx != null -> 1
            else -> firstCurrencyCode.compareTo(secondCurrencyCode)
        }
    }

    fun increasePriorityFor(currencyAmount: CurrencyAmount) {
        priorityMap[currencyAmount.currencyCode] = --priority
    }


}