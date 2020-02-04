package com.kakaomapalarm.utils

import com.kakaomapalarm.db.entity.AlarmEntity
import java.util.*

class AlarmUtils
{
    companion object
    {
        fun getNextAlarmTime (alarm: AlarmEntity): Date {
            val calendar: Calendar = Calendar.getInstance()
            calendar.time = Date()
            val nowHour = calendar.get(Calendar.HOUR_OF_DAY)
            val nowMinute = calendar.get(Calendar.MINUTE)
            val nowCalculationTime = nowHour * 60 + nowMinute
            val nowDayOfWeekIndex = calendar.get(Calendar.DAY_OF_WEEK)

            calendar.time = Date(alarm.time)
            val alarmHour = calendar.get(Calendar.HOUR_OF_DAY)
            val alarmMinute = calendar.get(Calendar.MINUTE)
            val alarmCalculationTime = alarmHour * 60 + alarmMinute

            var nextDayOfWeekIndex = Int.MAX_VALUE
            var minAlarmDayOfWeekIndex = Int.MAX_VALUE

            for (dayOfWeek in alarm.day_of_the_week) {
                val dayOfWeekIndex = DateUtils.getDayOfWeekIndex(dayOfWeek)

                // 이번주 현제 이후 알림 요일 인텍스 구하기 (현제 제외)
                if (dayOfWeekIndex == nowDayOfWeekIndex) {

                    // 오늘이면 현제 시간보다 안지났으면된다.
                    if (alarmCalculationTime > nowCalculationTime) {
                        nextDayOfWeekIndex = dayOfWeekIndex
                        break
                    }
                } else if (dayOfWeekIndex > nowDayOfWeekIndex) {

                    if (nextDayOfWeekIndex > dayOfWeekIndex) {
                        nextDayOfWeekIndex = dayOfWeekIndex
                    }
                }

                // 가장 낮은 요일 인텍스 구하기
                if (minAlarmDayOfWeekIndex > dayOfWeekIndex) {
                    minAlarmDayOfWeekIndex = dayOfWeekIndex
                }
            }

            //다음 알람 시간 구하기
            calendar.time = Date()
            calendar.set(Calendar.HOUR_OF_DAY, alarmHour)
            calendar.set(Calendar.MINUTE, alarmMinute)

            // 이번주 오늘 이후에 요일 인텍스가 없다면
            if (nextDayOfWeekIndex == Int.MAX_VALUE) {
                val addDate = (7 - nowDayOfWeekIndex) + minAlarmDayOfWeekIndex
                calendar.add(Calendar.DATE, addDate)
            } else {
                val addDate = nextDayOfWeekIndex - nowDayOfWeekIndex
                calendar.add(Calendar.DATE, addDate)
            }

            calendar.set(Calendar.SECOND, 0)

            return calendar.time
        }
    }
}