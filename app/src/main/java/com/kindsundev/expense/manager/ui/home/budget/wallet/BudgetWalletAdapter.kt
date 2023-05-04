package com.kindsundev.expense.manager.ui.home.budget.wallet

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.databinding.LayoutBudgetWalletItemBinding
import com.kindsundev.expense.manager.utils.formatDisplayCurrencyBalance

class BudgetWalletAdapter(
    private val wallets: ArrayList<WalletModel>,
    private val listener: BudgetWalletContract.Listener
) : RecyclerView.Adapter<BudgetWalletAdapter.BudgetWalletViewHolder>() {

    inner class BudgetWalletViewHolder(var binding: LayoutBudgetWalletItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetWalletViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = LayoutBudgetWalletItemBinding.inflate(layoutInflater)
        return BudgetWalletViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BudgetWalletViewHolder, position: Int) {
        val wallet = wallets[position]
        holder.binding.tvName.text = wallet.name
        holder.binding.tvBalance.text =
            "${formatDisplayCurrencyBalance(wallet.balance.toString())} ${wallet.currency}"
        holder.binding.btnUpdate.setOnClickListener {
            listener.onClickEditWallet(wallet)
        }
    }

    override fun getItemCount(): Int = wallets.size
}