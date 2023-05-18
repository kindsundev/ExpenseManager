package com.kindsundev.expense.manager.ui.home.budget.plan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kindsundev.expense.manager.data.model.PlanModel
import com.kindsundev.expense.manager.databinding.LayoutPlanItemBinding
import com.kindsundev.expense.manager.utils.abbreviateNumber

class BudgetPlanAdapter(
    private val dataMap: HashMap<String, PlanModel>,
    private val listener: BudgetPlanContract.Listener
) : RecyclerView.Adapter<BudgetPlanAdapter.PlanViewHolder>() {

    inner class PlanViewHolder(val binding: LayoutPlanItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        val view = LayoutPlanItemBinding.inflate(LayoutInflater.from(parent.context))
        return PlanViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        val keys = dataMap.keys.toList()
        val currentKey = keys[position]
        val plan = dataMap[currentKey]

        initDataToPlan(holder.binding, plan!!)
        // set percentage..
        holder.binding.root.setOnClickListener {
            listener.onClickPlanItem(currentKey, plan)
        }
    }

    private fun initDataToPlan(binding: LayoutPlanItemBinding, plan: PlanModel) {
        binding.tvPlanName.text = plan.name
        binding.tvCurrentBalance.text = abbreviateNumber(plan.currentBalance!!)
        binding.tvEstimateAmount.text = abbreviateNumber(plan.estimatedAmount!!)
    }

    override fun getItemCount(): Int = dataMap.size
}