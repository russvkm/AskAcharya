package com.russvkm.askacharya.models

import android.os.Parcel
import android.os.Parcelable

data class AdminEmail(val email:String=""):Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString()!!)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(email)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AdminEmail> {
        override fun createFromParcel(parcel: Parcel): AdminEmail {
            return AdminEmail(parcel)
        }

        override fun newArray(size: Int): Array<AdminEmail?> {
            return arrayOfNulls(size)
        }
    }
}