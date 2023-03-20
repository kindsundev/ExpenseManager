package com.kindsundev.expense.manager.ui.home.note.transaction.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.databinding.BottomSheetWalletBinding
import com.kindsundev.expense.manager.ui.custom.LoadingDialog
import com.kindsundev.expense.manager.ui.home.note.transaction.bottomsheet.create.CreateWalletDialog
import com.kindsundev.expense.manager.ui.home.note.transaction.bottomsheet.create.ResultWalletCallback
import com.kindsundev.expense.manager.utils.showToast
import com.kindsundev.expense.manager.utils.startLoadingDialog

class WalletBottomSheet(
    private val actionListener: WalletContract.Listener,
) : BottomSheetDialogFragment(), ResultWalletCallback, WalletContract.View {
    private var _binding: BottomSheetWalletBinding? = null
    private val binding get() = _binding
    private val loadingDialog by lazy { LoadingDialog() }

    private var wallets = ArrayList<WalletModel>()
    private lateinit var walletAdapter: WalletAdapter
    private lateinit var createWalletDialog: CreateWalletDialog
    private lateinit var walletPresenter: WalletPresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetWalletBinding.inflate(layoutInflater)
        walletPresenter = WalletPresenter(this)
        initRecyclerView()
        initListener()
        return binding!!.root
    }

    private fun initRecyclerView() {
        binding!!.rvWallets.layoutManager = LinearLayoutManager(context)
        walletPresenter.handlerGetWallets()
    }

    private fun initListener() {
        binding!!.btnArrowDown.setOnClickListener { hideBottomSheet() }
        binding!!.btnAddWallet.setOnClickListener { onClickAddWallet() }
    }

    private fun onClickAddWallet() {
        createWalletDialog = CreateWalletDialog(this)
        createWalletDialog.show(parentFragmentManager, createWalletDialog.tag)
    }

    fun hideBottomSheet() {
        this.dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateWalletResult(success: Boolean) {
        // update list
    }

    override fun onSuccess(message: String) {
        TODO("Not yet implemented")
    }

    override fun onSuccess() {
        initDataToRecyclerView()
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
    }

    private fun initDataToRecyclerView() {
        wallets = walletPresenter.getWallets()
        walletAdapter = WalletAdapter(wallets, actionListener)
        binding!!.rvWallets.adapter = walletAdapter
    }

    override fun onLoad() {
        startLoadingDialog(loadingDialog, parentFragmentManager, true)
    }

    override fun onError(message: String) {
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
        activity?.showToast(message)
    }
}