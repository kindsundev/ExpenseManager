package com.kindsundev.expense.manager.ui.home.menu

import com.kindsundev.expense.manager.data.firebase.UserFirebase
import com.kindsundev.expense.manager.data.model.UserModel

class MenuPresenter(
    private val  view: MenuContract.View
) : MenuContract.Presenter {

    override fun getCurrentUser(): UserModel {
        val userAuth =  UserFirebase().user
        val id = userAuth?.uid
        val photoUri = userAuth?.photoUrl
        val name = userAuth?.displayName
        val email = userAuth?.email
        val phoneNumber = userAuth?.phoneNumber
        return UserModel(id, photoUri, name, email, phoneNumber)
    }
}