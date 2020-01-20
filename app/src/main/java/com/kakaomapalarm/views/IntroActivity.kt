package com.example.kakaomapalarm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import com.kakaomapalarm.views.MainActivity
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

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
