package com.kindsundev.expense.manager.ui.home.bag.wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.databinding.BottomSheetSelectWalletBinding
import com.kindsundev.expense.manager.ui.home.bag.BagContract

class BagWalletBottomSheet(
    private val wallets: ArrayList<WalletModel>,
    private val listener: BagContract.Listener
): BottomSheetDialogFragment() {
    private var _binding: BottomSheetSelectWalletBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetSelectWalletBinding.inflate(layoutInflater)
        initRecyclerView()
        initListener()
        return binding!!.root
    }

    private fun initRecyclerView() {
        binding!!.rvWallets.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = BagWalletAdapter(wallets, listener)
        }
    }

    private fun initListener() {
        binding!!.btnArrowDown.setOnClickListener { this.dismiss() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}