package com.lmoumou.lib_calendar

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.lmoumou.lib_calendar.bean.DateBean
import com.lmoumou.lib_calendar.bean.ItemAttrsBeen
import com.lmoumou.lib_calendar.listener.OnPagerChangeListener
import com.lmoumou.lib_calendar.listener.OnSingleChooseListener
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

        val testMap: HashMap<String, ItemAttrsBeen> = hashMapOf()
        testMap["2019.10.8"] =
            ItemAttrsBeen(Color.parseColor("#FE7E6D"), R.mipmap.ic_already_read, R.drawable.item_bg_normal)
        testMap["2019.10.9"] =
            ItemAttrsBeen(Color.parseColor("#FE7E6D"), R.mipmap.ic_already_read, R.drawable.item_bg_normal)
        testMap["2019.10.10"] =
            ItemAttrsBeen(Color.parseColor("#FE7E6D"), R.mipmap.ic_already_read, R.drawable.item_bg_normal)
        testMap["2019.10.11"] =
            ItemAttrsBeen(Color.parseColor("#FE7E6D"), R.mipmap.ic_already_read, R.drawable.item_bg_normal)

        testMap["2019.10.19"] = ItemAttrsBeen(Color.parseColor("#59E8D8"), R.mipmap.ic_love,R.drawable.item_bg_normal)
        testMap["2019.10.21"] = ItemAttrsBeen(Color.parseColor("#59E8D8"), R.mipmap.ic_love,R.drawable.item_bg_normal)
        testMap["2019.10.23"] = ItemAttrsBeen(Color.parseColor("#59E8D8"), R.mipmap.ic_love,R.drawable.item_bg_normal)


        calendar
            .setStartEndDate("2016.1", "2028.12")//日历范围
            .setInitDate("${cDate[0]}.${cDate[1]}")//当前展示的月份
            .setSubscript(testMap)//有下标的日期
            .setSingleDate("2019.10.10")//选中的日期
            .initView()

        calendar.setOnSingleChooseListener(object : OnSingleChooseListener {
            override fun onSingleChoose(view: View, date: DateBean) {
                Log.e("CalendarActivity2", "onSingleChoose->${date.solar[0]}.${date.solar[1]}.${date.solar[2]}")
            }
        })

        calendar.setOnPagerChangeListener(object : OnPagerChangeListener {
            override fun onPagerChanged(date: IntArray) {
                Log.e("CalendarActivity2", "onPagerChanged->${date[0]}.${date[1]}.${date[2]}")
            }
        })
    }
}