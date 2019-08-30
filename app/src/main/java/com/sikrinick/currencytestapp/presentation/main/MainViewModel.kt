package com.sikrinick.currencytestapp.presentation.main

import androidx.lifecycle.*
import com.sikrinick.currencytestapp.domain.schedulers.AppSchedulers
import com.sikrinick.currencytestapp.domain.usecase.currency.ObserveCurrencyExchangeUseCase
import com.sikrinick.currencytestapp.presentation.model.CurrencyAmount
import com.sikrinick.currencytestapp.utils.replaceableDisposable
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

    private val currencyListComparator = CurrencyListComparator()

    init {
        observeCurrencyExchangeUseCase.setCurrency("EUR")
        observeCurrencyExchangeUseCase.setAmount("1.00")
    }

    fun onCurrencyChosen(currencyAmount: CurrencyAmount) {
        currencyListComparator.increasePriorityFor(currencyAmount)
        observeCurrencyExchangeUseCase.setCurrency(currencyAmount.currencyCode)
    }

    fun onAmountEntered(currencyAmount: CurrencyAmount) = observeCurrencyExchangeUseCase.setAmount(currencyAmount.amount)

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun start() {
        disposable = observeCurrencyExchangeUseCase.execute()
            .map { it.sortedWith(currencyListComparator) }
            .observeOn(schedulers.ui)
            .subscribeBy(
                onNext = { ratesData.value = it },
                onError = { error ->
                    Timber.e(error, "Error on receiving currency updates")
                    eventsData.value = MainScreenEvent.UnexpectedError
                }
            )
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun stop() {
        disposable?.dispose()
    }


    override fun onCleared() {
        stop()
        super.onCleared()
    }
}