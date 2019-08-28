package com.sikrinick.currencytestapp.presentation.main

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.sikrinick.currencytestapp.R
import com.sikrinick.currencytestapp.presentation.main.adapter.CurrencyListAdapter
import com.sikrinick.currencytestapp.presentation.model.CurrencyAmount
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModel<MainViewModel>()
    private val currencyAdapter by inject<CurrencyListAdapter> {
        parametersOf(
            { currencyAmount: CurrencyAmount -> viewModel.onViewFocused(currencyAmount) },
            { currencyAmount: CurrencyAmount -> viewModel.onAmountEntered(currencyAmount) }
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        currency_list.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = currencyAdapter
        }

        lifecycle.addObserver(viewModel)
        viewModel.ratesObservable.observe(this, ratesListObserver)
        viewModel.eventsObservable.observe(this, eventsObserver)
    }

    private val ratesListObserver = Observer<List<CurrencyAmount>> {
        currencyAdapter.setList(it)
    }

    private val eventsObserver = Observer<MainScreenEvent> {
        when(it) {
            is MainScreenEvent.UnexpectedError -> {
                Toast.makeText(this, R.string.unexpected_error, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
