package com.sikrinick.currencytestapp.presentation

import com.sikrinick.currencytestapp.presentation.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


private val viewModelModule = module {

    viewModel { MainViewModel(get(), get()) }

}

private val uiModule = module {
    //todo
}

val presentationModule = uiModule + viewModelModule