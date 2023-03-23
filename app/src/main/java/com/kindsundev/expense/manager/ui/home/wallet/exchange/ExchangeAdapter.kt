package com.kindsundev.expense.manager.ui.home.wallet.exchange

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kindsundev.expense.manager.databinding.LayoutWalletExchangeItemBinding
import com.kindsundev.expense.manager.ui.home.wallet.BagContract

class ExchangeAdapter(
    private val transactions: ArrayList<String>,
    private val listener: BagContract.Listener
) : RecyclerView.Adapter<ExchangeAdapter.ExchangeViewHolder>() {

    private val viewPool = RecyclerView.RecycledViewPool()

    inner class ExchangeViewHolder(var binding: LayoutWalletExchangeItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = LayoutWalletExchangeItemBinding.inflate(layoutInflater, parent, false)
        val viewHolder = ExchangeViewHolder(view)
        initRecyclerViewChild(viewHolder)
        return viewHolder
    }

    private fun initRecyclerViewChild(viewHolder: ExchangeViewHolder) {
        viewHolder.binding.rcvTransactionDetail.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = BillAdapter(transactions, listener)
            setRecycledViewPool(viewPool)
        }
    }

    override fun onBindViewHolder(holder: ExchangeViewHolder, position: Int) {

    }

    override fun getItemCount() = transactions.size

}