package com.kakaomapalarm.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

class AlarmIntentManager {

    fun setAlarm(context: Context) {
        val alarmIntent = Intent("com.kakaomapalarm.ALARM_START")
        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(context, 1, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP, 0 ,pendingIntent)
    }
}