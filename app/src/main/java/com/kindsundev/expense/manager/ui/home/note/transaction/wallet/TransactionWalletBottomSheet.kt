package com.kindsundev.expense.manager.ui.home.note.transaction.wallet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.databinding.BottomSheetSelectWalletBinding
import com.kindsundev.expense.manager.ui.custom.LoadingDialog
import com.kindsundev.expense.manager.utils.showMessage
import com.kindsundev.expense.manager.utils.startLoadingDialog

class TransactionWalletBottomSheet(
    private val listener: TransactionWalletContract.Listener,
) : BottomSheetDialogFragment(), TransactionWalletContract.View{
    private var _binding: BottomSheetSelectWalletBinding? = null
    private val binding get() = _binding
    private val loadingDialog by lazy { LoadingDialog() }

    private lateinit var transactionWalletAdapter: TransactionWalletAdapter
    private lateinit var transactionWalletPresenter: TransactionWalletPresenter

    override fun getCurrentContext(): Context = requireContext()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetSelectWalletBinding.inflate(layoutInflater)
        transactionWalletPresenter = TransactionWalletPresenter(this)
        initRecyclerView()
        binding!!.btnArrowDown.setOnClickListener { this.dismiss() }
        return binding!!.root
    }

    private fun initRecyclerView() {
        binding!!.rvWallets.layoutManager = LinearLayoutManager(getCurrentContext())
        transactionWalletPresenter.handleGetWallets()
    }

    override fun onSuccess() {}

    override fun onSuccessWallets(wallets: ArrayList<WalletModel>) {
        initDataToRecyclerView(wallets)
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
    }

    private fun initDataToRecyclerView(wallets: ArrayList<WalletModel>) {
        transactionWalletAdapter = TransactionWalletAdapter(wallets, listener)
        binding!!.rvWallets.adapter = transactionWalletAdapter
    }

    override fun onLoad() {
        startLoadingDialog(loadingDialog, parentFragmentManager, true)
    }

    override fun onError(message: String) {
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
        activity?.showMessage(message)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        transactionWalletPresenter.cleanUp()
    }
}