package com.russvkm.askacharya.utils

import android.content.Context
import com.russvkm.askacharya.R
import com.russvkm.askacharya.models.Notification

class NotificationObject(private val context: Context) {
    fun totalList(): ArrayList<Notification> {
        val menuArrayList = ArrayList<Notification>()
        val menu1 = Notification(R.drawable.video_black,context.getString(R.string.new_video_notification))
        menuArrayList.add(menu1)
        val menu2=Notification(R.drawable.wisdom_black,context.getString(R.string.wisdom_notification))
        menuArrayList.add(menu2)
        val menu3=Notification(R.drawable.ask_question_black,context.getString(R.string.new_reply_notification))
        menuArrayList.add(menu3)
        return menuArrayList
    }
}