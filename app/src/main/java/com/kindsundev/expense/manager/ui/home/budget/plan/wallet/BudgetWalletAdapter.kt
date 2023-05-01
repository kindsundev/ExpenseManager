package com.kindsundev.expense.manager.ui.home.budget.plan.wallet

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.databinding.LayoutWalletItemBinding
import com.kindsundev.expense.manager.utils.formatDisplayCurrencyBalance

internal class BudgetWalletAdapter(
    private val wallets: ArrayList<WalletModel>,
    private val listener: BudgetWalletContract.Listener
): RecyclerView.Adapter<BudgetWalletAdapter.WalletViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalletViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = LayoutWalletItemBinding.inflate(layoutInflater)
        return WalletViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: WalletViewHolder, position: Int) {
        val wallet = wallets[position]
        holder.binding.tvName.text = wallet.name
        holder.binding.tvBalance.text =
            "${formatDisplayCurrencyBalance(wallet.balance.toString())} ${wallet.currency}"
        holder.binding.root.setOnClickListener {
            listener.onClickWalletItem(wallet)
        }
    }

    override fun getItemCount(): Int = wallets.size

    inner class WalletViewHolder(val binding: LayoutWalletItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}