package com.sikrinick.currencytestapp.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sikrinick.currencytestapp.data.local.db.model.LocalDbCurrencyRate
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface CurrencyRatesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateAll(localDbCurrencyRates: List<LocalDbCurrencyRate>): Completable

    @Query("SELECT * FROM LocalDbCurrencyRate")
    fun getAll(): Single<List<LocalDbCurrencyRate>>
}