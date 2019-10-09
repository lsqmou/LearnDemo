package com.lmoumou.lib_calendar.widget

import android.support.v4.view.PagerAdapter
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import com.lmoumou.lib_calendar.bean.AttrsBean
import com.lmoumou.lib_calendar.listener.CalendarViewAdapter
import com.lmoumou.lib_calendar.utils.CalendarUtil
import com.lmoumou.lib_calendar.utils.SolarUtil
import java.util.*

/**
 * @author Lmoumou
 * @date : 2019/10/9 16:46
 */
class CalendarPagerAdapter(private val count: Int) : PagerAdapter() {
    override fun isViewFromObject(view: View, p1: Any): Boolean = view == p1

    override fun getCount(): Int = count

    //缓存上一次回收的MonthView
    private val cache = LinkedList<MonthView>()
    private val mViews = SparseArray<MonthView>()
    private var item_layout: Int = 0
    private var calendarViewAdapter: CalendarViewAdapter? = null

    private lateinit var mAttrsBean: AttrsBean

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: MonthView = if (!cache.isEmpty()) {
            cache.removeFirst()
        } else {
            MonthView(container.context)
        }
        //根据position计算对应年、月
        val date = CalendarUtil.positionToDate(position, mAttrsBean.startDate[0], mAttrsBean.startDate[1])
        view.mAttrsBean= mAttrsBean
        view.setOnCalendarViewAdapter(item_layout, calendarViewAdapter)
        view.setDateList(
            CalendarUtil.getMonthDate(date[0], date[1], mAttrsBean.subscriptArray),
            SolarUtil.getMonthDays(date[0], date[1])
        )
        mViews.put(position, view)
        container.addView(view)

        return view

    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as MonthView)
        cache.addLast(`object`)
        mViews.remove(position)
    }

    /**
     * 获得ViewPager缓存的View
     *
     * @return
     */
    fun getViews(): SparseArray<MonthView> {
        return mViews
    }

    fun setAttrsBean(attrsBean: AttrsBean) {
        mAttrsBean = attrsBean
    }

    fun setOnCalendarViewAdapter(item_layout: Int, calendarViewAdapter: CalendarViewAdapter?) {
        this.item_layout = item_layout
        this.calendarViewAdapter = calendarViewAdapter
    }
}