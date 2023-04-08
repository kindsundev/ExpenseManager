package com.kindsundev.expense.manager.ui.home.budget.wallet.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kindsundev.expense.manager.common.Logger
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.databinding.BottomSheetBudgetWalletDetailBinding
import com.kindsundev.expense.manager.ui.custom.LoadingDialog
import com.kindsundev.expense.manager.utils.formatDisplayCurrencyBalance
import com.kindsundev.expense.manager.utils.showToast
import com.kindsundev.expense.manager.utils.startLoadingDialog
import com.kindsundev.expense.manager.utils.toEditable

class BudgetWalletBottomSheet(
    private val wallet: WalletModel
): BottomSheetDialogFragment(), BudgetWalletDetailContract.View {
    private var _binding: BottomSheetBudgetWalletDetailBinding? = null
    private val binding get() = _binding
    private val loadingDialog by lazy { LoadingDialog() }

    private lateinit var mWalletDetailPresenter: BudgetWalletDetailPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mWalletDetailPresenter = BudgetWalletDetailPresenter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetBudgetWalletDetailBinding.inflate(layoutInflater)
        initDataToUi()
        initListener()
        return binding!!.root
    }

    private fun initDataToUi() {
        val currentCurrency = wallet.currency
        if (currentCurrency == "USD") {
            binding!!.radioBtnUsd.isChecked = true
        } else {
            binding!!.radioBtnVnd.isChecked = true
        }
        binding!!.edtName.text = wallet.name!!.toEditable()
        binding!!.edtBalance.text = formatDisplayCurrencyBalance(wallet.balance.toString()).toEditable()
        Logger.error(wallet.transaction.toString())
    }

    private fun initListener() {
        binding!!.btnArrowDown.setOnClickListener { this.dismiss() }
        binding!!.btnSave.setOnClickListener {  }
        binding!!.btnRemove.setOnClickListener {  }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onLoad() {
        startLoadingDialog(loadingDialog, parentFragmentManager, true)
    }

    override fun onError(message: String) {
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
        activity?.showToast(message)
    }

    override fun onSuccess() {
        startLoadingDialog(loadingDialog, parentFragmentManager, false)

    }
}