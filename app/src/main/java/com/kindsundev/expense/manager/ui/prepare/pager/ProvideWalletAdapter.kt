package com.kindsundev.expense.manager.ui.prepare.pager

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.databinding.LayoutWalletItemBinding
import com.kindsundev.expense.manager.ui.prepare.PrepareWalletContract
import com.kindsundev.expense.manager.utils.formatDisplayCurrencyBalance

class ProvideWalletAdapter(
    private val wallets: ArrayList<WalletModel>,
    private val listener: PrepareWalletContract.Listener
) : RecyclerView.Adapter<ProvideWalletAdapter.ProvideWalletViewHolder>() {

    inner class ProvideWalletViewHolder(val binding: LayoutWalletItemBinding):
            RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProvideWalletViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = LayoutWalletItemBinding.inflate(layoutInflater)
        return ProvideWalletViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProvideWalletViewHolder, position: Int) {
        val wallet = wallets[position]
        holder.binding.tvName.text = wallet.name
        holder.binding.tvBalance.text =
            "${formatDisplayCurrencyBalance(wallet.balance.toString())} ${wallet.currency}"
        holder.binding.root.setOnClickListener {
            listener.onClickWalletItem(wallet)
        }
    }

    override fun getItemCount(): Int = wallets.size
}