package com.kindsundev.expense.manager.ui.home.note.transaction.wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.databinding.BottomSheetSelectWalletBinding
import com.kindsundev.expense.manager.ui.custom.LoadingDialog
import com.kindsundev.expense.manager.utils.showToast
import com.kindsundev.expense.manager.utils.startLoadingDialog

class TransactionWalletBottomSheet(
    private val actionListener: TransactionWalletContract.Listener,
) : BottomSheetDialogFragment(), TransactionWalletContract.View{
    private var _binding: BottomSheetSelectWalletBinding? = null
    private val binding get() = _binding
    private val loadingDialog by lazy { LoadingDialog() }

    private var wallets = ArrayList<WalletModel>()
    private lateinit var transactionWalletAdapter: TransactionWalletAdapter
    private lateinit var transactionWalletPresenter: TransactionWalletPresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetSelectWalletBinding.inflate(layoutInflater)
        transactionWalletPresenter = TransactionWalletPresenter(this)
        initRecyclerView()
        initListener()
        return binding!!.root
    }

    private fun initRecyclerView() {
        binding!!.rvWallets.layoutManager = LinearLayoutManager(context)
        transactionWalletPresenter.handlerGetWallets()
    }

    private fun initListener() {
        binding!!.btnArrowDown.setOnClickListener { hideBottomSheet() }
    }

    fun hideBottomSheet() { this.dismiss() }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        transactionWalletPresenter.cleanUp()
    }

    override fun onSuccess(message: String) {
        TODO("Not yet implemented")
    }

    override fun onSuccess() {
        initDataToRecyclerView()
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
    }

    private fun initDataToRecyclerView() {
        wallets = transactionWalletPresenter.getWallets()
        transactionWalletAdapter = TransactionWalletAdapter(wallets, actionListener)
        binding!!.rvWallets.adapter = transactionWalletAdapter
    }

    override fun onLoad() {
        startLoadingDialog(loadingDialog, parentFragmentManager, true)
    }

    override fun onError(message: String) {
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
        activity?.showToast(message)
    }
}