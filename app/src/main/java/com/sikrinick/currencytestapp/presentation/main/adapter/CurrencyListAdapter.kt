package com.sikrinick.currencytestapp.presentation.main.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sikrinick.currencytestapp.presentation.model.CurrencyAmount


class CurrencyListAdapter(
    private val onAmountEntered: (CurrencyAmount) -> Unit
) : RecyclerView.Adapter<CurrencyAmountViewHolder>() {

    private val sortedList = SortedCurrencyAmountList(this)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CurrencyAmountViewHolder(
            parent,
            onViewFocused = { sortedList.setAsTopElement(it) },
            onAmountEntered = onAmountEntered
        )

    override fun getItemCount() = sortedList.size

    override fun onBindViewHolder(holder: CurrencyAmountViewHolder, position: Int) {
        holder.bind(sortedList[position])
    }

    override fun onBindViewHolder(holder: CurrencyAmountViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            for (payload in payloads) {
                if (payload is String) {
                    holder.updateAmount(payload)
                }
            }
        }
    }

    fun setList(newList: List<CurrencyAmount>) = sortedList.replaceAll(newList)

}