package com.kindsundev.expense.manager.data.model

import android.os.Parcel
import android.os.Parcelable

data class WalletModel(
    val id: Int? = 0,
    val name: String? = "",
    val currency: String? = "",
    val origin: Double? = 0.0,
    val balance: Double? = 0.0,
    val transaction: TransactionModel? = null
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readParcelable(TransactionModel::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(name)
        parcel.writeString(currency)
        parcel.writeValue(origin)
        parcel.writeValue(balance)
        parcel.writeParcelable(transaction, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WalletModel> {
        override fun createFromParcel(parcel: Parcel): WalletModel {
            return WalletModel(parcel)
        }

        override fun newArray(size: Int): Array<WalletModel?> {
            return arrayOfNulls(size)
        }
    }

}