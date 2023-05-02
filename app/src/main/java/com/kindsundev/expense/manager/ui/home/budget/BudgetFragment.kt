package com.kindsundev.expense.manager.ui.home.budget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kindsundev.expense.manager.databinding.FragmentBudgetBinding
import com.kindsundev.expense.manager.utils.requestPremium

class BudgetFragment : Fragment() {
    private var _binding: FragmentBudgetBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBudgetBinding.inflate(inflater, container, false)
        initListener()

        return binding!!.root
    }

    private fun initListener() {
        binding!!.wallet.btnWalletManager.setOnClickListener {
            findNavController().navigate(
                BudgetFragmentDirections.actionBudgetFragmentToBudgetWalletFragment()
            )
        }
        binding!!.plan.btnPlanManager.setOnClickListener {
            findNavController().navigate(
                BudgetFragmentDirections.actionBudgetFragmentToBudgetPlanFragment()
            )
        }
        binding!!.debt.btnDebtManager.setOnClickListener {
            activity?.requestPremium()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}