package com.russvkm.askacharya.models

import android.os.Parcel
import android.os.Parcelable

data class Pandits (val name:String="",
                    val image:String="",
                    val specialization:String=""
):Parcelable {
  constructor(parcel: Parcel) : this(
    parcel.readString()!!,
    parcel.readString()!!,
    parcel.readString()!!
  ) {
  }

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeString(name)
    parcel.writeString(image)
    parcel.writeString(specialization)
  }

  override fun describeContents(): Int {
    return 0
  }

  companion object CREATOR : Parcelable.Creator<Pandits> {
    override fun createFromParcel(parcel: Parcel): Pandits {
      return Pandits(parcel)
    }

    override fun newArray(size: Int): Array<Pandits?> {
      return arrayOfNulls(size)
    }
  }
}