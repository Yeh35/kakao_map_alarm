package com.kakaomapalarm.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kakaomapalarm.utils.AlarmIntentManager

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent!!.action.equals("com.kakaomapalarm.ALARM_START") == false) {
            return
        }

//        AlarmIntentManager().setAlarm(this)
//            val it = Intent(context, MainActivity::class.java)
//            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            context.startActivity(it)
    }
}