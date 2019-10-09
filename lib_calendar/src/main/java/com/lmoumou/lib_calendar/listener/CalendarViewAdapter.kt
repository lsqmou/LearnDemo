package com.lmoumou.lib_calendar.listener

import android.view.View
import com.lmoumou.lib_calendar.bean.DateBean

/**
 * @author Lmoumou
 * @date : 2019/10/9 15:59
 */
interface CalendarViewAdapter {
    fun convertView(view: View, date: DateBean): Array<View>
}