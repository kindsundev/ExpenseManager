package com.kindsundev.expense.manager.ui.home.budget.plan

import android.app.Dialog
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
import com.kindsundev.expense.manager.databinding.DialogCreatePlanBinding
import com.kindsundev.expense.manager.utils.formatInputCurrencyBalance
import com.kindsundev.expense.manager.utils.showMessage

class CreatePlanDialog : DialogFragment() {
    private var _binding: DialogCreatePlanBinding? = null
    private val binding get() = _binding

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
        initListener()
        return binding!!.root
    }

    private fun initListener() {
        binding!!.btnCancel.setOnClickListener { dialog!!.dismiss() }
        binding!!.btnCreate.setOnClickListener { onClickCreatePlan() }

    }

    private fun onClickCreatePlan() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}