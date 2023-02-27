package com.kindsundev.expense.manager.data.model

import android.net.Uri
import java.io.Serializable

data class UserModel(
    var id: String?,
    var uri: Uri?,
    var name: String?,
    var email: String?,
    var phoneNumber: String?
) : Serializable