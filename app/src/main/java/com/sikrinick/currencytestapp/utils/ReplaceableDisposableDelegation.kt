package com.sikrinick.currencytestapp.utils

import io.reactivex.disposables.Disposable
import kotlin.reflect.KProperty

fun replaceableDisposable() = ReplaceableDisposableDelegation()

class ReplaceableDisposableDelegation {

    private var disposable: Disposable? = null

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Disposable?) {
        disposable?.dispose()
        disposable = value
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): Disposable? {
        return disposable
    }

}