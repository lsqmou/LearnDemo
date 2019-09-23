package com.lmoumou.lib_widget.adapter

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lmoumou.lib_widget.entity.DateEntity
import com.lmoumou.lib_widget.R
import kotlinx.android.synthetic.main.item_calendar.view.*
import kotlinx.android.synthetic.main.item_calendar_week.view.*

/**
 * @author Lmoumou
 * @date : 2019/9/23 14:19
 */
class CalendarAdapter(context: Context, private val dataList: MutableList<DateEntity>) :
    RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    private val mInflater: LayoutInflater by lazy { LayoutInflater.from(context) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder =
        CalendarViewHolder(mInflater.inflate(viewType, parent, false))


    override fun getItemCount(): Int = dataList.size + 7

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_calendar -> {
                dataList[position - 7].apply {
                    if (isSelfMonthDate) {
                        holder.itemView.tvContent.setTextColor(Color.BLACK)
                    } else {
                        holder.itemView.tvContent.setTextColor(Color.GRAY)
                    }
                    holder.itemView.tvContent.text = "$day"
                }
            }
            R.layout.item_calendar_week -> {
                holder.itemView.tvWeek.text = when (position) {
                    0 -> "日"
                    1 -> "一"
                    2 -> "二"
                    3 -> "三"
                    4 -> "四"
                    5 -> "五"
                    6 -> "六"
                    else -> ""
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int = when (position) {
        in 0..6 -> R.layout.item_calendar_week
        else -> R.layout.item_calendar
    }


    inner class CalendarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}
}