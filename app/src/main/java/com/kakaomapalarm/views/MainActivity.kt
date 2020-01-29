package com.kakaomapalarm.views

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.kakaomapalarm.R
import com.kakaomapalarm.db.AppDatabase
import com.kakaomapalarm.db.entity.AlarmEntity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity()
{
    private val db = AppDatabase.getInstance(this)

    private val listAdapter: MainListAdapter by lazy {
        MainListAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        list_alarm.adapter = this.listAdapter

        val ioExecutor = Executors.newSingleThreadExecutor()
        ioExecutor.execute {
            SystemClock.sleep(500)
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

    fun OnClickListenerForAddButton(view: View) {
        val intent = Intent(this, AlarmImformation::class.java)
        this.startActivity(intent)
    }

    private data class MainListItemData(val id: Long, var time: Long, var day_of_the_week: String,
                                        var name: String, var location: String, var x: String,
                                        var y: String, var is_alaram: Boolean)

    private class MainListAdapter : BaseAdapter
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



            return view
        }

        fun addItem(item: MainListItemData) {
            alarmList.add(item)
        }
    }
}
