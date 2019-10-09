package com.lmoumou.lib_widget.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lmoumou.lib_widget.R

/**
 * @author Lmoumou
 * @date : 2019/9/30 14:17
 */
class CalendarAdapter2(context: Context) : RecyclerView.Adapter<CalendarAdapter2.CalendarViewHolder>() {

    private val mInflater:LayoutInflater by lazy { LayoutInflater.from(context) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        return CalendarViewHolder(mInflater.inflate(R.layout.item_calendar2,parent,false))
    }

    override fun getItemCount(): Int = Int.MAX_VALUE

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {

    }

    inner class CalendarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}