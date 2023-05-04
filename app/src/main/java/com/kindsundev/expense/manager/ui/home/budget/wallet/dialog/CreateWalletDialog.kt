package com.kindsundev.expense.manager.ui.home.budget.wallet.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.common.Status
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.databinding.DialogCreateWalletBinding
import com.kindsundev.expense.manager.ui.custom.LoadingDialog
import com.kindsundev.expense.manager.ui.home.budget.wallet.BudgetWalletContract
import com.kindsundev.expense.manager.ui.home.budget.wallet.BudgetWalletPresenter
import com.kindsundev.expense.manager.utils.*

class CreateWalletDialog(
    private val callback: BudgetWalletContract.Result
): DialogFragment(), BudgetWalletContract.View {
    private var _binding: DialogCreateWalletBinding? = null
    private val binding get() = _binding

    private val loadingDialog by lazy { LoadingDialog() }
    private lateinit var walletPresenter: BudgetWalletPresenter
    private lateinit var message: String

    override fun getCurrentContext(): Context = requireContext()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogCreateWalletBinding.inflate(layoutInflater)

        val dialog = MaterialAlertDialogBuilder(
            requireActivity(), R.style.Theme_ExpenseManager
        ).apply {
            setCancelable(false)
            setView(binding!!.root)
        }.create()

        dialog.window?.apply {
            setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.CENTER)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        walletPresenter = BudgetWalletPresenter(this)
        formatInputCurrencyBalance(binding!!.edtBalance)
        initListener()
        return binding!!.root
    }

    private fun initListener() {
        binding!!.btnCancel.setOnClickListener { dialog!!.dismiss() }
        binding!!.btnCreate.setOnClickListener { onClickCreateWallet() }
    }

    private fun onClickCreateWallet() {
        if (checkNotNullData()) {
            if (checkValidData(binding!!.edtName.text.toString(), binding!!.edtBalance.text.toString())) {
                val wallet = initWalletData()
                walletPresenter.handleCreateWallet(wallet)
            }
        }
    }

    private fun checkNotNullData(): Boolean {
        val name = binding!!.edtName.text.toString()
        val balance = binding!!.edtBalance.text.toString()
        return if (name.isEmpty() && balance.isEmpty()) {
            activity?.showMessage(getCurrentContext().getString(R.string.please_provide_full_data))
            false
        } else {
            true
        }
    }

    private fun initWalletData(): WalletModel {
        val name = binding!!.edtName.text.toString().trim()
        val balance = binding!!.edtBalance.text.toString().trim().replace(",", "")
        val currency = when (binding!!.radioGroupCurrency.checkedRadioButtonId) {
            binding!!.radioBtnUsd.id -> {
                binding!!.radioBtnUsd.text.toString().trim()
            }
            else -> {
                binding!!.radioBtnVnd.text.toString().trim()
            }
        }
        val id = hashCodeForID(name, currency, balance, getCurrentDateTime())
        return WalletModel(id, name, currency, origin = balance.toDouble(), balance.toDouble())
    }

    private fun checkValidData(name: String, balance: String): Boolean {
        return checkValidName(name) && checkValidBalance(balance)
    }

    private fun checkValidName(name: String): Boolean {
        return when (checkName(name)) {
            Status.WRONG_NAME_EMPTY -> {
                message = getCurrentContext().getString(R.string.name_not_null)
                activity?.showMessage(message)
                return false
            }
            Status.WRONG_NAME_SHORT -> {
                message = getCurrentContext().getString(R.string.name_to_short)
                activity?.showMessage(message)
                return false
            }
            Status.WRONG_NAME_LONG -> {
                message = getCurrentContext().getString(R.string.name_to_long)
                activity?.showMessage(message)
                return false
            }
            Status.WRONG_NAME_HAS_DIGITS -> {
                message = getCurrentContext().getString(R.string.name_cannot_digits)
                activity?.showMessage(message)
                return false
            }
            Status.WRONG_HAS_SPECIAL_CHARACTER -> {
                message = getCurrentContext().getString(R.string.name_cannot_character)
                activity?.showMessage(message)
                return false
            }
            else -> true
        }
    }

    private fun checkValidBalance(balance: String): Boolean {
        return when (checkBalance(balance)) {
            Status.WRONG_BALANCE_EMPTY -> {
                message = getCurrentContext().getString(R.string.balance_not_null)
                activity?.showMessage(message)
                return false
            }
            Status.WRONG_HAS_SPECIAL_CHARACTER -> {
                message = getCurrentContext().getString(R.string.balance_cannot_character)
                activity?.showMessage(message)
                return false
            }
            else -> true
        }
    }

    override fun onLoad() {
        hideKeyboard()
        startLoadingDialog(loadingDialog, parentFragmentManager, true)
    }

    override fun onError(message: String) {
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSuccess(message: String) {
        callback.onResultCreateWallet(true)
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
        this.dismiss()
    }

    override fun onSuccess() {}

    override fun onSuccessWallets(wallets: ArrayList<WalletModel>) {}

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        walletPresenter.cleanUp()
    }

}