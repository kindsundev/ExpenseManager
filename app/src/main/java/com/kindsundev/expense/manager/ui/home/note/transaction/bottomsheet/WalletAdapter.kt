package com.kindsundev.expense.manager.ui.home.note.transaction.bottomsheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kindsundev.expense.manager.databinding.LayoutWalletItemBinding

/*
* After setup realtime database, change List<String> to List<Object>
*/

internal class WalletAdapter(
    private val mListItems: List<String>,
    private val listener: WalletListener
) : RecyclerView.Adapter<WalletAdapter.WalletViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalletViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = LayoutWalletItemBinding.inflate(layoutInflater)
        return WalletViewHolder(view)
    }

    override fun onBindViewHolder(holder: WalletViewHolder, position: Int) {
        val wallet = mListItems[position]
        holder.binding.tvName.text = wallet
        holder.binding.root.setOnClickListener {
            listener.onClickWalletItem(wallet)
        }
    }

    override fun getItemCount(): Int = mListItems.size

    inner class WalletViewHolder(val binding: LayoutWalletItemBinding) :
        RecyclerView.ViewHolder(binding.root)

}