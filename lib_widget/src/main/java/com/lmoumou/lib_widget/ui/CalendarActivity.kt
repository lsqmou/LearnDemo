package com.lmoumou.lib_widget.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import com.lmoumou.lib_widget.entity.DateEntity
import com.lmoumou.lib_widget.R
import com.lmoumou.lib_widget.adapter.CalendarAdapter
import com.lmoumou.lib_widget.adapter.CalendarPageAdapter
import com.lmoumou.lib_widget.utils.CalendarTool
import kotlinx.android.synthetic.main.activity_calendar.*

/**
 * @author Lmoumou
 * @date : 2019/9/23 14:05
 */
class CalendarActivity : AppCompatActivity() {

    companion object {
        fun startThis(context: Context) {
            context.startActivity(Intent(context.applicationContext, CalendarActivity::class.java))
        }
    }

    private val adapter: CalendarAdapter by lazy { CalendarAdapter(this, CalendarTool<DateEntity>().initDateList()) }

    private val pageAdapter: CalendarPageAdapter by lazy { CalendarPageAdapter(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        rcvCalendar.layoutManager = GridLayoutManager(this, 7)
        rcvCalendar.adapter = adapter

        vpCalendar.adapter = pageAdapter
        vpCalendar.currentItem = 0
    }
}