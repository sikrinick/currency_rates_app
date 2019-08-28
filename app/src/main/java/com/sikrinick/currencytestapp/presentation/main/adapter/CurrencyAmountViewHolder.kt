package com.sikrinick.currencytestapp.presentation.main.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sikrinick.currencytestapp.R
import com.sikrinick.currencytestapp.presentation.model.CurrencyAmount
import kotlinx.android.synthetic.main.view_currency_amount.view.*

class CurrencyAmountViewHolder(
    parent: ViewGroup,
    private val onViewFocused: (CurrencyAmount) -> Unit,
    private val onAmountEntered: (CurrencyAmount) -> Unit
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(
        R.layout.view_currency_amount,
        parent,
        false
    )
) {

    private lateinit var currencyAmount: CurrencyAmount
    private var amountIsBeingChanged = false

    fun bind(currencyAmount: CurrencyAmount) {
        this.currencyAmount = currencyAmount
        itemView.apply {
            country_flag.text = currencyAmount.countryFlag
            currency_code.text = currencyAmount.currencyCode
            currency_name.text = currencyAmount.displayName
            updateAmount(currencyAmount.amount)

            currency_amount.setOnFocusChangeListener { _, isFocused ->
                if (isFocused) {
                    amountIsBeingChanged = true
                    currency_amount.addTextChangedListener(onTextChangedWatcher)
                    onViewFocused(currencyAmount)
                } else {
                    currency_amount.removeTextChangedListener(onTextChangedWatcher)
                    amountIsBeingChanged = false
                }
            }
        }
    }

    fun updateAmount(newAmount: String) {
        if (amountIsBeingChanged) return
        currencyAmount = currencyAmount.copy(amount = newAmount)
        itemView.currency_amount.setText(currencyAmount.amount)
    }

    private val onTextChangedWatcher = object : TextWatcher {
        override fun beforeTextChanged(source: CharSequence?, start: Int, end: Int, count: Int) {}
        override fun afterTextChanged(source: Editable?) {}
        override fun onTextChanged(source: CharSequence?, start: Int, end: Int, count: Int) {
            onAmountEntered(currencyAmount.copy(amount = source?.toString() ?: ""))
        }
    }
}