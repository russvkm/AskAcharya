package com.russvkm.askacharya.utils

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.russvkm.askacharya.R
import com.russvkm.askacharya.activity.MainActivity

class NotificationClass(private val context: Context, private val activity:Activity) {
    fun saveAllNotificationPreference(notificationCode:Int,prefName:String,key:String){
        val sharedPreference=context.getSharedPreferences(prefName,Context.MODE_PRIVATE)
        val editor:SharedPreferences.Editor=sharedPreference.edit()
        editor.putInt(key,notificationCode)
        editor.apply()
    }

    fun fetchAllNotificationPreference(prefName:String?,key:String?):Int{
        val sharedPreferences=context.getSharedPreferences(prefName,Context.MODE_PRIVATE)
        return sharedPreferences.getInt(key,0)
    }

    fun fcmPassedPreference(fcmPassed:Boolean){
        val sharedPreference=context.getSharedPreferences("fcmPassed",Context.MODE_PRIVATE)
        val editor:SharedPreferences.Editor=sharedPreference.edit()
        editor.putBoolean("fcm_state",fcmPassed)
        editor.apply()
    }

    fun isFcmPassed():Boolean{
        val sharedPreferences=context.getSharedPreferences("fcmPassed",Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("fcm_state",false)
    }

    fun configuringNotificationChannel(){

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val name=context.getString(R.string.wisdom_notification)
            val descriptionText=context.getString(R.string.channel_description)
            val importance= NotificationManager.IMPORTANCE_DEFAULT
            val mChannel= NotificationChannel(CHANNEL_ID,name,importance)
            mChannel.description=descriptionText
            val notificationManager = activity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    fun  displayingNotification(title:String,description:String,drawable:Int){
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        val soundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(drawable)
            .setContentTitle(title)
            .setContentText(description)
            .setAutoCancel(true)
            .setSound(soundUri)
            .setWhen(System.currentTimeMillis())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, builder.build())
        }
    }

    companion object{
        const val CHANNEL_ID="russvkm"
        const val notificationId=3
    }
}