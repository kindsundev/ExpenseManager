package com.kindsundev.expense.manager.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlanModel(
    val id: Int? = 0,
    val walletId: Int? = 0,
    val name: String? = "",
    val estimatedAmount: Double? = 0.0,
    val startDate: String? = "",
    val endDate: String? = "",
    val isWorking: Boolean? = true,
    val isSuccess: Boolean? = false
) : Parcelable