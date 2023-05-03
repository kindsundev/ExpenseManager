package com.kindsundev.expense.manager.data.firebase

import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.data.base.BaseFirebase
import com.kindsundev.expense.manager.data.model.PlanModel
import com.kindsundev.expense.manager.utils.getCurrentDate
import io.reactivex.Completable

class PlanFirebase: BaseFirebase() {

    fun upsertPlan(walletId: Int, plan: PlanModel) =
        Completable.create { emitter ->
            initPointerGeneric()
                .child(walletId.toString())
                .child(Constant.MY_REFERENCE_CHILD_PLANS)
                .child(getCurrentDate())
                .child(plan.id.toString())
                .setValue(plan)
                .addOnCompleteListener {
                    if (!emitter.isDisposed) {
                        if (it.isSuccessful) {
                            emitter.onComplete()
                        } else {
                            emitter.onError(it.exception!!)
                        }
                    }
                }
        }

}