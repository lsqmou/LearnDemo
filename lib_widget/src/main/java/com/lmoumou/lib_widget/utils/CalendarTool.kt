package com.lmoumou.lib_widget.utils

import android.graphics.Point
import android.util.Log
import com.lmoumou.lib_widget.entity.DateEntity
import com.lmoumou.lib_widget.entity.BaseDateEntity
import java.util.*

/**
 * @author Lmoumou
 * @date : 2019/9/20 16:50
 */
class CalendarTool<T : BaseDateEntity> {
    private val weekDayRow: IntArray by lazy { intArrayOf(0, 1, 2, 3, 4, 5, 6) }
    private val mDataList: MutableList<DateEntity> by lazy { mutableListOf<DateEntity>() }//日期数组
    private var mRecordList: MutableList<T>? = null//事件记录数组
    private var mDateEntity: DateEntity? = null
    private var mYear: Int = 0
    private var mMonth: Int = 0

    private var mEndBelong: Boolean = false
    private var mStartBelong: Boolean = false
    private var mStartDay: Int = 0
    private var mEndDay: Int = 0

    /**
     * 当前年月日
     */
    private var mCurrenYear: Int = 0
    private var mCurrenMonth: Int = 0
    private var mCurrenDay: Int = 0

    fun getmCurrenDay(): Int {
        return mCurrenDay
    }

    fun setmCurrenDay(mCurrenDay: Int) {
        this.mCurrenDay = mCurrenDay
    }

    fun initRecordList(recordList: ArrayList<T>) {
        mRecordList = recordList
    }


