package com.kindsundev.expense.manager.ui.home.bag.exchange

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kindsundev.expense.manager.data.model.BillModel
import com.kindsundev.expense.manager.data.model.TransactionModel
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.databinding.LayoutBillParentItemBinding
import com.kindsundev.expense.manager.ui.home.bag.BagContract
import com.kindsundev.expense.manager.utils.amountFormatDisplay

class BillParentAdapter(
    private val wallet: WalletModel,
    private val bills: ArrayList<BillModel>,
    private val listener: BagContract.Listener
) : RecyclerView.Adapter<BillParentAdapter.ExchangeViewHolder>() {

    private val viewPool = RecyclerView.RecycledViewPool()

    inner class ExchangeViewHolder(var binding: LayoutBillParentItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = LayoutBillParentItemBinding.inflate(layoutInflater, parent, false)
        return ExchangeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExchangeViewHolder, position: Int) {
        val bill = bills[position]
        if (bill.transactions?.isEmpty() != true) {
            switchLayout(holder.binding, true)
            initDateOfTransaction(holder.binding, bill.date.toString())
            initCurrentBalanceOfWallet(holder.binding)
            initBillChildAdapter(holder.binding, bill.transactions)
        } else {
            switchLayout(holder.binding, false)
        }
    }

    private fun switchLayout(binding: LayoutBillParentItemBinding, haveData: Boolean) {
        if (haveData) {
            binding.rlBillContainer.visibility = View.VISIBLE
            binding.rcvTransactionDetail.visibility = View.VISIBLE
            binding.llShowHasNoTransaction.visibility = View.GONE
        } else {
            binding.rlBillContainer.visibility = View.GONE
            binding.rcvTransactionDetail.visibility = View.GONE
            binding.llShowHasNoTransaction.visibility = View.VISIBLE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initDateOfTransaction(binding: LayoutBillParentItemBinding, date: String?) {
        val dateListChars = date?.split(" ")
        dateListChars?.let {
            if (dateListChars.size == 1) return
            binding.tvDayNumber.text = dateListChars[0]
            binding.tvDayCharacter.text = dateListChars[1]
            binding.tvMonthYear.text = "${dateListChars[2]}, ${dateListChars[3]}"
        }
    }

    private fun initCurrentBalanceOfWallet(binding: LayoutBillParentItemBinding) {
        binding.tvCurrentBalance.text = amountFormatDisplay(wallet.balance.toString())
    }

    private fun initBillChildAdapter(
        binding: LayoutBillParentItemBinding,
        transactions: ArrayList<TransactionModel>?
    ) {
        binding.rcvTransactionDetail.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = BillChildAdapter(transactions!!, listener)
            setRecycledViewPool(viewPool)
        }
    }

    override fun getItemCount() = bills.size

}