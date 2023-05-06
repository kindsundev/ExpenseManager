package com.kindsundev.expense.manager.ui.home.budget.plan.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.data.model.PlanModel
import com.kindsundev.expense.manager.databinding.FragmentBudgetPlanDetailBinding
import com.kindsundev.expense.manager.utils.formatDisplayCurrencyBalance
import com.kindsundev.expense.manager.utils.showMessage
import kotlin.properties.Delegates

class BudgetPlanDetailFragment : Fragment() {
    private var _binding: FragmentBudgetPlanDetailBinding? = null
    private val binding get() = _binding
    private var isLayoutInfoVisible = true

    private val args by navArgs<BudgetPlanDetailFragmentArgs>()
    private var mWalletId by Delegates.notNull<Int>()
    private lateinit var mPlan: PlanModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBudgetPlanDetailBinding.inflate(layoutInflater)
        binding!!.toolbar.inflateMenu(R.menu.plan_detail_menu)
        mPlan = args.plan
        mWalletId = args.walletId
        initPlanData()
        initListener()
        return binding!!.root
    }

    private fun initPlanData() {
        binding!!.tvName.text = mPlan.name
        binding!!.tvCurrentBalance.text = formatDisplayCurrencyBalance(mPlan.currentBalance.toString())
        binding!!.tvEstimatedAmount.text = formatDisplayCurrencyBalance(mPlan.estimatedAmount.toString())

        val startDay = mPlan.startDate!!.split(", ")
        val endDay = mPlan.endDate!!.split(", ")
        binding!!.tvStartDate.text = startDay[1]
        binding!!.tvEndDate.text = endDay[1]
    }

    private fun initListener() {
        binding!!.btnBack.setOnClickListener {it.findNavController().popBackStack() }
        binding!!.iBtnVisibility.setOnClickListener { toggleLayoutInfo()}
        binding!!.toolbar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId) {
                R.id.action_update_plan -> {
                    initPlanDetailBottomSheet()
                    true
                }
                R.id.action_delete_plan -> {
                    initDeletePlanDialog()
                    true
                }
                else -> false
            }
        }
    }

    private fun toggleLayoutInfo() {
        if (isLayoutInfoVisible) {
            binding!!.iBtnVisibility.setImageResource(R.drawable.ic_visibility)
            binding!!.cardViewDetail.visibility = View.GONE
            isLayoutInfoVisible = false
        } else {
            binding!!.iBtnVisibility.setImageResource(R.drawable.ic_visibility_off)
            binding!!.cardViewDetail.visibility = View.VISIBLE
            isLayoutInfoVisible = true
        }
    }

    private fun initPlanDetailBottomSheet() {
        activity?.showMessage("Update button clicked")
    }

    private fun initDeletePlanDialog() {
        activity?.showMessage("Delete button clicked")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}