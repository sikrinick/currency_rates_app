package com.sikrinick.currencytestapp.domain.schedulers

import io.reactivex.Scheduler

class AppSchedulers(
    val computation: Scheduler,
    val io: Scheduler,
    val ui: Scheduler
)