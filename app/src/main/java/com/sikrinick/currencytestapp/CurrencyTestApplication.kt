package com.sikrinick.currencytestapp

import android.app.Application
import androidx.emoji.bundled.BundledEmojiCompatConfig
import androidx.emoji.text.EmojiCompat
import com.sikrinick.currencytestapp.data.repoModule
import com.sikrinick.currencytestapp.domain.domainModule
import com.sikrinick.currencytestapp.presentation.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class CurrencyTestApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CurrencyTestApplication)
            modules(
                repoModule
                        + domainModule
                        + presentationModule
            )
        }
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        EmojiCompat.init(BundledEmojiCompatConfig(this))
    }
}