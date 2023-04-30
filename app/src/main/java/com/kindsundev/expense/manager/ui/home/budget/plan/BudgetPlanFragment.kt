package com.kindsundev.expense.manager.ui.home.budget.plan

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.databinding.FragmentBudgetPlanBinding
import com.kindsundev.expense.manager.utils.showMessage

class BudgetPlanFragment : Fragment() {
    private var _binding: FragmentBudgetPlanBinding? = null
    private val binding get() = _binding
    private val args: BudgetPlanFragmentArgs by navArgs()

    fun getCurrentContext(): Context = requireContext()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBudgetPlanBinding.inflate(layoutInflater)
        initListener()
        checkAndActionAsRequired()
        return binding!!.root
    }

    private fun initListener() {
        binding!!.btnBack.setOnClickListener { it.findNavController().popBackStack() }
        binding!!.btnAdd.setOnClickListener { onClickCreateSpendingPlan() }
    }

    private fun onClickCreateSpendingPlan() {
        val dialog = CreatePlanDialog()
        dialog.show(parentFragmentManager, dialog.tag)
    }

    private fun checkAndActionAsRequired() {
        val message: String
        when (args.keyAction) {
            Constant.ACTION_CREATE_SPENDING_PLAN -> {
                onClickCreateSpendingPlan()
            }
            Constant.ACTION_UPDATE_SPENDING_PLAN -> {
                message = getCurrentContext().getString(R.string.request_select_plan_update)
                showNotificationRequired(message)
            }
            else -> {
                message = getCurrentContext().getString(R.string.request_select_plan_delete)
                showNotificationRequired(message)
            }
        }
    }


    private fun showNotificationRequired(message: String) {
        val snackBar = Snackbar.make(
            binding!!.coordinatorBudgetPlanContainer,
            message, Snackbar.LENGTH_LONG
        )
        snackBar.setAction("OK") {
            snackBar.dismiss()
        }
        snackBar.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}