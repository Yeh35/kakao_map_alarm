package com.kakaomapalarm.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kakaomapalarm.db.entity.AlarmEntity
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface AlarmDao : BaseDao<AlarmEntity>
{
    @Query("SELECT * FROM alarm WHERE id = :id")
    fun selectById(id: Long) : AlarmEntity

    @Query("SELECT * FROM alarm")
    fun selectAll() : List<AlarmEntity>

    @Query("DELETE FROM alarm WHERE id = :id")
    fun deleteById(id: Long): Int
}