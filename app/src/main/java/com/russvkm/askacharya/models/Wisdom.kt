package com.russvkm.askacharya.models

import android.os.Parcel
import android.os.Parcelable

data class Wisdom (val articleTitle:String="",
                   val articleImage:String="",
                   val article:String="",
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(articleTitle)
        parcel.writeString(articleImage)
        parcel.writeString(article)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Wisdom> {
        override fun createFromParcel(parcel: Parcel): Wisdom {
            return Wisdom(parcel)
        }

        override fun newArray(size: Int): Array<Wisdom?> {
            return arrayOfNulls(size)
        }
    }
}