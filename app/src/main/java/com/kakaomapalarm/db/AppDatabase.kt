package com.kakaomapalarm.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kakaomapalarm.db.entity.AlarmEntity

@Database(entities = arrayOf(AlarmEntity::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase()
{
    abstract fun EventIconDao(): EventIconDao
    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "your_db.db").build()
                }
            }
            return INSTANCE
        }
    }
}