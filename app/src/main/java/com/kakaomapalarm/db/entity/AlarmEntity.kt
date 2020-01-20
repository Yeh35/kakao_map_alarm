package com.kakaomapalarm.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "eventIcon")
data class  AlarmEntity(@PrimaryKey(autoGenerate = true) val id: Long, var time: Long, var day_of_the_week: String, var name: String )