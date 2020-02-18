package com.kakaomapalarm.views

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.kakaomapalarm.R
import com.kakaomapalarm.db.AppDatabase
import com.kakaomapalarm.db.entity.AlarmEntity
import com.kakaomapalarm.utils.AlarmUtils
import com.kakaomapalarm.utils.DateUtils
import com.kakaomapalarm.utils.MapUtils
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity()
{
    companion object {
        private const val START_ALARM_ACTIVITY_ADD_OPTION = 1
        private const val START_ALARM_ACTIVITY_UPDATE_OPTION = 2
    }

    private val db = AppDatabase.getInstance(this)
    private val listAdapter: MainListAdapter by lazy {
        MainListAdapter(this)
    }
    private var is_action = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)

        if (MapUtils.CheckLocationServiceState(this)) {
            MapUtils.CheckRunTimePermission(this)
        } else {
            MapUtils.ShowDialogForLocationServiceSetting(this)
        }

        list_alarm.adapter = this.listAdapter
        list_alarm.onItemClickListener = AdapterView.OnItemClickListener() { parent, view, position, id ->
            val itemData:MainListItemData = listAdapter.getItem(id.toInt())

            val intent = Intent(this, AlarmImformation::class.java)
            intent.putExtra("state", AlarmImformation.STATE_UPDATE)
            intent.putExtra("id", itemData.id)
            this.startActivityForResult(intent, START_ALARM_ACTIVITY_UPDATE_OPTION)
        }

        list_alarm.onItemLongClickListener = AdapterView.OnItemLongClickListener { parent, view, position, id ->
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setTitle("삭제")
            dialogBuilder.setMessage("해당 알람을 삭제할까요?")

            //네 버튼
            dialogBuilder.setPositiveButton("네", DialogInterface.OnClickListener { dialog, which ->
                val itemData = listAdapter.getItem(id.toInt())

                val ioExecutor = Executors.newSingleThreadExecutor()
                ioExecutor.execute {
                    db!!.alarmDao().deleteById(itemData.id)
                }

                this.refreshMainList()
            })

            dialogBuilder.setNegativeButton("아니요", DialogInterface.OnClickListener { dialog, which ->

            })

            val dialog = dialogBuilder.create()
            dialog.show()

            true
        }

        this.refreshMainList()
    }

    override fun onStart() {
        super.onStart()
        is_action = true

        val ioExecutor = Executors.newSingleThreadExecutor()
        ioExecutor.execute {
            val calender = Calendar.getInstance()
            calender.time = Date()

            while (is_action) {
                val alarmList:List<AlarmEntity> = db!!.alarmDao().selectAll()
                var nextAlarmDate: Date = Date(Long.MAX_VALUE)

                for (alarm in alarmList) {
                    val nextAlarmDateTemp = AlarmUtils.getNextAlarmTime(alarm)
                    Log.d("MainActivity", "nextAlarmDateTemp: ${nextAlarmDateTemp}")

                    if (nextAlarmDate.time > nextAlarmDateTemp.time) {
                        nextAlarmDate = nextAlarmDateTemp
                    }
                }

                val nextAlarmCalender: Calendar = Calendar.getInstance()
                nextAlarmCalender.time = nextAlarmDate

                val intervalCalender: Calendar = Calendar.getInstance()
                val intervalYear: Int = nextAlarmCalender.get(Calendar.YEAR) - intervalCalender.get(Calendar.YEAR)
                val intervalDay: Int = nextAlarmCalender.get(Calendar.DAY_OF_YEAR) - intervalCalender.get(Calendar.DAY_OF_YEAR)
                val intervalHour: Int = nextAlarmCalender.get(Calendar.HOUR_OF_DAY) - intervalCalender.get(Calendar.HOUR_OF_DAY)
                val intervalMinute: Int = nextAlarmCalender.get(Calendar.MINUTE) - intervalCalender.get(Calendar.MINUTE)

                runOnUiThread {
                    var hour = (intervalYear * 365 * 24) + (intervalDay * 24) + intervalHour
                    var minute = intervalMinute
                    if (minute < 0) {
                        hour -= 1
                        minute = 60 + intervalMinute
                    }
                    tv_timeRemaining.text = "${hour}시간 ${minute}분 후 출발"
                    tv_now.text = DateUtils.formatKorDetail(Date())
                }

                // 1분 기다리고 1분 추가
                SystemClock.sleep(60000)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        is_action = false
    }

    fun onClickListenerForAddButton(view: View) {
        val intent = Intent(this, AlarmImformation::class.java)
        intent.putExtra("state", AlarmImformation.STATE_ADD)
        this.startActivityForResult(intent, START_ALARM_ACTIVITY_ADD_OPTION)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == AlarmImformation.RESULT_CODE_CANCEL) {
            return
        }

        when (requestCode) {
            START_ALARM_ACTIVITY_ADD_OPTION -> {
                this.refreshMainList()
            }

            START_ALARM_ACTIVITY_UPDATE_OPTION -> {
                this.refreshMainList()
            }
        }
    }

    private fun refreshMainList() {
        val ioExecutor = Executors.newSingleThreadExecutor()
        ioExecutor.execute {
            listAdapter.clear();

            val alarmList:List<AlarmEntity> = db!!.alarmDao().selectAll()

            for (alarm in alarmList) {
                val item = MainListItemData(alarm.id, alarm.time, alarm.day_of_the_week, alarm.name, alarm.location, alarm.x, alarm.y, alarm.is_alaram)
                listAdapter.addItem(item)
            }

            runOnUiThread {
                list_alarm.invalidateViews()
            }
        }
    }

    private data class MainListItemData(val id: Long, var time: Long, var day_of_the_week: String,
                                        var name: String, var location: String, var x: String,
                                        var y: String, var is_alaram: Boolean)

    private class MainListAdapter: BaseAdapter
    {
        val context: Context
        val alarmList : ArrayList<MainListItemData> = ArrayList(16)

        constructor(context: Context)
        {
            this.context = context
        }

        override fun getItem(index: Int): MainListItemData {
            return alarmList.get(index)
        }

        override fun getCount(): Int {
            return alarmList.size
        }

        override fun getItemId(index: Int): Long {
            return index.toLong()
        }

        override fun getView(index: Int, converView: View?, parent: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(context)
            val view = layoutInflater.inflate(R.layout.main_list_item, null)

            val tv_id = view.findViewById<TextView>(R.id.tv_id)
            val tv_time = view.findViewById<TextView>(R.id.tv_time)
            val tv_name = view.findViewById<TextView>(R.id.tv_name)
            val tv_location = view.findViewById<TextView>(R.id.tv_location)
            val tv_mon = view.findViewById<TextView>(R.id.tv_mon)
            val tv_tue = view.findViewById<TextView>(R.id.tv_tue)
            val tv_wed = view.findViewById<TextView>(R.id.tv_wed)
            val tv_thu = view.findViewById<TextView>(R.id.tv_thu)
            val tv_fri = view.findViewById<TextView>(R.id.tv_fri)
            val tv_sat = view.findViewById<TextView>(R.id.tv_sat)
            val tv_sun = view.findViewById<TextView>(R.id.tv_sun)

            val alarm = alarmList[index]
            tv_id.text = index.toString()
            tv_name.text = alarm.name
            tv_location.text = alarm.location

            val calender = Calendar.getInstance()
            calender.time = Date(alarm.time)
            val hour:Int = calender.get(Calendar.HOUR_OF_DAY)
            val Minute:Int = calender.get(Calendar.MINUTE)
            tv_time.text = if (hour >= 12) "오후 ${hour - 12}:${Minute}" else "오전 ${hour}:${Minute}"

            for (dayOfTheWeek: Char in alarm.day_of_the_week) {
                when (dayOfTheWeek) {
                    '일' -> {
                        tv_sun.isEnabled = true
                        tv_sun.setTextColor(ContextCompat.getColor(context, android.R.color.holo_blue_dark))
                    }
                    '월' -> {
                        tv_mon.isEnabled = true
                        tv_mon.setTextColor(ContextCompat.getColor(context, android.R.color.holo_blue_dark))
                    }
                    '화' -> {
                        tv_tue.isEnabled = true
                        tv_tue.setTextColor(ContextCompat.getColor(context, android.R.color.holo_blue_dark))
                    }
                    '수' -> {
                        tv_wed.isEnabled = true
                        tv_wed.setTextColor(ContextCompat.getColor(context, android.R.color.holo_blue_dark))
                    }
                    '목' -> {
                        tv_thu.isEnabled = true
                        tv_thu.setTextColor(ContextCompat.getColor(context, android.R.color.holo_blue_dark))
                    }
                    '금' -> {
                        tv_fri.isEnabled = true
                        tv_fri.setTextColor(ContextCompat.getColor(context, android.R.color.holo_blue_dark))
                    }
                    '토' -> {
                        tv_sat.isEnabled = true
                        tv_sat.setTextColor(ContextCompat.getColor(context, android.R.color.holo_blue_dark))
                    }
                }
            }

            return view
        }

        fun addItem(item: MainListItemData) {
            alarmList.add(item)
        }

        fun clear() {
            alarmList.clear()
        }
    }


}
