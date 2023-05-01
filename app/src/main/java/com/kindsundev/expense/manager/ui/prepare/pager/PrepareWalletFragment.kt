package com.kindsundev.expense.manager.ui.prepare.pager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.common.Status
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.databinding.FragmentPrepareWalletBinding
import com.kindsundev.expense.manager.ui.custom.LoadingDialog
import com.kindsundev.expense.manager.ui.home.HomeActivity
import com.kindsundev.expense.manager.ui.prepare.PrepareWalletContract
import com.kindsundev.expense.manager.ui.prepare.PrepareWalletPresenter
import com.kindsundev.expense.manager.utils.*

class PrepareWalletFragment : Fragment(), PrepareWalletContract.View {
    private var _binding: FragmentPrepareWalletBinding? = null
    private val binding get() = _binding
    private val loadingDialog by lazy { LoadingDialog() }

    private val preparePresenter = PrepareWalletPresenter(this)
    private lateinit var wallet: WalletModel
    private lateinit var message: String

    override fun getCurrentContext(): Context = requireContext()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPrepareWalletBinding.inflate(inflater, container, false)
        formatInputCurrencyBalance(binding!!.edtBalance)
        binding!!.btnOk.setOnClickListener { onClickButtonOk() }
        return binding!!.root
    }

    override fun onResume() {
        super.onResume()
        clearCurrentDataWhenBackPressed()
    }

    private fun clearCurrentDataWhenBackPressed() {
        val name = binding!!.edtName.text.toString()
        val balance = binding!!.edtBalance.text.toString()
        if (name.isNotEmpty() and balance.isNotEmpty()) {
            binding!!.edtName.text!!.clear()
            binding!!.edtBalance.text!!.clear()
        }
    }

    private fun onClickButtonOk() {
        if (checkNotNullData()) {
            if (checkValidData(binding!!.edtName.text.toString(), binding!!.edtBalance.text.toString())) {
                wallet = initWalletData()
                preparePresenter.handleCreateWallet(wallet)
            }
        }
    }

    private fun checkNotNullData(): Boolean {
        val name = binding!!.edtName.text.toString()
        val balance = binding!!.edtBalance.text.toString()
        return if (name.isEmpty() && balance.isEmpty()) {
            message = getCurrentContext().getString(R.string.please_provide_full_data)
            activity?.showMessage(message)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        preparePresenter.cleanUp()
    }

    override fun onSuccessWallets(wallets: ArrayList<WalletModel>) {}

    override fun onLoad() {
        startLoadingDialog(loadingDialog, parentFragmentManager, true)
    }

    override fun onError(message: String) {
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
        activity?.showMessage(message)
    }

    override fun onSuccess() {
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
        startHomeActivity(wallet.id.toString())
    }

    private fun startHomeActivity(id: String) {
        startActivity(
            Intent(requireActivity(), HomeActivity::class.java).apply {
                putExtras(
                    Bundle().apply {
                        putString(Constant.KEY_CURRENT_WALLET_ID, id)
                    }
                )
            })
    }
}