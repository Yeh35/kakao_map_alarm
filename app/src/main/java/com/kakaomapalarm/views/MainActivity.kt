package com.kakaomapalarm.views

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.core.view.LayoutInflaterCompat
import com.example.kakaomapalarm.R
import com.kakaomapalarm.db.AppDatabase

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = AppDatabase.getInstance(this)
        val alarmList = db!!.alarmDao().selectAll()


    }

    private data class MainListItemData(val id: Long, var time: Long, var day_of_the_week: String,
                                        var name: String, var location: String, var x: String,
                                        var y: String, var is_alaram: Boolean)

    private class MainListAdapter : BaseAdapter
    {
        val context: Context
        val alarmList : ArrayList<MainListItemData>

        constructor(context: Context, alarmList: ArrayList<MainListItemData>)
        {
            this.context = context
            this.alarmList = alarmList
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
            val layoutInflater = LayoutInflater.from(context);
            val view = layoutInflater.inflate(R.layout.main_list_item, null)

            val tv_timeRemaining = view.findViewById<TextView>(R.id.tv_timeRemaining);
            val tv_time = view.findViewById<TextView>(R.id.tv_time);
            val tv_name = view.findViewById<TextView>(R.id.tv_name);
            val tv_location = view.findViewById<TextView>(R.id.tv_location);



            return view
        }
    }
}
