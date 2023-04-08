package com.kindsundev.expense.manager.ui.home.budget.wallet

import android.app.Dialog
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
import com.kindsundev.expense.manager.utils.*

class CreateWalletDialog(
    private val callback: BudgetWalletContract.Result
): DialogFragment(), BudgetWalletContract.View {
    private var _binding: DialogCreateWalletBinding? = null
    private val binding get() = _binding

    private val loadingDialog by lazy { LoadingDialog() }
    private lateinit var walletPresenter: BudgetWalletPresenter

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
                walletPresenter.handlerCreateWallet(wallet)
            }
        }
    }

    private fun checkNotNullData(): Boolean {
        val name = binding!!.edtName.text.toString()
        val balance = binding!!.edtBalance.text.toString()
        return if (name.isEmpty() && balance.isEmpty()) {
            Toast.makeText(activity, "Please enter full data", Toast.LENGTH_SHORT).show()
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
        val id = hashCodeForID(name, currency, balance, getCurrentTime())
        return WalletModel(id, name, currency, origin = balance.toDouble(), balance.toDouble())
    }

    private fun checkValidData(name: String, balance: String): Boolean {
        return checkValidName(name) && checkValidBalance(balance)
    }

    private fun checkValidName(name: String): Boolean {
        return when (checkName(name)) {
            Status.WRONG_NAME_EMPTY -> {
                activity?.showToast("Don't name to empty")
                return false
            }
            Status.WRONG_NAME_SHORT -> {
                activity?.showToast("Don't name to short")
                return false
            }
            Status.WRONG_NAME_LONG -> {
                activity?.showToast("Don't name to long")
                return false
            }
            Status.WRONG_NAME_HAS_DIGITS -> {
                activity?.showToast("Name cannot is digits")
                return false
            }
            Status.WRONG_HAS_SPECIAL_CHARACTER -> {
                activity?.showToast("Name cannot is special character")
                return false
            }
            else -> true
        }
    }

    private fun checkValidBalance(balance: String): Boolean {
        return when (checkBalance(balance)) {
            Status.WRONG_BALANCE_EMPTY -> {
                activity?.showToast("Don't balance not null")
                return false
            }
            Status.WRONG_HAS_SPECIAL_CHARACTER -> {
                activity?.showToast("Balance cannot is digits")
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        walletPresenter.cleanUp()
    }

}