package com.kindsundev.expense.manager.ui.home.bag.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.data.model.PlanModel
import com.kindsundev.expense.manager.databinding.LayoutNotificationPlanItemBinding

class NotificationAdapter(
    private val plans: ArrayList<PlanModel>,
    private val listener: NotificationContract.Listener
) : RecyclerView.Adapter<NotificationAdapter.PlanViewHolder>() {

    inner class PlanViewHolder(val binding: LayoutNotificationPlanItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        val view = LayoutNotificationPlanItemBinding.inflate(LayoutInflater.from(parent.context))
        return PlanViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        val plan = plans[position]
        if (plan.isDone == true) {
            initNotificationPlanDone(holder.binding, plan)
        }
        if (plan.isNearDueDate == true) {
            initNotificationPlanNearDueDate(holder.binding, plan)
        }
        holder.binding.root.setOnClickListener {
            val progress = "Estimated: ${plan.currentBalance}/${plan.estimatedAmount}\n"
            val dateStartArr = plan.startDate!!.split(",")
            val dateEndArr = plan.endDate!!.split(",")
            val date = "Date: ${dateStartArr[1]}/${dateEndArr[1]}"
            val message = progress + date
            listener.onClickNotificationItem(message)
        }
    }

    private fun initNotificationPlanDone(
        binding: LayoutNotificationPlanItemBinding,
        plan: PlanModel
    ) {
        binding.ivIconPlan.setImageResource(R.drawable.ic_event_available)
        binding.tvName.text = plan.name
        binding.tvMessage.text =
            binding.root.context.getString(R.string.notification_message_success)
    }

    private fun initNotificationPlanNearDueDate(
        binding: LayoutNotificationPlanItemBinding,
        plan: PlanModel
    ) {
        binding.ivIconPlan.setImageResource(R.drawable.ic_alarm)
        binding.tvName.text = plan.name
        val message =
            binding.root.context.getString(R.string.notification_message_near_due_date) + " " +
                    plan.endDate.toString()
        binding.tvMessage.text = message
    }

    override fun getItemCount(): Int = plans.size
}
