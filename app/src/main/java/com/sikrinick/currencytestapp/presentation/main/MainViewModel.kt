package com.sikrinick.currencytestapp.presentation.main

import androidx.lifecycle.*
import com.sikrinick.currencytestapp.domain.schedulers.AppSchedulers
import com.sikrinick.currencytestapp.domain.usecase.currency.ObserveCurrencyExchangeUseCase
import com.sikrinick.currencytestapp.presentation.model.CurrencyAmount
import com.sikrinick.currencytestapp.utils.replaceableDisposable
import io.reactivex.Flowable
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

class MainViewModel(
    private val observeCurrencyExchangeUseCase: ObserveCurrencyExchangeUseCase,
    private val schedulers: AppSchedulers
): ViewModel(), LifecycleObserver {

    private val ratesData: MutableLiveData<List<CurrencyAmount>> = MutableLiveData()
    val ratesObservable: LiveData<List<CurrencyAmount>> = ratesData

    private val eventsData: MutableLiveData<MainScreenEvent> = MutableLiveData()
    val eventsObservable: LiveData<MainScreenEvent> = eventsData

    private var disposable by replaceableDisposable()

    private var listFlowable = defaultFlowable(CurrencyAmount("Euro", "EUR","1.00"))

    private val currencyListComparator = CurrencyListComparator()

    private fun defaultFlowable(currencyAmount: CurrencyAmount): Flowable<List<CurrencyAmount>> = observeCurrencyExchangeUseCase
        .execute(currencyAmount)
        .map { it.sortedWith(currencyListComparator) }
        .observeOn(schedulers.ui)

    fun onViewFocused(currencyAmount: CurrencyAmount) {
        currencyListComparator.increasePriorityFor(currencyAmount)
        start()
    }

    fun onAmountEntered(currencyAmount: CurrencyAmount) {
        listFlowable =
            if (currencyAmount.amount.isBlank()) {
                Flowable.just(ratesData.value?.map { it.copy(amount = "") })
            } else {
                defaultFlowable(currencyAmount)
            }
        start()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun start() {
        disposable = listFlowable
            .subscribeBy(
                onNext = { ratesData.value = it },
                onError = { error ->
                    Timber.e(error, "Error on receiving currency updates")
                    eventsData.value = MainScreenEvent.UnexpectedError
                }
            )
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        disposable?.dispose()
    }


    override fun onCleared() {
        stop()
        super.onCleared()
    }
}