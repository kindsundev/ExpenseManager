package com.kindsundev.expense.manager.ui.home.bag.exchange

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kindsundev.expense.manager.databinding.LayoutWalletBillItemBinding
import com.kindsundev.expense.manager.ui.home.bag.BagContract

class BillAdapter(
    private val transactions: ArrayList<String>,
    private val listener: BagContract.Listener
): RecyclerView.Adapter<BillAdapter.BillViewHolder>() {

    inner class BillViewHolder(val binding: LayoutWalletBillItemBinding):
            RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = LayoutWalletBillItemBinding.inflate(layoutInflater, parent, false)
        return BillViewHolder(view)
    }

    override fun onBindViewHolder(holder: BillViewHolder, position: Int) {
        val bill = transactions[position]
        holder.binding.tvCategoryName.text = bill
        holder.binding.tvNote.text = "Note of $bill"
        holder.binding.root.setOnClickListener {
            listener.onClickTransaction(bill)
        }
    }

    override fun getItemCount() = transactions.size
}