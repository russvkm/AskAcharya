package com.russvkm.askacharya.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.russvkm.askacharya.R
import com.russvkm.askacharya.activity.MainActivity

class NotificationService:FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(FIREBASE_MESSAGING_SERVICE,"FROM: ${remoteMessage.from}")
        remoteMessage.data.isNotEmpty().let {
            Log.d(FIREBASE_MESSAGING_SERVICE,"Message data payload: ${remoteMessage.from}")
            val title=remoteMessage.data[com.russvkm.askacharya.utils.Constants.FCM_KEY_TITLE]!!
            val message=remoteMessage.data[com.russvkm.askacharya.utils.Constants.FCM_KEY_MESSAGE]!!
            sendNotification(title,message)
        }
        remoteMessage.notification?.let {
            Log.d(FIREBASE_MESSAGING_SERVICE,"Message notification body: ${it.body}")

        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e(FIREBASE_MESSAGING_SERVICE,"Refreshed Token $token")
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String){
        //to be implement
    }

    private fun sendNotification(title:String,messageBody:String){
        val intent= run {
            Intent(this,MainActivity::class.java)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        val pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT)
        val channelId=this.resources.getString(R.string.default_notification_id)
        val defaultSoundUri=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder= NotificationCompat.Builder(
            this,channelId
        ).setSmallIcon(R.drawable.app_icon)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setSound(defaultSoundUri)
            .setWhen(System.currentTimeMillis())
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
        val notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val channel=NotificationChannel(channelId,"Default_notification_ask_acharya",
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0,notificationBuilder.build())
    }

    companion object{
        const val FIREBASE_MESSAGING_SERVICE="NOTIFICATION_SERVICE"
    }
}