package com.kindsundev.expense.manager.ui.home.budget.plan.dialog.create

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.databinding.DialogCreatePlanBinding
import com.kindsundev.expense.manager.ui.custom.DateTimePickerDialog
import com.kindsundev.expense.manager.ui.custom.ResultDateTimeCallback
import com.kindsundev.expense.manager.ui.home.budget.plan.wallet.BudgetWalletBottomSheet
import com.kindsundev.expense.manager.ui.home.budget.plan.wallet.BudgetWalletContract
import com.kindsundev.expense.manager.utils.formatInputCurrencyBalance
import com.kindsundev.expense.manager.utils.showMessage

class CreatePlanDialog(
    private val listener: CreatePlanContract.Result
) : DialogFragment(), CreatePlanContract.View {
    private var _binding: DialogCreatePlanBinding? = null
    private val binding get() = _binding
    private lateinit var walletBottomSheet: BudgetWalletBottomSheet
    private lateinit var dialogPresenter: CreatePlanPresenter
    private lateinit var mCurrentWallet: WalletModel

    override fun getCurrentContext(): Context = requireContext()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogCreatePlanBinding.inflate(layoutInflater)

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
        formatInputCurrencyBalance(binding!!.edtEstimatedAmount)
        dialogPresenter = CreatePlanPresenter(this)
        initListener()
        return binding!!.root
    }

    private fun initListener() {
        binding!!.btnCancel.setOnClickListener { dialog!!.dismiss() }
        binding!!.rlSelectWallet.setOnClickListener { onClickSelectWallet() }
        binding!!.iBtnSelectWallet.setOnClickListener { onClickSelectWallet() }
        binding!!.iBtnSelectStartDay.setOnClickListener { onCLickSelectStartDay() }
        binding!!.iBtnSelectEndDay.setOnClickListener { onCLickSelectEndDay() }
        binding!!.btnCreate.setOnClickListener { onClickCreatePlan() }
    }

    private fun onClickSelectWallet() {
        walletBottomSheet = BudgetWalletBottomSheet(object: BudgetWalletContract.Listener {
            override fun onClickWalletItem(wallet: WalletModel) {
                mCurrentWallet = wallet
                binding!!.tvWallet.text = wallet.name
                binding!!.tvWallet.setTextColor(Color.BLACK)
                walletBottomSheet.dismiss()
            }
        })
        walletBottomSheet.show(parentFragmentManager, Constant.BUDGET_WALLET_BOTTOM_SHEET_WALLET_NAME)
    }

    private fun onCLickSelectStartDay() {
        DateTimePickerDialog(requireContext(), object : ResultDateTimeCallback {
            override fun resultNewDateTime(newDateTime: String) {
                binding!!.tvStartDay.text = newDateTime
                binding!!.tvStartDay.setTextColor(Color.BLACK)
            }
        }).onShowDateTimePickerDialog()
    }

    private fun onCLickSelectEndDay() {
        DateTimePickerDialog(requireContext(), object : ResultDateTimeCallback {
            override fun resultNewDateTime(newDateTime: String) {
                binding!!.tvEndDay.text = newDateTime
                binding!!.tvEndDay.setTextColor(Color.BLACK)
            }
        }).onShowDateTimePickerDialog()
    }

    private fun onClickCreatePlan() {
        val name = binding!!.edtName.text.toString()
        val amount = binding!!.edtEstimatedAmount.text.toString().replace(",", "")
        val startDate = binding!!.tvStartDay.text.toString()
        val endDate = binding!!.tvEndDay.text.toString()
        if (binding!!.tvWallet.text.toString() == getCurrentContext().getString(R.string.wallet)) {
            activity?.showMessage(requireContext().getString(R.string.please_select_wallet))
        } else {
            val plan = dialogPresenter.handleDataFromInput(name, amount, startDate, endDate)
            if (plan.id != 0) {
                listener.onSuccessPlan(mCurrentWallet, plan)
                this.dismiss()
            }
        }
    }

    override fun showMessageInvalidData(message: String) {
        activity?.showMessage(message)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}