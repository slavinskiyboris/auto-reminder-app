package com.a.autoreminderapp.alarms

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.a.autoreminderapp.data.entities.AutoEvent
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

// класс-помощник для создания отложенных напоминаний
class AlarmHelper {
    companion object {
        // функция создания отложенных напоминаний для события
        fun makeAlarmsForEvent(context: Context, autoEvent: AutoEvent) {
            makeAlarmForEvent(context, autoEvent, 0)
            makeAlarmForEvent(context, autoEvent, 1)
        }

        // функция удаления отложенных напоминаний для события
        fun removeAlarmsForEvent(context: Context, autoEvent: AutoEvent){
            removeAlarmForEvent(context, autoEvent, 0)
            removeAlarmForEvent(context, autoEvent, 1)
        }

        // функция создания отложенного напоминания для события
        private fun makeAlarmForEvent(context: Context, autoEvent: AutoEvent, additional: Int) {
            val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, EventReceiver::class.java)
            if (additional != 1) {
                intent.putExtra("eventName", autoEvent.name)
            } else {
                intent.putExtra(
                    "eventName",
                    "Не забудьте, завтра наступит событие '" + autoEvent.name + "'"
                )
            }
            intent.putExtra("eventDescription", autoEvent.description)
            intent.putExtra("eventDate", autoEvent.date)
            intent.putExtra("eventTime", autoEvent.time)
            val dateFormat: DateFormat =
                SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.getDefault())
            val eventDate: Date = dateFormat.parse(autoEvent.date + " " + autoEvent.time) as Date
            val requestCode: Long = eventDate.time + autoEvent.id + additional
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode.toInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            val calendar = Calendar.getInstance()
            calendar.time = eventDate
            calendar.add(Calendar.DATE, -additional)
            am[AlarmManager.RTC_WAKEUP, calendar.timeInMillis] = pendingIntent
        }

        // функция создания отложенного повторяющегося напоминания для пробега
        fun makeAlarmForMileageRemind(context: Context, daysInterval: Int) {
            val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, MileageRemindReceiver::class.java)
            val calendar = Calendar.getInstance()
            val requestCode: Int = 1
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
//            val dayMillis: Long = 60000
            val dayMillis = AlarmManager.INTERVAL_DAY
            am.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                dayMillis * daysInterval,
                pendingIntent
            )
        }

        // функция удаления отложенного напоминания о событии
        fun removeAlarmForEvent(context: Context, autoEvent: AutoEvent, additional: Int){
            val intent = Intent(context, EventReceiver::class.java)
            val dateFormat: DateFormat =
                SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.getDefault())
            val eventDate: Date = dateFormat.parse(autoEvent.date + " " + autoEvent.time) as Date
            val requestCode: Long = eventDate.time + autoEvent.id + additional
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode.toInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
        }

        // функция удаления отложенного напоминания о пробеге
        fun removeAlarmForMileageRemind(context: Context) {
            val intent = Intent(context, MileageRemindReceiver::class.java)
            val requestCode: Int = 1
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
        }
    }
}