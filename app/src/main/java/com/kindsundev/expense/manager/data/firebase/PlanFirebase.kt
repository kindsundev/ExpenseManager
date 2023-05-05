package com.kindsundev.expense.manager.data.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.data.base.BaseFirebase
import com.kindsundev.expense.manager.data.model.PlanModel
import com.kindsundev.expense.manager.utils.getCurrentDate
import io.reactivex.Completable
import io.reactivex.Observable

class PlanFirebase: BaseFirebase() {

    private fun initPointerPlan(walletId: Int) =
        initPointerGeneric()
            .child(walletId.toString())
            .child(Constant.MY_REFERENCE_CHILD_PLANS)


    fun upsertPlan(walletId: Int, plan: PlanModel) =
        Completable.create { emitter ->
            initPointerPlan(walletId)
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

    fun getPlanList(walletId: Int): Observable<PlanModel> =
        Observable.create { subscriber ->
            initPointerPlan(walletId).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if ((!subscriber.isDisposed) && (snapshot.hasChildren())) {
                        for (data in snapshot.children) {
                            for (child in data.children) {
                                val value = child.getValue(PlanModel::class.java)
                                value?.let {
                                    subscriber.onNext(value)
                                }
                            }
                        }
                        subscriber.onComplete()
                    } else {
                        subscriber.onComplete()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    subscriber.onError(error.toException())
                }
            })
        }
}