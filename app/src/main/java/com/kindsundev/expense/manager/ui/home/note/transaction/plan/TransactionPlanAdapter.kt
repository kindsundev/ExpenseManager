package com.kindsundev.expense.manager.ui.home.note.transaction.plan

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kindsundev.expense.manager.data.model.PlanModel
import com.kindsundev.expense.manager.data.model.PlannedModel
import com.kindsundev.expense.manager.databinding.LayoutTransactionPlanItemBinding

class TransactionPlanAdapter(
    private val plans: ArrayList<PlannedModel>,
    private val listener: TransactionPlanContract.Listener
) : RecyclerView.Adapter<TransactionPlanAdapter.PlanViewHolder>(){

    inner class PlanViewHolder(val binding: LayoutTransactionPlanItemBinding) :
            RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        val view = LayoutTransactionPlanItemBinding.inflate(LayoutInflater.from(parent.context))
        return PlanViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        val planned = plans[position]
        planned.plan?.let {
            updateDataToUi(holder.binding, it)
            holder.binding.root.setOnClickListener {
                listener.onClickPlanItem(planned)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateDataToUi(binding: LayoutTransactionPlanItemBinding, plan: PlanModel) {
        binding.tvName.text = plan.name
        val startDate = plan.startDate!!.split(",")
        val endDate = plan.endDate!!.split(",")
        binding.tvDate.text = "Date: ${startDate[1]} - ${endDate[1]}"
    }

    override fun getItemCount(): Int = plans.size
}