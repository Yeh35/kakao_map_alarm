package com.kakaomapalarm.receiver

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kakaomapalarm.utils.AlarmIntentManager
import com.kakaomapalarm.views.MainActivity


class AlarmAutoRunReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent!!.action.equals("android.intent.action.BOOT_COMPLETED")) {
            AlarmIntentManager.getInstance().setAlarmAll(context!!)
        }
    }

}