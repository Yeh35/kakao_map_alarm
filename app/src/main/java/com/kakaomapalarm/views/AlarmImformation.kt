package com.kakaomapalarm.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.View
import android.widget.ToggleButton
import com.example.kakaomapalarm.R
import com.kakaomapalarm.db.AppDatabase
import com.kakaomapalarm.db.entity.AlarmEntity
import com.kakaomapalarm.utils.AlarmIntentManager
import kotlinx.android.synthetic.main.activity_alarm_imformation.*
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import java.util.*
import java.util.concurrent.Executors

class AlarmImformation : AppCompatActivity() {

    companion object {
        const val STATE_ADD: Int = 1
        const val STATE_UPDATE: Int = 2
        const val RESULT_CODE_SAVE = 1
        const val RESULT_CODE_CANCEL = 0
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
                                '일' -> {
                                    tg_sun.isChecked = true
                                    onClickToggleButton(tg_sun)
                                }
                                '월' -> {
                                    tg_mon.isChecked = true
                                    onClickToggleButton(tg_mon)
                                }
                                '화' -> {
                                    tg_tue.isChecked = true
                                    onClickToggleButton(tg_tue)
                                }
                                '수' -> {
                                    tg_wed.isChecked = true
                                    onClickToggleButton(tg_wed)
                                }
                                '목' -> {
                                    tg_thu.isChecked = true
                                    onClickToggleButton(tg_thu)
                                }
                                '금' -> {
                                    tg_fri.isChecked = true
                                    onClickToggleButton(tg_fri)
                                }
                                '토' -> {
                                    tg_sat.isChecked = true
                                    onClickToggleButton(tg_sat)
                                }
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
    }

    fun onClickClose(view: View) {

        setResult(RESULT_CODE_CANCEL)
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

        if (state == STATE_ADD) {
            this.id
        }

        val location = "서울시 종로구 경복궁"
        val x = "37.578937"
        val y = "126.976397"
        val isAlaram = true
        val music = 0
        val isVibration = sw_vibration.isChecked

        val alarmEntity = AlarmEntity(this.id, calender.timeInMillis, dayOfTheWeek, name, location, x, y, isAlaram, music, isVibration)

        val ioExecutor = Executors.newSingleThreadExecutor()
        ioExecutor.execute {
            when (state) {
                STATE_ADD -> {
                    this.id = AppDatabase.getInstance(this)!!.alarmDao().insert(alarmEntity)
                }
                STATE_UPDATE -> {
                    AppDatabase.getInstance(this)!!.alarmDao().update(alarmEntity)
                }
            }

            AlarmIntentManager.getInstance().setAlarm(this, this.id)

            runOnUiThread {
                val resultIntent: Intent = Intent()
                resultIntent.putExtra("id", this.id)
                setResult(RESULT_CODE_SAVE)
                this.finish()
            }
        }

    }
}
