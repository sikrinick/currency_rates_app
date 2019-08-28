package com.sikrinick.currencytestapp.presentation.model

import java.util.*



data class CurrencyAmount(
    val displayName: String,
    val currencyCode: String,
    val amount: String
) {
    val countryFlag: String

    init {
        val firstChar = Character.codePointAt(currencyCode, 0) - OFFSET_ASCII + OFFSET_FLAG
        val secondChar = Character.codePointAt(currencyCode, 1) - OFFSET_ASCII + OFFSET_FLAG
        countryFlag = String(Character.toChars(firstChar)) + String(Character.toChars(secondChar))
    }

    companion object {
        const val OFFSET_FLAG = 0x1F1E6
        const val OFFSET_ASCII = 0x41


        const val USD = "USD"
        const val EUR = "EUR"
        const val GBP = "GBP"
    }
}

fun Currency.toCurrencyAmount(amount: String) = CurrencyAmount(
    displayName = displayName,
    currencyCode = currencyCode,
    amount = amount
)