package com.sikrinick.currencytestapp.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sikrinick.currencytestapp.data.local.db.model.LocalDbCurrencyRates
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface CurrencyRatesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update(localDbCurrencyRates: LocalDbCurrencyRates): Completable

    @Query("SELECT * FROM LocalDbCurrencyRates WHERE currencyCode = :currencyCode")
    fun get(currencyCode: String): Single<LocalDbCurrencyRates>
}