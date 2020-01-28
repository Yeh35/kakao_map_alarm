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
import kotlinx.android.synthetic.main.activity_alarm_imformation.*


class AlarmImformation : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_imformation)
        tp_time.setIs24HourView(true)
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

    private fun animateToggle(toggle: ToggleButton, colorFrom: Int,colorTo: Int) {
        val colorAnim = ObjectAnimator.ofInt(toggle, "textColor", colorFrom, colorTo)
        colorAnim.setEvaluator(ArgbEvaluator())
        colorAnim.start()
    }


}
