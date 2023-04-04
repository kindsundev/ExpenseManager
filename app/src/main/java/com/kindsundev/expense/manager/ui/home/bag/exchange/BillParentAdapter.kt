package com.kindsundev.expense.manager.ui.home.bag.exchange

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
import com.kindsundev.expense.manager.ui.home.bag.BagContract
import com.kindsundev.expense.manager.ui.home.bag.BagPresenter
import com.kindsundev.expense.manager.utils.amountFormatDisplay

class BillParentAdapter(
    private val bills: ArrayList<BillModel>,
    private val listener: BagContract.Listener
) : RecyclerView.Adapter<BillParentAdapter.ExchangeViewHolder>(), BagContract.ViewChild {

    private val viewPool = RecyclerView.RecycledViewPool()
    private val bagPresenter = BagPresenter(bagView = null, adapterView = this)
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
            initDateOfTransaction(bill.date.toString())
            initCurrentBalanceOfWallet(bill)
            initBillChildAdapter(bill.transactions)
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

    private fun initCurrentBalanceOfWallet(bill: BillModel) {
        val result = bagPresenter.handlerCalculateBalanceOfDay(bill)
        view.tvNewAmount.text = amountFormatDisplay(result.toString())
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

    private fun initBillChildAdapter(transactions: ArrayList<TransactionModel>?) {
        view.rcvTransactionDetail.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = BillChildAdapter(transactions!!, listener)
            setRecycledViewPool(viewPool)
        }
    }

    override fun getItemCount() = bills.size

    override fun onResultColor(type: String) {
        mColor = type
    }
}