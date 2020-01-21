package com.kakaomapalarm.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kakaomapalarm.R
import com.kakaomapalarm.db.AppDatabase

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = AppDatabase.getInstance(this)
        val alarmList = db!!.alarmDao().selectAll()

    }
}
