package com.kakaomapalarm.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Debug
import android.view.View
import android.widget.ToggleButton
import com.example.kakaomapalarm.R
import com.kakaomapalarm.db.AppDatabase
import com.kakaomapalarm.db.entity.AlarmEntity
import kotlinx.android.synthetic.main.activity_alarm_imformation.*
import java.util.*
import java.util.concurrent.Executors


class AlarmImformation : AppCompatActivity() {

    companion object {
        const val STATE_ADD: Int = 1
        const val STATE_UPDATE: Int = 2
    }

    var state: Int = 0
    var id: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_imformation)
        tp_time.setIs24HourView(true)

        this.state = intent.getIntExtra("state", 0)


        when (state) {
            STATE_ADD -> {
                val calendar = Calendar.getInstance()
                tp_time.hour = calendar.get(Calendar.HOUR_OF_DAY)
                tp_time.minute = calendar.get(Calendar.MINUTE)
            }
            STATE_UPDATE -> {
                this.id = intent.getLongExtra("id", 0)
                val ioExecutor = Executors.newSingleThreadExecutor()
                ioExecutor.execute {
                    val alarm: AlarmEntity = AppDatabase.getInstance(this)!!.alarmDao().selectById(this.id)

                    runOnUiThread {
                        val calendar = Calendar.getInstance()
                        calendar.time = Date(alarm.time)
                        tp_time.hour = calendar.get(Calendar.HOUR_OF_DAY)
                        tp_time.minute = calendar.get(Calendar.MINUTE)

                        et_name.setText(alarm.name)
                        sw_vibration.isChecked = alarm.is_vibration

                        for (dayOfTheWeek: Char in alarm.day_of_the_week) {
                            when (dayOfTheWeek) {
                                '일' -> tg_sun.isChecked = true
                                '월' -> tg_mon.isChecked = true
                                '화' -> tg_tue.isChecked = true
                                '수' -> tg_wed.isChecked = true
                                '목' -> tg_thu.isChecked = true
                                '금' -> tg_fri.isChecked = true
                                '토' -> tg_sat.isChecked = true
                            }
                        }
                    }
                }

            }
            else -> this.finish()
        }
    }

    fun onClickToggleButton(view: View) {
        val toggle: ToggleButton = view as ToggleButton;

        if (toggle.isChecked) {
            toggle.setTextColor(Color.BLUE)
            toggle.background = getDrawable(R.drawable.ic_toggle_choose)
        } else {
            when (toggle.id) {
                R.id.tg_sun -> toggle.setTextColor(Color.RED)
                R.id.tg_sat -> toggle.setTextColor(Color.BLUE)
                else -> toggle.setTextColor(Color.BLACK)
            }

            toggle.background = getDrawable(R.color.default_List_background_color)
        }

//        val colorFrom: Int =
//        val colorTo: Int = android.R.color.holo_blue_bright
//        animateToggle(view as ToggleButton, colorFrom, colorTo)
    }

    fun onClickKakaoMapOpen(view: View) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("daummaps://open?page=placeSearch"))
        startActivity(intent)
    }

    fun onClickClose(view: View) {
        this.finish()
    }

    fun onClickSave(view: View) {

        var dayOfTheWeek: String = ""
        dayOfTheWeek += if (tg_sun.isChecked) "일" else ""
        dayOfTheWeek += if (tg_mon.isChecked) "월" else ""
        dayOfTheWeek += if (tg_tue.isChecked) "화" else ""
        dayOfTheWeek += if (tg_wed.isChecked) "수" else ""
        dayOfTheWeek += if (tg_thu.isChecked) "목" else ""
        dayOfTheWeek += if (tg_fri.isChecked) "금" else ""
        dayOfTheWeek += if (tg_sat.isChecked) "토" else ""

        val name = et_name.text.toString()
        val calender = Calendar.getInstance()
        calender.set(0, 0, 0, tp_time.hour, tp_time.minute)

        val location = "서울시 종로구 경복궁"
        val x = "37.578937"
        val y = "126.976397"
        val isAlaram = true
        val music = 0
        val isVibration = sw_vibration.isChecked

        val defultData1 = AlarmEntity(this.id, calender.timeInMillis, dayOfTheWeek, name, location, x, y, isAlaram, music, isVibration)

        val ioExecutor = Executors.newSingleThreadExecutor()
        ioExecutor.execute {
            when (state) {
                STATE_ADD -> AppDatabase.getInstance(this)!!.alarmDao().insert(defultData1)
                STATE_UPDATE -> AppDatabase.getInstance(this)!!.alarmDao().update(defultData1)
            }

            runOnUiThread {
                this.finish()
            }
        }

    }

    private fun animateToggle(toggle: ToggleButton, colorFrom: Int,colorTo: Int) {
        val colorAnim = ObjectAnimator.ofInt(toggle, "textColor", colorFrom, colorTo)
        colorAnim.setEvaluator(ArgbEvaluator())
        colorAnim.start()
    }

}
