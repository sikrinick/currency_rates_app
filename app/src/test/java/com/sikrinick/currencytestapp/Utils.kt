package com.sikrinick.currencytestapp

import com.sikrinick.currencytestapp.domain.schedulers.AppSchedulers
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

fun testSchedulers(
    computation: Scheduler = Schedulers.trampoline(),
    io: Scheduler = Schedulers.trampoline(),
    ui: Scheduler = Schedulers.trampoline()
) = AppSchedulers(
    computation = computation,
    io = io,
    ui = ui
)