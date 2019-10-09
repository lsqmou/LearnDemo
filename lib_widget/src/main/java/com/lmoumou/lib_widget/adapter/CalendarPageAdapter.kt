package com.lmoumou.lib_widget.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.lmoumou.lib_widget.entity.DateEntity
import com.lmoumou.lib_widget.R
import com.lmoumou.lib_widget.utils.CalendarTool
import kotlinx.android.synthetic.main.view_vp_item.view.*
import java.util.*


/**
 * @author Lmoumou
 * @date : 2019/9/23 14:46
 */
class CalendarPageAdapter(private val mContext: Context) : PagerAdapter() {

    companion object {
        var CURRENT_DAY_INDEX = 1000
    }

    private val dataList: MutableList<DateEntity> by lazy { mutableListOf<DateEntity>() }

    override fun isViewFromObject(p0: View, p1: Any): Boolean {
        return p0 == p1
    }

    override fun getCount(): Int = Int.MAX_VALUE

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
//        super.destroyItem(container, position, `object`)
        container.removeView(container)
    }

    private val adapter: CalendarAdapter by lazy {
        CalendarAdapter(
            mContext,
            dataList
        )
    }

    private val views: MutableList<View> = mutableListOf()

    init {
        for (i in 0 until 3) {
            val view = View.inflate(mContext, R.layout.view_vp_item, null)
            view.mRecyclerView.layoutManager = GridLayoutManager(mContext, 7)
            view.mRecyclerView.adapter = adapter
            views.add(view)
        }
    }

    private val calendarTools: CalendarTool<DateEntity> by lazy { CalendarTool<DateEntity>() }
    private val calendar: Calendar by lazy { Calendar.getInstance() }
    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val view = views[position % views.size]
        val calendar2 = getCurrentDate(position)
        dataList.addAll(calendarTools.initDateList(calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH) + 1))
        adapter.notifyDataSetChanged()
        if (container.childCount == views.size) {
            container.removeView(views[position % 3])
        }
        if (container.childCount < views.size) {
            container.addView(view, 0)
        } else {
            container.addView(view, position % 3)
        }
        return view
    }

    private fun getCurrentDate(position: Int): Calendar {
        val calendarCopy: Calendar = calendar.clone() as Calendar
        calendarCopy.add(Calendar.MONTH, position - CURRENT_DAY_INDEX)
        Log.e(
            "CalendarPageAdapter",
            "${calendarCopy.get(Calendar.YEAR)}年${calendarCopy.get(Calendar.MONTH) + 1}月${calendarCopy.get(Calendar.DAY_OF_MONTH)}日"
        )
        return calendarCopy
    }
}