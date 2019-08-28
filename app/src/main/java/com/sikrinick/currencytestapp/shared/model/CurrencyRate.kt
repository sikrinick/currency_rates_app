package com.sikrinick.currencytestapp.shared.model

import java.util.*

data class CurrencyRate(
    val currency: Currency,
    val ratePerOne: String
)