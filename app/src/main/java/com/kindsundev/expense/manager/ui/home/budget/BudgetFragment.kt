package com.kindsundev.expense.manager.ui.home.budget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.databinding.FragmentBudgetBinding
import com.kindsundev.expense.manager.utils.requestPremium

class BudgetFragment : Fragment() {
    private var _binding : FragmentBudgetBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBudgetBinding.inflate(inflater, container, false)
        initWalletListener()
        initPlannedListener()
        initDebtListener()
        initSavingsListener()
        return binding!!.root
    }

    private fun initWalletListener() {
        binding!!.wallet.tvCreateWallet.setOnClickListener {
            startBudgetWalletFragment(Constant.ACTION_CREATE_WALLET)
        }
        binding!!.wallet.tvUpdateWallet.setOnClickListener {
            startBudgetWalletFragment(Constant.ACTION_UPDATE_WALLET)
        }
        binding!!.wallet.tvDeleteWallet.setOnClickListener {
            startBudgetWalletFragment(Constant.ACTION_DELETE_WALLET)
        }
    }

    private fun startBudgetWalletFragment(action : String) {
        findNavController().navigate(
            BudgetFragmentDirections.actionBudgetFragmentToBudgetWalletFragment(action)
        )
    }

    private fun initPlannedListener() {
        binding!!.planned.tvCreatePlan.setOnClickListener {
            startBudgetPlanFragment(Constant.ACTION_CREATE_SPENDING_PLAN)
        }
        binding!!.planned.tvUpdatePlan.setOnClickListener {
            startBudgetPlanFragment(Constant.ACTION_UPDATE_SPENDING_PLAN)
        }
        binding!!.planned.tvDeletePlan.setOnClickListener {
            startBudgetPlanFragment(Constant.ACTION_DELETE_SPENDING_PLAN)
        }
    }

    private fun startBudgetPlanFragment(action : String) {
        findNavController().navigate(
            BudgetFragmentDirections.actionBudgetFragmentToBudgetPlanFragment(action)
        )
    }

    private fun initDebtListener() {
        binding!!.debt.tvCreateDebt.setOnClickListener { activity?.requestPremium() }
        binding!!.debt.tvUpdateDebt.setOnClickListener { activity?.requestPremium() }
        binding!!.debt.tvDeleteDebt.setOnClickListener { activity?.requestPremium() }
    }

    private fun initSavingsListener() {
        binding!!.savings.tvCreateSavings.setOnClickListener { activity?.requestPremium() }
        binding!!.savings.tvUpdateSavings.setOnClickListener { activity?.requestPremium() }
        binding!!.savings.tvDeleteSavings.setOnClickListener { activity?.requestPremium() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}