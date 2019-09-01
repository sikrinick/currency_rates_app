package com.sikrinick.currencytestapp.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sikrinick.currencytestapp.data.local.db.model.LocalDbCurrencyRate

@Database(entities = [LocalDbCurrencyRate::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun currencyRates(): CurrencyRatesDao

    companion object {

        fun create(context: Context) = Room
            .databaseBuilder(context, AppDatabase::class.java,"database")
            .fallbackToDestructiveMigration()
            .build()
    }

}