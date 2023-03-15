package com.kindsundev.expense.manager.ui.home.note.transaction.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.databinding.BottomSheetWalletBinding
import com.kindsundev.expense.manager.ui.home.note.transaction.bottomsheet.create.CreateWalletDialog
import com.kindsundev.expense.manager.ui.home.note.transaction.bottomsheet.create.ResultWalletCallback

class WalletBottomSheet(
    private val wallets: ArrayList<WalletModel>,
    private val actionListener: WalletListener,
    private val resultListener: ResultWalletCallback
) : BottomSheetDialogFragment() {
    private var _binding: BottomSheetWalletBinding? = null
    private val binding get() = _binding

    private lateinit var walletAdapter: WalletAdapter
    private lateinit var createWalletDialog: CreateWalletDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetWalletBinding.inflate(layoutInflater)
        initRecyclerView()
        initListener()
        return binding!!.root
    }

    private fun initRecyclerView() {
        binding!!.rvWallets.layoutManager = LinearLayoutManager(context)
        walletAdapter = WalletAdapter(wallets, actionListener)
        binding!!.rvWallets.adapter = walletAdapter
    }

    private fun initListener() {
        binding!!.btnArrowDown.setOnClickListener { hideBottomSheet() }
        binding!!.btnAddWallet.setOnClickListener { onClickAddWallet() }
    }

    private fun onClickAddWallet() {
        createWalletDialog = CreateWalletDialog(resultListener)
        createWalletDialog.show(parentFragmentManager, createWalletDialog.tag)
    }

    internal fun addNewWallet(wallet: WalletModel) {
        wallets.add(wallet)
        walletAdapter.notifyItemInserted(wallets.size + 1)
        binding!!.rvWallets.adapter = walletAdapter
    }

    fun hideBottomSheet() {
        this.dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}