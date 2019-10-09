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

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSpecSize = View.MeasureSpec.getSize(widthMeasureSpec)
        var heightSpecSize = View.MeasureSpec.getSize(heightMeasureSpec)

        val itemWidth = widthSpecSize / COLUMN

        //计算日历的最大高度
        if (heightSpecSize > itemWidth * ROW) {
            heightSpecSize = itemWidth * ROW
        }

        setMeasuredDimension(widthSpecSize, heightSpecSize)

        val itemHeight = heightSpecSize / ROW

        val itemSize = Math.min(itemWidth, itemHeight)
        for (i in 0 until childCount) {
            val childView = getChildAt(i)
            childView.measure(
                View.MeasureSpec.makeMeasureSpec(itemSize, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(itemSize, View.MeasureSpec.EXACTLY)
            )
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (childCount == 0) return

        val childView = getChildAt(0)
        val itemWidth = measuredWidth/7
        val itemHeight = childView.measuredHeight
        //计算列间距
        val dx = (measuredWidth - itemWidth * COLUMN) / (COLUMN * 2)

        //当显示五行时扩大行间距
        var dy = 0
        if (childCount == 35) {
            dy = itemHeight / 5
        }
        for (i in 0 until childCount) {
            val view = getChildAt(i)

            val left = i % COLUMN * itemWidth + (2 * (i % COLUMN) + 1)
            val top = i / COLUMN * (itemHeight)
            val right = left + itemWidth
            val bottom = top + itemHeight
            view.layout(left, top, right, bottom)
        }
    }

    private var lastClickedView: View? = null//记录上次点击的Item
    private var lastMonthDays: Int = 0//记录当月显示的上个月天数
    private var nextMonthDays: Int = 0//记录当月显示的下个月天数
    private var currentMonthDays: Int = 0//记录当月天数
    private var item_layout: Int = 0
    private var calendarViewAdapter: CalendarViewAdapter? = null
    /**
     * @param dates             需要展示的日期数据
     * @param currentMonthDays  当月天数
     * */
    fun setDateList(dates: List<DateBean>, currentMonthDays: Int) {
        if (childCount > 0) removeAllViews()
        this.currentMonthDays = currentMonthDays

        for (i in 0 until dates.size) {
            val date = dates[i]


            var view: View
            var solarDay: TextView//阳历TextView
            var lunarDay: TextView//阴历TextView
            var ivSubscript: ImageView//下标
            if (item_layout != 0 && calendarViewAdapter != null) {
                view = LayoutInflater.from(mContext).inflate(item_layout, null)
                val views = calendarViewAdapter!!.convertView(view, date)
                solarDay = views[0] as TextView
                lunarDay = views[1] as TextView
                ivSubscript = views[2] as ImageView
            } else run {
                view = LayoutInflater.from(mContext).inflate(R.layout.item_month_layout, null)
                solarDay = view.findViewById(R.id.solar_day)
                lunarDay = view.findViewById(R.id.lunar_day)
                ivSubscript = view.findViewById(R.id.iv_subscript)
            }

            if (date.type == 0) {
                lastMonthDays++
                if (!mAttrsBean.isShowLastNext) {
                    solarDay.visibility = View.INVISIBLE
                    ivSubscript.visibility = View.INVISIBLE
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
                    view.isEnabled = false
                    addView(view, i)
                    continue
                }
            }

            solarDay.setTextColor(mAttrsBean.colorSolarNormal)
            solarDay.textSize = mAttrsBean.sizeSolar.toFloat()

            //设置上个月和下个月的阳历颜色
            if (date.type == 0 || date.type == 2) {
                solarDay.setTextColor(Color.GRAY)
            }
            solarDay.text = "${date.solar[2]}"

            if (date.isShowSubscript) {
                ivSubscript.visibility = View.VISIBLE
                ivSubscript.setImageResource(mAttrsBean.subscript)
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
                setDayStyle(view, COLOR_SET)
            }


            view.setOnClickListener {
                //1.单选，默认选中
                if (mAttrsBean.chooseType == 0) {

                }
                //2.多选，
                //3.范围选择
            }
            addView(view, i)
        }
        requestLayout()
    }

    private fun setDayStyle(v: View, type: Int) {
        val solarDay = v.findViewById<TextView>(R.id.solar_day)
        if (type == 0) {
            v.setBackgroundResource(0)
            solarDay.setTextColor(mAttrsBean.colorSolarNormal)
        } else {
            v.setBackgroundResource(mAttrsBean.dayBg)
            solarDay.setTextColor(mAttrsBean.colorSolarSelect)
        }

    }

    fun setOnCalendarViewAdapter(item_layout: Int, calendarViewAdapter: CalendarViewAdapter?) {
        this.item_layout = item_layout
        this.calendarViewAdapter = calendarViewAdapter
    }
}