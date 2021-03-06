package com.lmoumou.lib_widget.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lmoumou.lib_widget.R
import kotlinx.android.synthetic.main.activity_widget.*
import kotlinx.android.synthetic.main.item_widget.view.*

/**
 * @author Lmoumou
 * @date : 2019/8/30 15:33
 */
class WidgetActivity : AppCompatActivity() {
    companion object {
        fun startThis(context: Context) {
            context.startActivity(Intent(context.applicationContext, WidgetActivity::class.java))
        }
    }

    private val mInflater: LayoutInflater by lazy { LayoutInflater.from(this) }
    private val dataList: MutableList<String> by lazy { mutableListOf<String>() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_widget)

        initData()

        mRecyclerView.adapter = object : RecyclerView.Adapter<WidgetViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WidgetViewHolder =
                WidgetViewHolder(mInflater.inflate(R.layout.item_widget, parent, false))

            override fun getItemCount(): Int = dataList.size

            override fun onBindViewHolder(holder: WidgetViewHolder, position: Int) {
                dataList[position].apply {
                    holder.itemView.tvContent.text = this
                    holder.itemView.setOnClickListener {
                        when (position) {
                            0 -> DoubleWaveActivity.startThis(this@WidgetActivity)
                            1 -> RecyclerViewItemDecorationActivity.startThis(this@WidgetActivity)
                            2 -> RecyclerViewDragActivity.startThis(this@WidgetActivity)
                            3 -> CalendarActivity.startThis(this@WidgetActivity)
                            4 -> SlideActivity.startThis(this@WidgetActivity)
                            5 -> TemperatureActivity.startThis(this@WidgetActivity)
                            6 -> ShockCircleActivity.startThis(this@WidgetActivity)
                            7 -> DashboardActivity.startThis(this@WidgetActivity)
                            8 -> RuleActivity.startThis(this@WidgetActivity)
                            9 -> RecordVoiceActivity.startThis(this@WidgetActivity)
                            10 -> WeiActivity.startThis(this@WidgetActivity)
                        }
                    }
                }
            }
        }

//        mRecyclerView.addItemDecoration()
    }

    inner class WidgetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private fun initData() {
        dataList.add("水波浪")
        dataList.add("RecyclerView分割线")
        dataList.add("RecyclerView拖动")
        dataList.add("日历")
        dataList.add("左右滑动")
        dataList.add("体温折线图")
        dataList.add("震动圆")
        dataList.add("仪表盘")
        dataList.add("刻度")
        dataList.add("录音")
        dataList.add("@，#功能的EditText")
    }
}