package com.kindsundev.expense.manager.ui.home.budget.plan.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kindsundev.expense.manager.databinding.FragmentBudgetPlanDetailBinding
import com.kindsundev.expense.manager.ui.home.HomeActivity
import com.kindsundev.expense.manager.utils.toggleBottomNavigation

class BudgetPlanDetailFragment : Fragment() {
    private var _binding: FragmentBudgetPlanDetailBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBudgetPlanDetailBinding.inflate(layoutInflater)
        toggleBottomNavigation(requireActivity() as HomeActivity, false)

        return binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}