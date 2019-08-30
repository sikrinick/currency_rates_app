package com.sikrinick.currencytestapp.presentation.main.adapter

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import com.sikrinick.currencytestapp.presentation.model.CurrencyAmount

class SortedCurrencyAmountList(adapter: RecyclerView.Adapter<*>) {

    private var priority = 0

    private val priorityMap = mutableMapOf(
        CurrencyAmount.GBP to --priority,
        CurrencyAmount.USD to --priority,
        CurrencyAmount.EUR to --priority
    )

    private val batchedCallback = SortedList.BatchedCallback(object : SortedList.Callback<CurrencyAmount>() {

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            adapter.notifyItemMoved(fromPosition, toPosition)
        }

        override fun onChanged(position: Int, count: Int) {
            adapter.notifyItemRangeChanged(position, count)
        }

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            adapter.notifyItemRangeChanged(position, count, payload)
        }

        override fun onInserted(position: Int, count: Int) {
            adapter.notifyItemRangeInserted(position, count)
        }

        override fun onRemoved(position: Int, count: Int) {
            adapter.notifyItemRangeRemoved(position, count)
        }

        override fun areItemsTheSame(oldItem: CurrencyAmount, newItem: CurrencyAmount) =
            oldItem.currencyCode == newItem.currencyCode

        override fun areContentsTheSame(oldItem: CurrencyAmount, newItem: CurrencyAmount) =
            oldItem.amount == newItem.amount

        override fun compare(first: CurrencyAmount, second: CurrencyAmount): Int {
            val firstCurrencyCode = first.currencyCode
            val secondCurrencyCode = second.currencyCode

            val firstIdx = priorityMap[firstCurrencyCode]
            val secondIdx = priorityMap[secondCurrencyCode]

            return when {
                firstCurrencyCode == secondCurrencyCode -> 0
                firstIdx != null && secondIdx != null -> firstIdx - secondIdx
                firstIdx != null -> -1
                secondIdx != null -> 1
                else -> firstCurrencyCode.compareTo(secondCurrencyCode)
            }
        }

        override fun getChangePayload(oldItem: CurrencyAmount, newItem: CurrencyAmount) = newItem.amount

    })

    private val list = SortedList(CurrencyAmount::class.java, batchedCallback)

    fun replaceAll(newList: List<CurrencyAmount>) {
        list.replaceAll(newList)
        batchedCallback.dispatchLastEvent()
    }

    val size: Int
        get() = list.size()

    operator fun get(index: Int): CurrencyAmount = list[index]

    fun setAsTopElement(currencyAmount: CurrencyAmount) {
        val idx = list.indexOf(currencyAmount)
        if (idx >= 0) {
            priorityMap[currencyAmount.currencyCode] = --priority
            list.recalculatePositionOfItemAt(idx)
        }
    }

}