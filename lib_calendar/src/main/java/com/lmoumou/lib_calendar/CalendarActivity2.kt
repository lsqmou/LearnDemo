package com.lmoumou.lib_calendar

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.lmoumou.lib_calendar.utils.CalendarUtil
import kotlinx.android.synthetic.main.activity_calendar2.*

/**
 * @author Lmoumou
 * @date : 2019/10/9 9:58
 */
class CalendarActivity2 : AppCompatActivity() {

    companion object {
        fun startThis(context: Context) {
            context.startActivity(Intent(context.applicationContext, CalendarActivity2::class.java))
        }
    }

    private val cDate = CalendarUtil.getCurrentDate()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar2)
        val testData = ArrayList<String>()
        testData.add("2019.10.9")
        testData.add("2019.10.8")
        testData.add("2019.10.7")
        testData.add("2019.10.6")
        calendar
            .setStartEndDate("2016.1", "2028.12")
            .setInitDate("${cDate[0]}.${cDate[1]}")
            .setSubscript(testData)
            .setSingleDate("2019.10.31")
            .initView()
    }
}