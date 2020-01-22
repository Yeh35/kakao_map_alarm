package com.kakaomapalarm.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarm")
data class  AlarmEntity(@PrimaryKey(autoGenerate = true) val id: Long, var time: Long,
                        var day_of_the_week: String, var name: String, var location: String,
                        var x: String, var y: String, var is_alaram: Boolean)