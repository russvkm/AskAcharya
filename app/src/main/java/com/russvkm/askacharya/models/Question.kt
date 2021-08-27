package com.russvkm.askacharya.models

import android.os.Parcel
import android.os.Parcelable

data class Question(val name:String="",
                    val totalQuestion:ArrayList<String> = ArrayList(),
                    val title:String="",
                    val question:String="",
                    val image:String="",
                    var documentId:String="",
                    val answer:String="",
                    val fcm:String=""
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.createStringArrayList()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeStringList(totalQuestion)
        parcel.writeString(title)
        parcel.writeString(question)
        parcel.writeString(image)
        parcel.writeString(documentId)
        parcel.writeString(answer)
        parcel.writeString(fcm)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Question> {
        override fun createFromParcel(parcel: Parcel): Question {
            return Question(parcel)
        }

        override fun newArray(size: Int): Array<Question?> {
            return arrayOfNulls(size)
        }
    }
}