package com.kindsundev.expense.manager.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlannedModel(
    val date: String? = "",
    val plan: PlanModel? = PlanModel()
) : Parcelable