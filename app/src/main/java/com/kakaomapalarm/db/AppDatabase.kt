package com.kakaomapalarm.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kakaomapalarm.db.dao.AlarmDao
import com.kakaomapalarm.db.entity.AlarmEntity

@Database(entities = arrayOf(AlarmEntity::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase()
{
    abstract fun alarmDao(): AlarmDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "map-alarm.db").build()
                }
            }
            return INSTANCE
        }
    }
}