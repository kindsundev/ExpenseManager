package com.kindsundev.expense.manager.ui.home.budget.plan.dialog.create

import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.data.model.PlanModel
import com.kindsundev.expense.manager.utils.hashCodeForID
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class CreatePlanPresenter(
    val view: CreatePlanContract.View
) : CreatePlanContract.Presenter {

    override fun handleDataFromInput(
        name: String,
        amount: String,
        startDate: String,
        endDate: String,
    ): PlanModel {
        if (isValidData(name, amount, startDate, endDate)) {
            val id = hashCodeForID(name, amount, startDate, endDate)
            return PlanModel(id, name, amount.toDouble(), startDate, endDate)
        }
        return PlanModel()
    }

    private fun isValidData(
        name: String,
        amount: String,
        startDate: String,
        endDate: String,
    ): Boolean {
        val message: String
        if (name.isNotEmpty()
            && amount.isNotEmpty()
            && startDate != view.getCurrentContext().getString(R.string.start_day)
            && endDate != view.getCurrentContext().getString(R.string.end_day)
        ) {
            val amountToLarge = amount.replace(",", "").length > 15
            return when {
                amountToLarge -> {
                    message = view.getCurrentContext().getString(R.string.amount_to_large)
                    view.showMessageInvalidData(message)
                    false
                }
                !isValidDate(startDate, endDate) -> {
                    message = view.getCurrentContext().getString(R.string.please_provide_valid_date_plan)
                    view.showMessageInvalidData(message)
                    false
                }
                else -> {
                    true
                }
            }
        }
        message = view.getCurrentContext().getString(R.string.please_provide_full_data)
        view.showMessageInvalidData(message)
        return false
    }

    private fun isValidDate(startDate: String, endDate: String): Boolean {
        val sdf = SimpleDateFormat("HH:mm, dd-MM-yyyy", Locale.getDefault())
        val start = sdf.parse(startDate)
        val end = sdf.parse(endDate)
        val differenceInMillis = (end!!.time) - (start!!.time)
        val differenceInDays = TimeUnit.MILLISECONDS.toDays(differenceInMillis)
        return differenceInDays > 0
    }
}