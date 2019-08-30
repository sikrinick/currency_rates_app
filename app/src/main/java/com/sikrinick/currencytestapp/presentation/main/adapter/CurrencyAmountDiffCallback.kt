package com.sikrinick.currencytestapp.presentation.main.adapter

import androidx.recyclerview.widget.DiffUtil
import com.sikrinick.currencytestapp.presentation.model.CurrencyAmount

class CurrencyAmountDiffCallback(
    val oldList: List<CurrencyAmount>,
    val newList: List<CurrencyAmount>
): DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].currencyCode == newList[newItemPosition].currencyCode

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].amount == newList[newItemPosition].amount

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int) = newList[newItemPosition].amount
}