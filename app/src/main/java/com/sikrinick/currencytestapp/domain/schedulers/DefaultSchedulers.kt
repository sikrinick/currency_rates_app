package com.sikrinick.currencytestapp.domain.schedulers

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

val defaultSchedulers = AppSchedulers(
    computation = Schedulers.computation(),
    io = Schedulers.io(),
    ui = AndroidSchedulers.mainThread()
)