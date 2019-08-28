package com.sikrinick.currencytestapp.data.remote.model

import com.google.gson.annotations.SerializedName
import com.sikrinick.currencytestapp.shared.model.CurrencyRate
import java.util.*

data class GetCurrencyRatesResponse(
    @SerializedName("base")
    val baseCurrency: Currency,
    @SerializedName("date")
    val date: Date,
    @SerializedName("rates")
    val rates: List<CurrencyRate>
)
/*
{
  "base": "EUR",
  "date": "2018-09-06",
  "rates": {
    "AUD": 1.6229,
    "BGN": 1.9636,
    "BRL": 4.811,
    "CAD": 1.5399,
    "CHF": 1.132,
    "CNY": 7.9769,
    "CZK": 25.818,
    "DKK": 7.4866,
    "GBP": 0.90184,
    "HKD": 9.169,
    "HRK": 7.4639,
    "HUF": 327.8,
    "IDR": 17393.0,
    "ILS": 4.1873,
    "INR": 84.053,
    "ISK": 128.31,
    "JPY": 130.07,
    "KRW": 1310.0,
    "MXN": 22.455,
    "MYR": 4.8313,
    "NOK": 9.8151,
    "NZD": 1.7704,
    "PHP": 62.843,
    "PLN": 4.3356,
    "RON": 4.6571,
    "RUB": 79.893,
    "SEK": 10.633,
    "SGD": 1.6064,
    "THB": 38.283,
    "TRY": 7.6587,
    "USD": 1.1681,
    "ZAR": 17.895
  }
}
 */