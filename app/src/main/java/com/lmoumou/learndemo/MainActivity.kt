package com.lmoumou.learndemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lmoumou.lib_calendar.CalendarActivity2
import com.lmoumou.lib_widget.ui.WidgetActivity
import com.soandso.lib_pdf.PDFActivity
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_main.view.*

class MainActivity : AppCompatActivity() {

    private val mInflater: LayoutInflater by lazy { LayoutInflater.from(this) }

    private val dataList: MutableList<String> by lazy { mutableListOf<String>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AndPermission.with(this)
            .runtime()
            .permission(
                arrayOf(
                    Permission.READ_EXTERNAL_STORAGE,
                    Permission.WRITE_EXTERNAL_STORAGE,
                    Permission.RECORD_AUDIO
                ))
            .start()
        initData()

        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = object : RecyclerView.Adapter<MainViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder =
                MainViewHolder(mInflater.inflate(R.layout.item_main, parent, false))

            override fun getItemCount(): Int = dataList.size

            override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
                dataList[position].apply {
                    holder.itemView.tvContent.text = this

                    holder.itemView.setOnClickListener {
                        when (position) {
                            0 -> {
                                WidgetActivity.startThis(this@MainActivity)
                            }
                            1 -> {
                                CalendarActivity2.startThis(this@MainActivity)
                            }
                            2 -> RichTextActivity.startThis(this@MainActivity)
                            3 -> PDFActivity.startThis(this@MainActivity)
                        }
                    }
                }
            }
        }
    }

    inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private fun initData() {
        dataList.add("自定义View")
        dataList.add("日历2222")
        dataList.add("富文本")
        dataList.add("pdf展示")
    }
}
