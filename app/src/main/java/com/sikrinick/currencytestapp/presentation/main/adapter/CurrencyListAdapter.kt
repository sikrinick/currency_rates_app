package com.sikrinick.currencytestapp.presentation.main.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sikrinick.currencytestapp.domain.schedulers.AppSchedulers
import com.sikrinick.currencytestapp.presentation.model.CurrencyAmount
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber


class CurrencyListAdapter(
    private val schedulers: AppSchedulers,
    private val onViewFocused: (CurrencyAmount) -> Unit,
    private val onAmountEntered: (CurrencyAmount) -> Unit
) : RecyclerView.Adapter<CurrencyAmountViewHolder>() {

    //private val sortedList = SortedCurrencyAmountList(this)

    private var currencyAmountList = listOf<CurrencyAmount>()
    private val listSubject = BehaviorSubject.create<List<CurrencyAmount>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CurrencyAmountViewHolder(
            parent,
            onViewFocused = onViewFocused, //{ onViewFocused.setAsTopElement(it) },
            onAmountEntered = onAmountEntered
        )

    override fun getItemCount() = currencyAmountList.size //sortedList.size

    override fun onBindViewHolder(holder: CurrencyAmountViewHolder, position: Int) {
        holder.bind(currencyAmountList[position]) //sortedList[position]
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

    //fun setList(newList: List<CurrencyAmount>) = sortedList.replaceAll(newList)

    fun setList(newList: List<CurrencyAmount>) = listSubject.onNext(newList)

    private lateinit var disposable: Disposable

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        disposable = listSubject
            .map { currencyAmountList to it }
            .map { (oldList, newList) ->
                newList to DiffUtil.calculateDiff(
                    CurrencyAmountDiffCallback(
                        oldList = oldList,
                        newList = newList
                    )
                )
            }
            .subscribeOn(schedulers.computation)
            .observeOn(schedulers.ui)
            .subscribeBy(
                onNext = { (newList, result) ->
                    currencyAmountList = newList
                    result.dispatchUpdatesTo(this)
                },
                onError = { Timber.e(it, "Unexpected error in recycler view") }
            )
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        disposable.dispose()
    }
}