package com.kakaomapalarm.views

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import com.example.kakaomapalarm.R
import kotlinx.android.synthetic.main.activity_alarm.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors

class AlarmActivity : AppCompatActivity() {

    private var is_action = false
    private val vibrator: Vibrator by lazy {
        getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        val name = intent.getStringExtra("name")
        tv_name.text = name
    }

    override fun onStart() {
        super.onStart()

        if (intent.getBooleanExtra("vibration", false)) {
            val pattern: LongArray = longArrayOf(1000, 50, 2000, 50)
            val effect: VibrationEffect = VibrationEffect.createWaveform(pattern, 0)
            this.vibrator.vibrate(effect)
        }

        is_action = true
        val ioExecutor = Executors.newSingleThreadExecutor()
        ioExecutor.execute {

            val format:SimpleDateFormat = SimpleDateFormat("HH시 mm분 ss초")
            val calendar: Calendar = Calendar.getInstance()
            calendar.time.time = System.currentTimeMillis()

            while (is_action) {

                runOnUiThread {
                    tv_time.text = format.format(calendar.time)
                }

                calendar.add(Calendar.SECOND, 1)
                //1초 대기
                SystemClock.sleep(1000)

            }
        }
    }

    override fun onStop() {
        super.onStop()
        this.vibrator.cancel()
        is_action = false
    }

    fun onClickForOk(view: View) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("daummaps://open?page=placeSearch"))
        startActivity(intent)
        finish()
    }
}
