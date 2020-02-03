package com.kakaomapalarm.views

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.kakaomapalarm.R

class AlarmActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)
    }

    fun onClickForOk(view: View) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("daummaps://open?page=placeSearch"))
        startActivity(intent)
        finish()
    }
}
