package com.russvkm.askacharya.models

import android.os.Parcel
import android.os.Parcelable

data class Images(val imageOne:String="",
                  val imageTwo:String="",
                  val imageThree:String=""
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(imageOne)
        parcel.writeString(imageTwo)
        parcel.writeString(imageThree)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Images> {
        override fun createFromParcel(parcel: Parcel): Images {
            return Images(parcel)
        }

        override fun newArray(size: Int): Array<Images?> {
            return arrayOfNulls(size)
        }
    }
}