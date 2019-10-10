package com.lmoumou.lib_calendar.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.lmoumou.lib_calendar.R
import com.lmoumou.lib_calendar.bean.AttrsBean
import com.lmoumou.lib_calendar.bean.DateBean
import com.lmoumou.lib_calendar.listener.CalendarViewAdapter
import com.lmoumou.lib_calendar.listener.OnSingleChooseListener
import com.lmoumou.lib_calendar.utils.CalendarUtil

/**
 * @author Lmoumou
 * @date : 2019/10/9 15:32
 */
class MonthView : ViewGroup {
    companion object {
        const val ROW = 6
        const val COLUMN = 7
        const val COLOR_RESET = 0
        const val COLOR_SET = 1
    }

    constructor(context: Context) : this(context, null)

    private var mContext: Context

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        mContext = context
        setBackgroundColor(Color.WHITE)
    }

    lateinit var mAttrsBean: AttrsBean
    private var dy = 0
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSpecSize = View.MeasureSpec.getSize(widthMeasureSpec)
        var heightSpecSize = View.MeasureSpec.getSize(heightMeasureSpec)

        //每个Item的宽度
        val itemWidth = widthSpecSize / COLUMN

        //当前的行数
        val currentColumn = childCount / 7
        heightSpecSize = itemWidth * currentColumn + dy * (currentColumn - 1)

        setMeasuredDimension(widthSpecSize, heightSpecSize)

        val itemHeight = heightSpecSize / ROW

        val itemSize = Math.min(itemWidth, itemHeight)
        for (i in 0 until childCount) {
            val childView = getChildAt(i)
            childView.measure(
                View.MeasureSpec.makeMeasureSpec(itemWidth, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(itemWidth, View.MeasureSpec.EXACTLY)
            )
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (childCount == 0) {
            return
        }

        val childView = getChildAt(0)
        val itemWidth = measuredWidth / COLUMN
        val itemHeight = childView.measuredHeight
        //计算列间距
        val dx = (measuredWidth - itemWidth * COLUMN) / (COLUMN * 2)

        for (i in 0 until childCount) {
            val view = getChildAt(i)

            val left = i % COLUMN * itemWidth + (2 * (i % COLUMN) + 1) * dx
            val top = i / COLUMN * (itemHeight + dy)
            val right = left + itemWidth
            val bottom = top + itemHeight
            view.layout(left, top, right, bottom)
        }
    }

    private val lastClickMap: HashMap<View, DateBean> = hashMapOf()
    private var lastClickedView: View? = null//记录上次点击的Item
    private var lastMonthDays: Int = 0//记录当月显示的上个月天数
    private var nextMonthDays: Int = 0//记录当月显示的下个月天数
    private var currentMonthDays: Int = 0//记录当月天数
    private var item_layout: Int = 0
    private var calendarViewAdapter: CalendarViewAdapter? = null
    private var dates: List<DateBean>? = null
    /**
     * @param dates             需要展示的日期数据
     * @param currentMonthDays  当月天数
     * */
    fun setDateList(dates: List<DateBean>, currentMonthDays: Int) {
        if (childCount > 0) removeAllViews()
        this.currentMonthDays = currentMonthDays
        this.dates = dates
        lastMonthDays = 0
        nextMonthDays = 0
        lastClickMap.clear()
        for (i in 0 until dates.size) {
            val date = dates[i]
            val view: View = LayoutInflater.from(mContext).inflate(R.layout.item_month_layout, null)
            var solarDay: TextView//阳历TextView
            var lunarDay: TextView//阴历TextView
            var ivSubscript: ImageView//下标
            var ivBg: ImageView//背景View
            var ivBg2: ImageView//背景框

            solarDay = view.findViewById(R.id.solar_day)
            lunarDay = view.findViewById(R.id.lunar_day)
            ivSubscript = view.findViewById(R.id.iv_subscript)
            ivBg2 = view.findViewById(R.id.ivBg2)
            ivBg = view.findViewById(R.id.ivBg)


            if (date.type == 0) {
                lastMonthDays++
                if (!mAttrsBean.isShowLastNext) {
                    solarDay.visibility = View.INVISIBLE
                    ivSubscript.visibility = View.INVISIBLE
                    ivBg.visibility = View.INVISIBLE
                    ivBg2.visibility = View.INVISIBLE
                    view.isEnabled = false
                    addView(view, i)
                    continue
                }
            }

            if (date.type == 2) {
                nextMonthDays++
                if (!mAttrsBean.isShowLastNext) {
                    solarDay.visibility = View.INVISIBLE
                    ivSubscript.visibility = View.INVISIBLE
                    ivBg.visibility = View.INVISIBLE
                    ivBg2.visibility = View.INVISIBLE
                    view.isEnabled = false
                    addView(view, i)
                    continue
                }
            }
            ivBg2.setBackgroundResource(date.bgResId)
            solarDay.setTextColor(date.textColorNormal)
            solarDay.textSize = mAttrsBean.sizeSolar.toFloat()

            //设置上个月和下个月的阳历颜色
            if (date.type == 0 || date.type == 2) {
                solarDay.setTextColor(Color.GRAY)
            }
            solarDay.text = "${date.solar[2]}"

            if (date.isShowSubscript) {
                ivSubscript.visibility = View.VISIBLE
                ivSubscript.setImageResource(date.subscriptResId)
            } else {
                ivSubscript.visibility = View.INVISIBLE
            }

            if (mAttrsBean.chooseType == 0
                && mAttrsBean.singleDate != null
                && mAttrsBean.singleDate[0] == date.solar[0]
                && mAttrsBean.singleDate[1] == date.solar[1]
                && mAttrsBean.singleDate[2] == date.solar[2]
            ) {

                lastClickedView = view
                lastClickMap[view] = date
                setDayStyle(view, COLOR_SET)
            }

            //设置禁用日期
            if (date.type == 1) {
                view.tag = date.solar[2]
                if (mAttrsBean.disableStartDate != null && CalendarUtil.dateToMillis(mAttrsBean.disableStartDate) > CalendarUtil.dateToMillis(
                        date.solar
                    )
                ) {
                    view.tag = -1
                    addView(view, i)
                    continue
                }

                if (mAttrsBean.disableEndDate != null && CalendarUtil.dateToMillis(mAttrsBean.disableEndDate) < CalendarUtil.dateToMillis(
                        date.solar
                    )
                ) {
                    view.tag = -1
                    addView(view, i)
                    continue
                }
            }

            view.setOnClickListener {
                val day = date.solar[2]
                val calendarView = parent as CalendarView
                val clickListener = calendarView.getSingleChooseListener()
                when (mAttrsBean.chooseType) {
                    0 -> {//单选
                        calendarView.setLastClickDay(day)
                        singleChoose(it, clickListener, date)
                    }
                    1 -> {//多选

                    }
                    2 -> {//范围选择

                    }
                }
            }
            addView(view, i)
        }
        requestLayout()
    }

    /**
     * 单选事件处理
     * */
    private fun singleChoose(it: View, singleChooseListener: OnSingleChooseListener?, date: DateBean) {
        if (lastClickedView != null) {
            setDayStyle(lastClickedView!!, COLOR_RESET)
        }
        lastClickMap[it] = date
        setDayStyle(it, COLOR_SET)
        lastClickedView = it


        singleChooseListener?.onSingleChoose(it, date)
    }

    private fun setDayStyle(v: View, type: Int) {
        val dateBean = lastClickMap[v]

        val solarDay = v.findViewById<TextView>(R.id.solar_day)
        val ivBg = v.findViewById<ImageView>(R.id.ivBg)
        if (type == COLOR_RESET) {
            ivBg.setBackgroundResource(0)
            solarDay.setTextColor(dateBean?.textColorNormal?:Color.parseColor("#9C9EA8"))
        } else {
            ivBg.setBackgroundResource(mAttrsBean.dayBg)
            solarDay.setTextColor(dateBean?.textColorSelect?:Color.WHITE)
        }


    }

    fun setOnCalendarViewAdapter(item_layout: Int, calendarViewAdapter: CalendarViewAdapter?) {
        this.item_layout = item_layout
        this.calendarViewAdapter = calendarViewAdapter
    }


    fun refresh(day: Int, flag: Boolean) {
        if (lastClickedView != null) {
            setDayStyle(lastClickedView!!, COLOR_RESET)
        }
        if (!flag) {
            return
        }
        val array = findDestView(day)
        val destView = array[0]
        val date = array[1]
        if (destView != null && destView is View && date != null && date is DateBean) {
            setDayStyle(destView, COLOR_SET)
            lastClickedView = destView
            lastClickMap[destView] = date
            invalidate()
        }
    }

    /**
     * 查找要跳转到的页面需要展示的日期View
     *
     * @param day
     * @return
     */
    private fun findDestView(day: Int): Array<Any?> {
        var view: View? = null
        var date: DateBean? = null
        for (i in lastMonthDays until childCount - nextMonthDays) {
            if (getChildAt(i).tag as Int == day) {
                date = dates?.get(i)
                view = getChildAt(i)
                break
            }
        }


        if (view == null) {
            view = getChildAt(currentMonthDays + lastMonthDays - 1)
            date = dates?.get(currentMonthDays + lastMonthDays - 1)
        }


        if (view!!.tag as Int == -1) {
            view = null
        }


        return arrayOf(view, date)
    }


}