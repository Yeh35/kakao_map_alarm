package com.kakaomapalarm.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import com.kakaomapalarm.db.AppDatabase
import com.kakaomapalarm.db.entity.AlarmEntity
import com.kakaomapalarm.utils.AlarmIntentManager
import com.kakaomapalarm.views.AlarmActivity
import java.util.*
import java.util.concurrent.Executors

class AlarmReceiver : BroadcastReceiver() {

    var context: Context? = null

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

            // 시스템에서 powermanager 받아옴
            val pm: PowerManager = context!!.getSystemService(Context.POWER_SERVICE) as PowerManager

            // 객체의 제어레벨 설정
            val sCpuWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP, "app:alarm")

            //acquire 함수를 실행하여 앱을 깨운다. cpu 를 획득한다
            sCpuWakeLock.acquire(60*1000L /*1 minutes*/)

            this.context = context

            //알람이 호출
            val intent = Intent(context, AlarmActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("name", alarm.name)
            intent.putExtra("vibration", alarm.is_vibration)

            this.context!!.startActivity(intent)

            sCpuWakeLock.release()
        }
    }
}