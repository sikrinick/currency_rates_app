package com.sikrinick.currencytestapp.data.local.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocalDbCurrencyRate(

    @PrimaryKey
    val currencyCode: String,

    @ColumnInfo(name = "rate")
    val rate: String
)


