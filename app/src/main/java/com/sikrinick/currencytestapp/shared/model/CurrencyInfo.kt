package com.sikrinick.currencytestapp.shared.model

import java.util.*

data class CurrencyInfo(
    val currency: Currency,
    val rates: List<CurrencyRate>
)