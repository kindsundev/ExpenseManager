package com.kindsundev.expense.manager.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlanModel(
    val id: Int? = 0,
    var name: String? = "",
    var estimatedAmount: Double? = 0.0,
    var startDate: String? = "",
    var endDate: String? = "",
    val currentBalance: Double? = 0.0,
    var isNearDueDate: Boolean? = false,
    var isDone: Boolean? = false
) : Parcelable