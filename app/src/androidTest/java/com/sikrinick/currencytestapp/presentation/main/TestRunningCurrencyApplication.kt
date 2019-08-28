package com.sikrinick.currencytestapp.presentation.main

import android.app.Application
import androidx.emoji.bundled.BundledEmojiCompatConfig
import androidx.emoji.text.EmojiCompat
import com.sikrinick.currencytestapp.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class TestRunningCurrencyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TestRunningCurrencyApplication)
            modules(emptyList())
        }
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        EmojiCompat.init(BundledEmojiCompatConfig(this))
    }

}