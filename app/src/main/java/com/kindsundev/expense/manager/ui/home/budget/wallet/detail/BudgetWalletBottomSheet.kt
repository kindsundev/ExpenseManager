package com.kindsundev.expense.manager.ui.home.budget.wallet.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.databinding.BottomSheetBudgetWalletDetailBinding
import com.kindsundev.expense.manager.ui.custom.LoadingDialog
import com.kindsundev.expense.manager.utils.*

class BudgetWalletBottomSheet(
    private val wallet: WalletModel,
    private val callback: BudgetWalletDetailContract.Result
): BottomSheetDialogFragment(), BudgetWalletDetailContract.View {
    private var _binding: BottomSheetBudgetWalletDetailBinding? = null
    private val binding get() = _binding
    private val loadingDialog by lazy { LoadingDialog() }

    private lateinit var mWalletDetailPresenter: BudgetWalletDetailPresenter
    private lateinit var mCurrentWallet: WalletModel

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
        formatInputCurrencyBalance(binding!!.edtBalance)
        initDataToUi()
        initListener()
        return binding!!.root
    }

    @SuppressLint("SetTextI18n")
    private fun initDataToUi() {
        val currentCurrency = wallet.currency
        if (currentCurrency == "USD") {
            binding!!.radioBtnUsd.isChecked = true
        } else {
            binding!!.radioBtnVnd.isChecked = true
        }
        val transactionAmount: Int = wallet.getTransactionCount()
        binding!!.tvTransactionCount.text = "Transactions count: $transactionAmount"
        binding!!.edtName.text = wallet.name!!.toEditable()
        binding!!.edtBalance.text = formatDisplayCurrencyBalance(wallet.balance.toString()).toEditable()
    }

    private fun initListener() {
        binding!!.btnArrowDown.setOnClickListener { this.dismiss() }
        binding!!.btnSave.setOnClickListener { onCLickSaveUpdateWallet() }
        binding!!.btnRemove.setOnClickListener { onClickDeleteWallet() }
    }

    private fun onCLickSaveUpdateWallet() {
        if (binding!!.edtBalance.text.toString().isEmpty()) {
            activity?.showToast("Please enter balance!")
        } else if (binding!!.edtName.text.toString().isEmpty()) {
            activity?.showToast("Please enter name!")
        } else {
            mCurrentWallet = getCurrentWallet()
            mWalletDetailPresenter.updateWallet(mCurrentWallet)
        }
    }
    
    private fun onClickDeleteWallet() {
        val alertDialog = MaterialAlertDialogBuilder(requireContext(), R.style.DeleteWarningAlertDialog)
            .setTitle(R.string.delete_wallet)
            .setMessage(R.string.message_for_delete_wallet)
            .setCancelable(false)
            .setPositiveButton("OK") { _, _ ->
                mWalletDetailPresenter.deleteWallet(wallet)
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .create()
        alertDialog.window?.apply {
            setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.CENTER)
        }
        alertDialog.show()
    }

    private fun getCurrentWallet(): WalletModel {
        val id = wallet.id
        val name = binding!!.edtName.text.toString().trim()
        val currency = if (binding!!.radioBtnUsd.isChecked) { "USD" } else { "VND" }
        val origin = wallet.origin
        val balance = binding!!.edtBalance.text.toString().trim().replace(",","")
        val transactions = wallet.transactions
        return WalletModel(id, name, currency, origin, balance.toDouble(), transactions)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mWalletDetailPresenter.cleanUp()
    }

    override fun onLoad() {
        startLoadingDialog(loadingDialog, parentFragmentManager, true)
    }

    override fun onError(message: String) {
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
        activity?.showToast(message)
    }

    override fun onSuccess() {}

    override fun onSuccess(message: String) {
        activity?.showToast(message)
        this.dismiss()
        callback.onSuccessAndRequiredRefreshData(true)
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
    }
}