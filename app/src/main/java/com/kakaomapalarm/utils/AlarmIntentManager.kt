package com.kakaomapalarm.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.kakaomapalarm.db.AppDatabase
import com.kakaomapalarm.db.entity.AlarmEntity
import com.kakaomapalarm.receiver.AlarmReceiver
import java.util.*
import java.util.concurrent.Executors

class AlarmIntentManager {

    companion object {
        private var instance: AlarmIntentManager? = null

        fun getInstance(): AlarmIntentManager {
            if (instance == null) {
                instance = AlarmIntentManager()
            }

            return instance!!
        }
    }

    fun setAlarmAll(context: Context) {
        val ioExecutor = Executors.newSingleThreadExecutor()
        ioExecutor.execute {
            val db: AppDatabase? = AppDatabase.getInstance(context)
            val alarmList: List<AlarmEntity> = db!!.alarmDao().selectAll()

            for (alarm: AlarmEntity in alarmList) {
                var alarmDate: Date = AlarmUtils.getNextAlarmTime(alarm)

                val calendar: Calendar = Calendar.getInstance()
                calendar.time = alarmDate

//                val alarmIntent = Intent("com.kakaomapalarm.ALARM_START")
                val alarmIntent = Intent(context, AlarmReceiver::class.java)
//                alarmIntent.setClass(context, AlarmReceiver::class.java)
                alarmIntent.putExtra("id", alarm.id)
                alarmIntent.putExtra("name", alarm.name)

                val pendingIntent: PendingIntent = PendingIntent.getBroadcast(context, 1, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)

                val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            }
        }
    }

    fun setAlarm(context: Context, id: Long) {
        val ioExecutor = Executors.newSingleThreadExecutor()
        ioExecutor.execute {
            val db: AppDatabase? = AppDatabase.getInstance(context)
            val alarm: AlarmEntity = db!!.alarmDao().selectById(id)
            var alarmDate: Date = AlarmUtils.getNextAlarmTime(alarm)

            val calendar: Calendar = Calendar.getInstance()
            calendar.time = alarmDate

//            val alarmIntent = Intent("com.kakaomapalarm.ALARM_START")
            val alarmIntent = Intent(context, AlarmReceiver::class.java)
//            alarmIntent.setClass(context, AlarmReceiver::class.java)
            alarmIntent.putExtra("id", alarm.id)
            alarmIntent.putExtra("name", alarm.name)

            val pendingIntent: PendingIntent = PendingIntent.getBroadcast(context, 1, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT)

            val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        }
    }
}