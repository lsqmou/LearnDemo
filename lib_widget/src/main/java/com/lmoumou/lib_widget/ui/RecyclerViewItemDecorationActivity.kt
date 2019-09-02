package com.lmoumou.lib_widget.ui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.lmoumou.lib_widget.R
import com.lmoumou.lib_widget.widget.SpaceItemDecoration
import com.lmoumou.lib_widget.widget.SpaceItemDecoration1
import kotlinx.android.synthetic.main.activity_recyclerview_itemdecoration.*
import kotlinx.android.synthetic.main.item_decoration1.view.*

/**
 * @author Lmoumou
 * @date : 2019/8/30 16:16
 */
class RecyclerViewItemDecorationActivity : AppCompatActivity() {

    companion object {
        fun startThis(context: Context) {
            context.startActivity(Intent(context.applicationContext, RecyclerViewItemDecorationActivity::class.java))
        }
    }

    private val mInflater: LayoutInflater by lazy { LayoutInflater.from(this) }

    private val dataList: MutableList<String> by lazy { mutableListOf<String>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerview_itemdecoration)

        initData()

//        rcv1.addItemDecoration(
//            HorizontalItemDecoration(
//                this,
//                50,
//                300,
//                dividerResId = R.drawable.test_line,
//                isShowBootomDicider = false
//            )
//        )

        rcv1.addItemDecoration(SpaceItemDecoration1(10,50,10, Color.RED))

        rcv1.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
                object : RecyclerView.ViewHolder(mInflater.inflate(R.layout.item_decoration1, parent, false)) {}

            override fun getItemCount(): Int = dataList.size

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                dataList[position].apply {
                    holder.itemView.tvContent.text = this
                }
            }
        }

        rcv2.layoutManager = GridLayoutManager(this, 4)
        rcv2.addItemDecoration(SpaceItemDecoration1(0,50,10, Color.RED))
//        rcv2.addItemDecoration(SpaceItemDecoration(10, 10, 10, Color.RED))
        rcv2.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
                object : RecyclerView.ViewHolder(mInflater.inflate(R.layout.item_decoration1, parent, false)) {}

            override fun getItemCount(): Int = dataList.size

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                dataList[position].apply {
                    holder.itemView.tvContent.text = this
                }
            }
        }


    }

    private fun initData() {
        for (i in 0 until 10) {
            dataList.add("Item->$i")
        }
    }

}