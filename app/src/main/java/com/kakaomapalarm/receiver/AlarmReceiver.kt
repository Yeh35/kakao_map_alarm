package com.kakaomapalarm.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kakaomapalarm.db.AppDatabase
import com.kakaomapalarm.db.entity.AlarmEntity
import com.kakaomapalarm.utils.AlarmIntentManager
import com.kakaomapalarm.views.AlarmActivity
import java.util.*
import java.util.concurrent.Executors

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val alarmId: Long = intent!!.getLongExtra("id", -1)

        if (alarmId == -1L) {
            return
        }

        val ioExecutor = Executors.newSingleThreadExecutor()
        ioExecutor.execute {
            val db = AppDatabase.getInstance(context!!)
            val alarm: AlarmEntity = db!!.alarmDao().selectById(alarmId)

            if (alarm == null) {
                return@execute
            }

            //알람이 수정된 경우에 어떻게 할 것인지.
            val intent = Intent(context, AlarmActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("name", alarm.name)
            intent.putExtra("vibration", alarm.is_vibration)
            context.startActivity(intent)
        }
    }
}