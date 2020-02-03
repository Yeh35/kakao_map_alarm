package com.kakaomapalarm.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.animation.AnimationUtils
import com.example.kakaomapalarm.R
import com.kakaomapalarm.db.AppDatabase
import com.kakaomapalarm.utils.AlarmIntentManager
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity : AppCompatActivity()
{
    private val icon_animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.intro_animation)
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        icon_animation.isFillEnabled = false;
        img_mapIcon.startAnimation(icon_animation);

        img_mapIcon.setOnClickListener {
            img_mapIcon.startAnimation(icon_animation);
        }

        val introBackThread = IntroBackThread(this)
        introBackThread.start()

        AlarmIntentManager.getInstance().setAlarmAll(this)
    }

    private class IntroBackThread(activity:IntroActivity) : Thread()
    {
        private val parentActivity:IntroActivity = activity

        override fun run()
        {

            val db= AppDatabase.getInstance(parentActivity)
            if (db == null) {
                parentActivity.runOnUiThread(java.lang.Runnable {
                    parentActivity.finish()
                })

                return
            }

            SystemClock.sleep(2000)

            parentActivity.runOnUiThread(java.lang.Runnable {
                val intent = Intent(parentActivity, MainActivity::class.java)
                parentActivity.startActivity(intent)
                parentActivity.finish()
            })
        }
    }
}