    /**
     * 平年月天数数组
     */
    private var commonYearMonthDay = intArrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
    /**
     * 闰年月天数数组
     */
    private var leapYearMonthDay = intArrayOf(31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

    init {
        /** 初始化当前系统的日期 */
        val calendar = Calendar.getInstance()
        mCurrenYear = calendar.get(Calendar.YEAR)
        mCurrenMonth = calendar.get(Calendar.MONTH) + 1
        mCurrenDay = calendar.get(Calendar.DAY_OF_MONTH)
        this.mYear = mCurrenYear
        this.mMonth = mCurrenMonth
    }

    /**
     * 获取当前日历的年月 x为年，y为月
     */
    fun getNowCalendar(): Point {
        return Point(mYear, mMonth)
    }

    /**
     * 判断第一天属不属于本月
     */
    fun isStartBelong(): Boolean {
        return mStartBelong
    }

    /**
     * 判断最后一天属不属于本月
     */
    fun isEndBelong(): Boolean {
        return mEndBelong
    }

    /**
     * 获取日历第一天的日期
     */
    fun getStartDay(): Int {
        return mStartDay
    }

    /**
     * 获取日历最后一天的日期
     */
    fun getEndDay(): Int {
        return mEndDay
    }

    fun initRecordList(recordList: MutableList<T>) {
        mRecordList = recordList
    }

    fun initDateList(year: Int = mYear, month: Int = mMonth): MutableList<DateEntity> {
        mDataList.clear()
        var endDate = 0// 得到上一个月的天数，作为上一个月在本日历的结束日期
        endDate =
            if (year - 1 == this.mYear || month == 1) {// 说明向前翻了一年，那么上个月的天数就应该是上一年的12月的天数，或者到翻到一月份的时候，那么上一个月的天数也是上一年的12月份的天数
                this.getDays(year - 1, 12)
            } else {// 得到上一个月的天数，作为上一个月在本日历的结束日期
                this.getDays(year, month - 1)
            }
        /** 修改部分结束 */

        this.mYear = year// 当前日历上显示的年
        this.mMonth = month// 当前日历上显示的月

        val days = this.getDays(year, month)// 得到本月的总共天数
        val dayOfWeek = this.getWeekDay(year, month)//得到当前年月的第一天为星期几
        var selfDaysEndWeek = 0// 本月的最后一天是星期几

        mStartBelong = true

        /** 先添加前面不属于本月的 */
        if (dayOfWeek != 0) {
            val startDate = endDate - dayOfWeek + 1// 当前月的上一个月在本日历的开始日期
            var i = startDate
            var j = 0
            while (i <= endDate) {


                mDateEntity = DateEntity(year, month - 1, i)
                mDateEntity!!.date = mDateEntity!!.year * 10000 + mDateEntity!!.month * 100 + i
                if (startDate == i) {
                    mStartBelong = false
                    mStartDay = mDateEntity!!.date
                }

                mDateEntity!!.isSelfMonthDate = false
                mDateEntity!!.weekDay = weekDayRow[j]
                mDateEntity!!.hasRecord = hasRecord(mDateEntity!!.date)
                mDataList.add(mDateEntity!!)
                i++
                j++
            }
        }

        /** 添加本月的 */
        run {
            var i = 1
            var j = dayOfWeek
            while (i <= days) {

                mDateEntity = DateEntity(year, month, i)
                mDateEntity!!.date = mDateEntity!!.year * 10000 + mDateEntity!!.month * 100 + i
                if (mStartBelong && i == 1) {
                    mStartDay = mDateEntity!!.date
                }
                if (i == days) {
                    mEndDay = mDateEntity!!.date
                }
                mDateEntity!!.isSelfMonthDate = true
                if (j >= 7) {
                    j = 0
                }
                selfDaysEndWeek = j
                mDateEntity!!.weekDay = weekDayRow[j]
                if (year == mCurrenYear && month == mCurrenMonth && i == mCurrenDay) {
                    mDateEntity!!.isNowDate = true
                }
                mDateEntity!!.hasRecord = hasRecord(mDateEntity!!.date)
                mDataList.add(mDateEntity!!)
                i++
                j++
            }
        }

        mEndBelong = true

        /*** 添加后面下一个月的 */
        var i = 1
        var j = selfDaysEndWeek + 1
        while (i < 7) {

            if (j >= 7) {
                break
            }
            mEndBelong = false

            mDateEntity = DateEntity(year, month + 1, i)

            if (mDateEntity!!.month > 12) {
                mDateEntity!!.year = year + 1
                mDateEntity!!.month = 1
            }
            mDateEntity!!.date = mDateEntity!!.year * 10000 + mDateEntity!!.month * 100 + i
            mDateEntity!!.isSelfMonthDate = false
            mDateEntity!!.weekDay = weekDayRow[j]
            mDateEntity!!.hasRecord = hasRecord(mDateEntity!!.date)
            mDataList.add(mDateEntity!!)

            mEndDay = mDateEntity!!.year * 10000 + mDateEntity!!.month * 100 + i
            i++
            j++
        }

        return mDataList
    }

    private fun hasRecord(date: Int): Boolean {
        mRecordList?.let {
            for (baseDateEntity in it) {
                if (baseDateEntity.year * 10000 + baseDateEntity.month * 100 + baseDateEntity.day == date) {
                    return true
                }
            }
        }
        return false
    }


    /**
     * 通过年月，获取这个月一共有多少天
     */
    private fun getDays(year: Int, month: Int): Int {
        var days = 0

        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
            if (month in 1..12) {
                days = leapYearMonthDay[month - 1]
            }
        } else {
            if (month in 1..12) {
                days = commonYearMonthDay[month - 1]
            }
        }
        return days
    }

    /**
     * 通过年，月获取当前月的第一天为星期几 ，返回0是星期天，1是星期一，依次类推
     */
    private fun getWeekDay(year: Int, month: Int): Int {
        val dayOfWeek: Int
        var goneYearDays = 0
        var thisYearDays = 0
        var isLeapYear = false//闰年
        val commonYearMonthDay = intArrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
        val leapYearMonthDay = intArrayOf(31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
        for (i in 1900 until year) {// 从1900年开始算起，1900年1月1日为星期一
            goneYearDays += if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {
                366
            } else {
                365
            }
        }
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
            isLeapYear = true
            for (i in 0 until month - 1) {
                thisYearDays += leapYearMonthDay[i]
            }
        } else {
            isLeapYear = false
            for (i in 0 until month - 1) {
                thisYearDays += commonYearMonthDay[i]
            }
        }
        dayOfWeek = (goneYearDays + thisYearDays + 1) % 7

        Log.d(this.javaClass.name, "从1990到现在有" + (goneYearDays + thisYearDays + 1) + "天")
        Log.d(this.javaClass.name, year.toString() + "年" + month + "月" + 1 + "日是星期" + dayOfWeek)
        return dayOfWeek
    }
}