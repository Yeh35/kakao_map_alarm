package com.kakaomapalarm.utils

import java.text.SimpleDateFormat
import java.util.*

class DateUtils {
    companion object {

        fun getDayOfWeekIndex(dayOfWeek: Char): Int {

            var dayOfWeekIndex : Int =  when (dayOfWeek) {
                '일' -> 1
                '월' -> 2
                '화' -> 3
                '수' -> 4
                '목' -> 5
                '금' -> 6
                '토' -> 7
                else -> -1
            }

            return dayOfWeekIndex
        }

        // 날짜는 무시하고 시간이 얼마나 차이 나는지 계산해준다.
        fun compareTimeGap(time1: Long, time2: Long): Long {
            val calendar = Calendar.getInstance()

            calendar.time = Date(time1)
            val milliSeconds1 = (calendar.get(Calendar.HOUR_OF_DAY) as Long * 60 + calendar.get(Calendar.MINUTE)) * 60000

            calendar.time = Date(time2)
            val milliSeconds2 = (calendar.get(Calendar.HOUR_OF_DAY) as Long * 60 + calendar.get(Calendar.MINUTE)) * 60000

            return milliSeconds1 - milliSeconds2
        }

        fun formatKorDetail(date: Date): String {
            var formatString: String = ""

            val calender = Calendar.getInstance()
            calender.time = date
            val month:Int = calender.get(Calendar.MONTH) + 1
            val day:Int = calender.get(Calendar.DAY_OF_MONTH)
            formatString += "${month}월 ${day}일 "

            val hour:Int = calender.get(Calendar.HOUR_OF_DAY)
            val Minute:Int = calender.get(Calendar.MINUTE)
            formatString += if (hour >= 12) "오후 ${hour - 12}:${Minute}" else "오전 ${hour}:${Minute}"

            return formatString
        }
    }
}