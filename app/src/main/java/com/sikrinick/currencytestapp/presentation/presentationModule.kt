package com.sikrinick.currencytestapp.presentation

import com.sikrinick.currencytestapp.presentation.main.MainViewModel
import com.sikrinick.currencytestapp.presentation.main.adapter.CurrencyListAdapter
import com.sikrinick.currencytestapp.presentation.model.CurrencyAmount
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


private val viewModelModule = module {

    viewModel { MainViewModel(get(), get()) }

}

private val uiModule = module {

    factory {
        (onViewFocused: (CurrencyAmount) -> Unit, onAmountEntered: (CurrencyAmount) -> Unit) ->
            CurrencyListAdapter(get(), onViewFocused = onViewFocused, onAmountEntered = onAmountEntered)
    }

}

val presentationModule = uiModule + viewModelModule