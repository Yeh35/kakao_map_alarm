package com.kakaomapalarm.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.kakaomapalarm.db.AppDatabase
import com.kakaomapalarm.db.entity.AlarmEntity
import java.util.*

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
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = Date()

        val db: AppDatabase? = AppDatabase.getInstance(context)
        val alarmList: List<AlarmEntity> = db!!.alarmDao().selectAll()

        for (alarm: AlarmEntity in alarmList) {
            for (dayOfWeek: Char in alarm.day_of_the_week) {
                var dayOfWeekIndex: Int = DateUtils.getDayOfWeekIndex(dayOfWeek)

                if (dayOfWeekIndex ==  calendar.get(Calendar.DAY_OF_WEEK)) {
                    calendar.time = Date(alarm.time)
                    val hour = calendar.get(Calendar.HOUR_OF_DAY)
                    val minute = calendar.get(Calendar.MINUTE)

                    // 알람 시간 세팅
                    calendar.time = Date()
                    calendar.set(Calendar.HOUR_OF_DAY, hour)
                    calendar.set(Calendar.MINUTE, minute)

                    val alarmIntent = Intent("com.kakaomapalarm.ALARM_START")
                    alarmIntent.putExtra("id", alarm.id)

                    val pendingIntent: PendingIntent = PendingIntent.getBroadcast(context, 1, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)

                    val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
                }
            }
        }
    }

    fun setAlarm(context: Context, id: Long) {
        val alarmIntent = Intent("com.kakaomapalarm.ALARM_START")
        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(context, 1, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP, 0,pendingIntent)
    }
}