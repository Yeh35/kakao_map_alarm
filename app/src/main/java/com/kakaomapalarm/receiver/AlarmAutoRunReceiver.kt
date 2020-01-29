package com.kakaomapalarm.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kakaomapalarm.views.MainActivity


class AlarmAutoRunReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent!!.action.equals("android.intent.action.BOOT_COMPLETED")) {
//            val it = Intent(context, MainActivity::class.java)
//            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            context.startActivity(it)
        }
    }

}