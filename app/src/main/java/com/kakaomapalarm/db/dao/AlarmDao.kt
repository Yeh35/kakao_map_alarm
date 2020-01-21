package com.kakaomapalarm.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.kakaomapalarm.db.entity.AlarmEntity
import io.reactivex.Maybe

@Dao
interface AlarmDao : BaseDao<AlarmEntity>
{
    @Query("SELECT * FROM alarm WHERE id = :id")
    fun selectById(id: Long) : Maybe<AlarmEntity>

    @Query("SELECT * FROM alarm")
    fun selectAll() : Maybe<List<AlarmEntity>>

    @Query("DELETE FROM alarm WHERE id = :id")
    fun deleteById(id: Long)

}