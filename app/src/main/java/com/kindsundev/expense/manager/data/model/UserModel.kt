package com.kindsundev.expense.manager.data.model

import java.io.Serializable

data class UserModel(
    var id: String?,
    var photoUrl: String?,
    var name: String?,
    var email: String?,
    var phoneNumber: String?
) : Serializable