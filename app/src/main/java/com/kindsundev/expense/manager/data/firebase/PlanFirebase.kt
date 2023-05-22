package com.kindsundev.expense.manager.data.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.data.base.BaseFirebase
import com.kindsundev.expense.manager.data.model.PlanModel
import com.kindsundev.expense.manager.data.model.PlannedModel
import com.kindsundev.expense.manager.utils.getCurrentDate
import io.reactivex.Completable
import io.reactivex.Observable

class PlanFirebase: BaseFirebase() {

    private fun initPointerPlan(walletId: Int) =
        initPointerGeneric()
            .child(walletId.toString())
            .child(Constant.MY_REFERENCE_CHILD_PLANS)


    fun insertPlan(walletId: Int, plan: PlanModel) =
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

    fun updatePlan(walletId: Int, dateKey: String, plan: PlanModel) =
        Completable.create { emitter ->
            initPointerPlan(walletId)
                .child(dateKey)
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

    fun updateBalance(walletId: Int, dateKey: String, planId: Int, balance: Double) =
        Completable.create { emitter ->
            initPointerPlan(walletId)
                .child(dateKey)
                .child(planId.toString())
                .child(Constant.REF_FIELD_CURRENT_BALANCE_OF_PLAN)
                .setValue(balance)
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

    fun getPlanMap(walletId: Int): Observable<PlannedModel> =
        Observable.create { subscriber ->
            initPointerPlan(walletId).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if ((!subscriber.isDisposed) && (snapshot.hasChildren())) {
                        for (data in snapshot.children) {
                            for (child in data.children) {
                                val value = child.getValue(PlanModel::class.java)
                                val planned = PlannedModel(child.key, value)
                                subscriber.onNext(planned)
                            }
                        }
                        subscriber.onComplete()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    subscriber.onError(error.toException())
                }
            })
        }


    fun deletePlan(walletId: Int, dateKey: String, planId: Int) =
        Completable.create { emitter ->
            initPointerPlan(walletId)
                .child(dateKey)
                .child(planId.toString())
                .removeValue()
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

    fun getPlan(walletId: Int, dateKey: String, planId: Int): Observable<PlanModel> =
        Observable.create { subscriber ->
            initPointerPlan(walletId)
                .child(dateKey)
                .child(planId.toString())
                .addValueEventListener(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if ((!subscriber.isDisposed) && (snapshot.hasChildren())) {
                            val data = snapshot.getValue(PlanModel::class.java)
                            data?.let {
                                subscriber.onNext(data)
                                subscriber.onComplete()
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        subscriber.onError(error.toException())
                    }
                })
        }
}