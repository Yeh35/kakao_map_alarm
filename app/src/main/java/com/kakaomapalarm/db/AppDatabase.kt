package com.kakaomapalarm.db

import android.content.Context
import android.os.Debug
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kakaomapalarm.db.dao.AlarmDao
import com.kakaomapalarm.db.entity.AlarmEntity
import java.util.*
import java.util.concurrent.Executors

@Database(entities = arrayOf(AlarmEntity::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase()
{
    abstract fun alarmDao(): AlarmDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
//                    INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "map-alarm.db").build()
                    INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "map-alarm.db")
                            .addCallback(object: Callback() {
                                override fun onCreate(db: SupportSQLiteDatabase) {
                                    super.onCreate(db)

                                    val ioExecutor = Executors.newSingleThreadExecutor()
                                    ioExecutor.execute {
                                        val calender = Calendar.getInstance()
                                        calender.set(0,0,0,1,0)
                                        val defultData1 = AlarmEntity(0, calender.timeInMillis, "월", "기본 알람1", "서울시 종로구 경복궁", "37.578937", "126.976397", false, 0, true)
                                        getInstance(context)!!.alarmDao().insert(defultData1)

                                        calender.set(0,0,0,13,0)

                                        val defultData2 = AlarmEntity(0, calender.timeInMillis, "월", "기본 알람2", "서울시 종로구 경복궁", "37.578937", "126.976397", true, 0, true)
                                        getInstance(context)!!.alarmDao().insert(defultData2)
                                    }
                                }
                            }) .build()
                }
            }

            return INSTANCE
        }
    }
}