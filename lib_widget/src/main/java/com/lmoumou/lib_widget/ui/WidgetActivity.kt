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
    }
}