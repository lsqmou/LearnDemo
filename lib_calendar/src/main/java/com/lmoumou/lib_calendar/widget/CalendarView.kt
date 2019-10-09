package com.lmoumou.lib_calendar.widget

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.lmoumou.lib_calendar.R
import com.lmoumou.lib_calendar.bean.AttrsBean
import com.lmoumou.lib_calendar.listener.CalendarViewAdapter
import com.lmoumou.lib_calendar.utils.CalendarUtil
import com.lmoumou.lib_calendar.utils.SolarUtil

/**
 * @author Lmoumou
 * @date : 2019/10/9 16:52
 */
class CalendarView : ViewPager {

    private val mAttrsBean: AttrsBean by lazy { AttrsBean() }
    private var initDate: IntArray? = null//日历初始显示的年月
    private lateinit var startDate: IntArray //日历的开始年、月
    private lateinit var endDate: IntArray//日历的结束年、月

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        attrs?.let {
            val ta = context.obtainStyledAttributes(it, R.styleable.CalendarView)
            for (i in 0 until ta.indexCount) {
                val attr = ta.getIndex(i)
                when (attr) {
                    R.styleable.CalendarView_show_last_next -> mAttrsBean.isShowLastNext = ta.getBoolean(attr, true)
                    R.styleable.CalendarView_show_lunar -> mAttrsBean.isShowLunar = ta.getBoolean(attr, true)
//                    R.styleable.CalendarView_show_holiday -> mAttrsBean.setShowHoliday(ta.getBoolean(attr, true))
//                    R.styleable.CalendarView_show_term -> mAttrsBean.setShowTerm(ta.getBoolean(attr, true))
                    R.styleable.CalendarView_switch_choose -> mAttrsBean.isSwitchChoose = ta.getBoolean(attr, true)
//                    R.styleable.CalendarView_solar_color -> mAttrsBean.setColorSolar(ta.getColor(attr, mAttrsBean.getColorSolar()))
//                    R.styleable.CalendarView_solar_size -> mAttrsBean.sizeSolar = CalendarUtil.getTextSize(context, ta.getInteger(attr, mAttrsBean.sizeSolar))
//                    R.styleable.CalendarView_lunar_color -> mAttrsBean.setColorLunar(ta.getColor(attr, mAttrsBean.getColorLunar()))
//                    R.styleable.CalendarView_lunar_size -> mAttrsBean.setSizeLunar(
//                        CalendarUtil.getTextSize(
//                            context,
//                            ta.getInt(attr, mAttrsBean.getSizeLunar())
//                        )
//                    )
//                    R.styleable.CalendarView_holiday_color -> mAttrsBean.setColorHoliday(ta.getColor(attr, mAttrsBean.getColorHoliday()))
                    R.styleable.CalendarView_choose_color -> mAttrsBean.colorChoose =
                        ta.getColor(attr, mAttrsBean.colorChoose)
                    R.styleable.CalendarView_day_bg -> mAttrsBean.dayBg = ta.getResourceId(attr, mAttrsBean.dayBg)
                    R.styleable.CalendarView_choose_type -> mAttrsBean.chooseType = ta.getInt(attr, 0)
                }
            }

            ta.recycle()

            startDate = intArrayOf(1900, 1)
            endDate = intArrayOf(2049, 12)
            mAttrsBean.startDate = startDate
            mAttrsBean.endDate = endDate
        }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val calendarHeight: Int
        if (adapter != null) {
            val view = getChildAt(0) as MonthView
            calendarHeight = view.measuredHeight
            setMeasuredDimension(
                widthMeasureSpec,
                View.MeasureSpec.makeMeasureSpec(calendarHeight, View.MeasureSpec.EXACTLY)
            )
        }
    }

    private var calendarViewAdapter: CalendarViewAdapter? = null
    private var item_layout: Int = 0
    private var calendarPagerAdapter: CalendarPagerAdapter? = null
    private var count: Int = 0//ViewPager的页数
    //记录当前PagerAdapter的position
    private var currentPosition: Int = 0

    fun initView() {
        count = (endDate[0] - startDate[0]) * 12 + endDate[1] - startDate[1] + 1
        calendarPagerAdapter = CalendarPagerAdapter(count)
        calendarPagerAdapter!!.setAttrsBean(mAttrsBean)
        calendarPagerAdapter!!.setOnCalendarViewAdapter(item_layout, calendarViewAdapter)
        adapter = calendarPagerAdapter
        currentPosition = CalendarUtil.dateToPosition(initDate!![0], initDate!![1], startDate[0], startDate[1])
        Log.e("CalendarView", "currentPosition->$currentPosition")
        setCurrentItem(currentPosition, false)
    }

    /**
     * 设置自定义日期样式
     *
     * @param item_layout         自定义的日期item布局
     * @param calendarViewAdapter 解析item的接口
     */
    fun setOnCalendarViewAdapter(item_layout: Int, calendarViewAdapter: CalendarViewAdapter): CalendarView {
        this.item_layout = item_layout
        this.calendarViewAdapter = calendarViewAdapter
        return this
    }

    /**
     * 设置日历初始显示的年月
     *
     * @param date
     * @return
     */
    fun setInitDate(date: String): CalendarView {
        Log.e("CalendarView","date->$date")
        initDate = CalendarUtil.strToArray(date)
        return this
    }

    /**
     * 设置日历的开始年月、结束年月
     *
     * @param startDate
     * @param endDate
     * @return
     */
    fun setStartEndDate(startDate: String?, endDate: String?): CalendarView {
        this.startDate = CalendarUtil.strToArray(startDate)
        if (startDate == null) {
            this.startDate = intArrayOf(1900, 1)
        }
        this.endDate = CalendarUtil.strToArray(endDate)
        if (endDate == null) {
            this.endDate = intArrayOf(2049, 12)
        }
        mAttrsBean.startDate = this.startDate
        mAttrsBean.endDate = this.endDate
        return this
    }

    /**
     * 设置需要展示下标的日期
     * */
    fun setSubscript(arrays: ArrayList<String>): CalendarView {
        mAttrsBean.subscriptArray = arrays
        return this
    }

    /**
     * 设置单选时默认选中的日期
     *
     * @param date
     * @return
     */
    fun setSingleDate(date: String): CalendarView {
        var singleDate = CalendarUtil.strToArray(date)
        if (!isIllegal(singleDate!!)) {
            singleDate = null
        }
        mAttrsBean.singleDate = singleDate
        return this
    }

    /**
     * 检查初始化选中的日期，或者要跳转的日期是否合法
     *
     * @param destDate
     * @return
     */
    private fun isIllegal(destDate: IntArray): Boolean {

        if (destDate[1] > 12 || destDate[1] < 1) {
            return false
        }

        if (CalendarUtil.dateToMillis(destDate) < CalendarUtil.dateToMillis(startDate)) {
            return false
        }

        if (CalendarUtil.dateToMillis(destDate) > CalendarUtil.dateToMillis(endDate)) {
            return false
        }

        if (destDate[2] > SolarUtil.getMonthDays(destDate[0], destDate[1]) || destDate[2] < 1) {
            return false
        }


        if (mAttrsBean.disableStartDate != null) {
            if (CalendarUtil.dateToMillis(destDate) < CalendarUtil.dateToMillis(mAttrsBean.disableStartDate)) {
                return false
            }
        }

        if (mAttrsBean.disableEndDate != null) {
            if (CalendarUtil.dateToMillis(destDate) > CalendarUtil.dateToMillis(mAttrsBean.disableEndDate)) {
                return false
            }
        }

        return true
    }
}