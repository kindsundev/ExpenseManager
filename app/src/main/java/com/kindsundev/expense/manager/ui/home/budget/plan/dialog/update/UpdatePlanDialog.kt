package com.kindsundev.expense.manager.ui.home.budget.plan.dialog.update

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
import com.kindsundev.expense.manager.data.model.PlanModel
import com.kindsundev.expense.manager.databinding.DialogUpdatePlanBinding
import com.kindsundev.expense.manager.ui.custom.DateTimePickerDialog
import com.kindsundev.expense.manager.ui.custom.LoadingDialog
import com.kindsundev.expense.manager.ui.custom.ResultDateTimeCallback
import com.kindsundev.expense.manager.utils.*

class UpdatePlanDialog(
    private val walletId: Int,
    private val date: String,
    private val plan: PlanModel,
    private val listener: UpdatePlanContract.Listener
) : DialogFragment(), UpdatePlanContract.View {
    private var _binding: DialogUpdatePlanBinding? = null
    private val binding get() = _binding
    private val loadingDialog by lazy { LoadingDialog() }

    private lateinit var updatePlanPresenter: UpdatePlanPresenter

    override fun getCurrentContext(): Context = requireContext()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogUpdatePlanBinding.inflate(layoutInflater)

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
        updatePlanPresenter = UpdatePlanPresenter(this)
        initCurrentData()
        initListener()
        return binding!!.root
    }

    private fun initCurrentData() {
        val amount = formatDisplayCurrencyBalance(plan.estimatedAmount.toString()).trim()
        binding!!.edtEstimatedAmount.text = amount.toEditable()
        binding!!.edtName.text = plan.name!!.toEditable()
        binding!!.tvStartDay.text = plan.startDate
        binding!!.tvEndDay.text = plan.endDate
    }

    private fun initListener() {
        binding!!.btnCancel.setOnClickListener { dialog!!.dismiss() }
        binding!!.iBtnSelectStartDay.setOnClickListener { onCLickSelectStartDay() }
        binding!!.iBtnSelectEndDay.setOnClickListener { onCLickSelectEndDay() }
        binding!!.btnUpdate.setOnClickListener { onClickCreatePlan() }
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
        val estimatedAmount = binding!!.edtEstimatedAmount.text.toString().replace(",", "")
        val startDate = binding!!.tvStartDay.text.toString()
        val endDate = binding!!.tvEndDay.text.toString()
        updatePlanPresenter.handleDataFromInput(
            name,
            estimatedAmount,
            startDate,
            endDate,
            walletId,
            plan
        )
    }

    override fun onPlanValidation(plan: PlanModel) {
        updatePlanPresenter.handleUpdatePlan(walletId, date, plan)
    }

    override fun showMessageInvalidData(message: String) {
        activity?.showMessage(message)
    }

    override fun onLoad() {
        startLoadingDialog(loadingDialog, parentFragmentManager, true)
    }

    override fun onError(message: String) {
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
        activity?.showMessage(message)
    }

    override fun onSuccess() {}

    override fun onSuccess(walletId: Int, dateKey: String, planId: Int) {
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
        activity?.showMessage(getCurrentContext().getString(R.string.update_plan_success))
        listener.requestUpdateData(walletId, dateKey, planId)
        this.dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}