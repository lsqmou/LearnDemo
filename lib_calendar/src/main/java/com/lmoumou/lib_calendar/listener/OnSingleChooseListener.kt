package com.lmoumou.lib_calendar.listener

import android.view.View
import com.lmoumou.lib_calendar.bean.DateBean

/**
 * @author Lmoumou
 * @date : 2019/10/10 11:30
 */
interface OnSingleChooseListener {

    fun onSingleChoose(view: View, date: DateBean)
}