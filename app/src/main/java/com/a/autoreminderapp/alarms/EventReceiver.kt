package com.a.autoreminderapp.alarms

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.a.autoreminderapp.R

// класс-получатель (cлушатель) отложенных напоминаний о событиях
class EventReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val notification_id = System.currentTimeMillis().toInt()

        val eventName = intent!!.getStringExtra("eventName")
        val eventDescription = intent!!.getStringExtra("eventDescription")
        val eventDate = intent!!.getStringExtra("eventDate")
        val eventTime = intent!!.getStringExtra("eventTime")

        val notificationManager =
            context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val mBuilder = NotificationCompat.Builder(context, "notify")
        mBuilder.setContentTitle("$eventName")
        mBuilder.setContentText("$eventDescription\n$eventDate $eventTime")
        mBuilder.setSmallIcon(R.drawable.ic_repair)
        mBuilder.priority = Notification.PRIORITY_HIGH
        mBuilder.build().flags = Notification.FLAG_NO_CLEAR or Notification.PRIORITY_HIGH


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "channel_id"
            val channel = NotificationChannel(
                channelId,
                "notifications_channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.enableVibration(true)
            notificationManager.createNotificationChannel(channel)
            mBuilder.setChannelId(channelId)
        }

        val notification = mBuilder.build()
        notificationManager.notify(notification_id, notification)
    }
}