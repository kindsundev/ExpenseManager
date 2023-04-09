package com.kindsundev.expense.manager.ui.home.bag.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.data.model.BillModel
import com.kindsundev.expense.manager.data.model.TransactionModel
import com.kindsundev.expense.manager.databinding.LayoutBillParentItemBinding
import com.kindsundev.expense.manager.utils.formatDisplayCurrencyBalance

class BillParentAdapter(
    private val bills: ArrayList<BillModel>,
    private val listener: BillAdapterContract.Listener
) : RecyclerView.Adapter<BillParentAdapter.ExchangeViewHolder>(), BillAdapterContract.View {

    private val viewPool = RecyclerView.RecycledViewPool()
    private val adapterPresenter = BillAdapterPresenter(this)
    private lateinit var view: LayoutBillParentItemBinding
    private lateinit var mColor: String

    inner class ExchangeViewHolder(var binding: LayoutBillParentItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        view = LayoutBillParentItemBinding.inflate(layoutInflater, parent, false)
        return ExchangeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExchangeViewHolder, position: Int) {
        val bill = bills[position]
        if (bill.transactions?.isEmpty() != true) {
            switchLayout(true)
            initDataToUi(holder, bill)
        } else {
            switchLayout(false)
        }
    }

    private fun switchLayout(haveData: Boolean) {
        if (haveData) {
            view.rlBillContainer.visibility = View.VISIBLE
            view.rcvTransactionDetail.visibility = View.VISIBLE
            view.llShowHasNoTransaction.visibility = View.GONE
        } else {
            view.rlBillContainer.visibility = View.GONE
            view.rcvTransactionDetail.visibility = View.GONE
            view.llShowHasNoTransaction.visibility = View.VISIBLE
        }
    }

    private fun initDataToUi(holder: ExchangeViewHolder, bill: BillModel) {
        holder.setIsRecyclable(false)
        initDateOfTransaction(bill.date.toString())
        initCurrentBalanceOfDay(bill)
        initBillChildAdapter(bill.date, bill.transactions)
    }

    @SuppressLint("SetTextI18n")
    private fun initDateOfTransaction(date: String?) {
        val dateListChars = date?.split(" ")
        dateListChars?.let {
            if (dateListChars.size == 1) return
            view.tvDayNumber.text = dateListChars[0]
            view.tvDayCharacter.text = dateListChars[1]
            view.tvMonthYear.text = "${dateListChars[2]}, ${dateListChars[3]}"
        }
    }

    private fun initCurrentBalanceOfDay(bill: BillModel) {
        val result = adapterPresenter.handlerCalculateBalanceOfDay(bill)
        view.tvNewAmount.text = formatDisplayCurrencyBalance(result.toString())
        when (mColor) {
            Constant.TRANSACTION_STATE_BALANCE -> {
                view.tvNewAmount.setTextColor(Color.parseColor(Constant.GRAY_COLOR_CODE))
            }
            Constant.TRANSACTION_STATE_INCOME -> {
                view.tvNewAmount.setTextColor(Color.parseColor(Constant.GREEN_COLOR_CODE))
            }
            else -> {
                view.tvNewAmount.setTextColor(Color.parseColor(Constant.RED_COLOR_CODE))
            }
        }
    }

    private fun initBillChildAdapter(date: String?, transactions: ArrayList<TransactionModel>?) {
        val layoutChildManager = LinearLayoutManager(
            view.rcvTransactionDetail.context,
            LinearLayoutManager.VERTICAL,
            false
        )
        layoutChildManager.initialPrefetchItemCount = transactions!!.size

        view.rcvTransactionDetail.apply {
            layoutManager = layoutChildManager
            adapter = BillChildAdapter(date!!,transactions, listener)
            setRecycledViewPool(viewPool)
            setItemViewCacheSize(10)
        }
    }

    override fun getItemCount() = bills.size

    override fun onResultColor(type: String) { mColor = type }
}