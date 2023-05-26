package com.kindsundev.expense.manager.ui.home.budget.plan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kindsundev.expense.manager.data.model.PlanModel
import com.kindsundev.expense.manager.data.model.PlannedModel
import com.kindsundev.expense.manager.databinding.LayoutPlanItemBinding
import com.kindsundev.expense.manager.utils.abbreviateNumber

class BudgetPlanAdapter(
    private val plans: ArrayList<PlannedModel>,
    private val listener: BudgetPlanContract.Listener
) : RecyclerView.Adapter<BudgetPlanAdapter.PlanViewHolder>() {

    inner class PlanViewHolder(val binding: LayoutPlanItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        val view = LayoutPlanItemBinding.inflate(LayoutInflater.from(parent.context))
        return PlanViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        val planned = plans[position]
        planned.plan?.let {
            initDataToPlan(holder.binding, planned.plan)
            holder.binding.root.setOnClickListener {
                listener.onClickPlanItem(planned)
            }
        }
    }

    private fun initDataToPlan(binding: LayoutPlanItemBinding, plan: PlanModel) {
        binding.tvPlanName.text = plan.name
        binding.tvCurrentBalance.text = abbreviateNumber(plan.currentBalance!!)
        binding.tvEstimateAmount.text = abbreviateNumber(plan.estimatedAmount!!)
        val percentage = (plan.currentBalance.toDouble() / plan.estimatedAmount!!.toDouble()) * 100
        binding.progressBarPlan.progress = percentage.toInt()
    }

    override fun getItemCount(): Int = plans.size
}