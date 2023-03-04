package com.kindsundev.expense.manager.ui.home

import com.kindsundev.expense.manager.data.firebase.UserFirebase
import com.kindsundev.expense.manager.data.model.UserModel

class HomePresenter(
    val view: HomeContract.View
) : HomeContract.Presenter {

    override fun getCurrentUser(): UserModel {
        val userAuth =  UserFirebase().user
        val id = userAuth?.getIdToken(true).toString()
        val photoUri = userAuth?.photoUrl.toString()
        val name = userAuth?.displayName
        val email = userAuth?.email
        val phoneNumber = userAuth?.phoneNumber
        return UserModel(id, photoUri, name, email, phoneNumber)
    }
}