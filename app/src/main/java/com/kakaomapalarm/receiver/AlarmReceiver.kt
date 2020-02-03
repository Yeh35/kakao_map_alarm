package com.kakaomapalarm.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kakaomapalarm.db.AppDatabase
import com.kakaomapalarm.db.entity.AlarmEntity
import com.kakaomapalarm.utils.AlarmIntentManager
import com.kakaomapalarm.views.AlarmActivity
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (! intent!!.action.equals("com.kakaomapalarm.ALARM_START")) {
            return
        }

        val alarmId: Long = intent.getLongExtra("id", -1)

        if (alarmId == -1L) {
            return
        }

        val db = AppDatabase.getInstance(context!!)
        val alarm: AlarmEntity = db!!.alarmDao().selectById(alarmId)

        if (alarm == null) {
            return
        }

        val calendar: Calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        if (Date().time != calendar.timeInMillis + alarm.time) {
            return
        }

        val intent = Intent(context, AlarmActivity::class.java)
        intent.putExtra("name", alarm.name)
        context.startActivity(intent)
    }
}