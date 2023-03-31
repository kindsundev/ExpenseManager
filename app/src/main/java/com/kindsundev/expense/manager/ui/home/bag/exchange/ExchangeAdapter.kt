package com.kindsundev.expense.manager.ui.home.bag.exchange

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kindsundev.expense.manager.data.model.BillModel
import com.kindsundev.expense.manager.databinding.LayoutWalletExchangeItemBinding
import com.kindsundev.expense.manager.ui.home.bag.BagContract

class ExchangeAdapter(
    private val bills: ArrayList<BillModel>,
    private val listener: BagContract.Listener
) : RecyclerView.Adapter<ExchangeAdapter.ExchangeViewHolder>() {

    private val viewPool = RecyclerView.RecycledViewPool()

    inner class ExchangeViewHolder(var binding: LayoutWalletExchangeItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = LayoutWalletExchangeItemBinding.inflate(layoutInflater, parent, false)
        return ExchangeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExchangeViewHolder, position: Int) {
        val bill = bills[position]
        initDateOfTransaction(holder.binding, bill.date.toString())

        for (key in bill.mapTransactions!!.keys) {
                /*
                * init child adapter (list transaction)
                * new issue: get list transactions is error
                * java.lang.ClassCastException
                * */
//            val value: TransactionModel? = data.hashMap!![key] as TransactionModel
        }

//        holder.binding.rcvTransactionDetail.apply {
//            layoutManager = LinearLayoutManager(context)
//            adapter = BillAdapter(above, listener)
//            setRecycledViewPool(viewPool)
//        }
    }


    @SuppressLint("SetTextI18n")
    private fun initDateOfTransaction(binding: LayoutWalletExchangeItemBinding, date: String?) {
        val dateListChars = date?.split(" ")
        dateListChars?.let {
            if (dateListChars.size == 1) return
            binding.tvDayNumber.text = dateListChars[0]
            binding.tvDayCharacter.text = dateListChars[1]
            binding.tvMonthYear.text = "${dateListChars[2]}, ${dateListChars[3]}"
        }
    }

    override fun getItemCount() = bills.size

}