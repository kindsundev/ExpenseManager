package com.kindsundev.expense.manager.data.model

import android.os.Parcel
import android.os.Parcelable
import com.kindsundev.expense.manager.common.Constant

data class TransactionModel(
    val id: Int? = 0,
    val type: String? = "",
    val category: String? = "",
    val amount: Double? = 0.0,
    val date: String? = "",
    val note: String? = Constant.FAKE_DATA_NULL
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(type)
        parcel.writeString(category)
        parcel.writeValue(amount)
        parcel.writeString(date)
        parcel.writeString(note)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TransactionModel> {
        override fun createFromParcel(parcel: Parcel): TransactionModel {
            return TransactionModel(parcel)
        }

        override fun newArray(size: Int): Array<TransactionModel?> {
            return arrayOfNulls(size)
        }
    }

}