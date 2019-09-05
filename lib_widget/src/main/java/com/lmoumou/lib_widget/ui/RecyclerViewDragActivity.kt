package com.lmoumou.lib_widget.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.ViewGroup
import com.lmoumou.lib_widget.R
import com.lmoumou.lib_widget.widget.SpaceItemDecoration
import kotlinx.android.synthetic.main.activity_recyclerview_drag.*
import kotlinx.android.synthetic.main.item_decoration1.view.*
import java.util.*

/**
 * @author Lmoumou
 * @date : 2019/9/5 17:09
 */
class RecyclerViewDragActivity : AppCompatActivity() {

    companion object {
        fun startThis(context: Context) {
            context.startActivity(Intent(context.applicationContext, RecyclerViewDragActivity::class.java))
        }
    }

    private val mInflater: LayoutInflater by lazy { LayoutInflater.from(this) }
    private val dataList: MutableList<String> by lazy { mutableListOf<String>() }

    private val adapter: RecyclerView.Adapter<RecyclerView.ViewHolder> by lazy {
        object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
                object : RecyclerView.ViewHolder(mInflater.inflate(R.layout.item_drag, parent, false)) {}

            override fun getItemCount(): Int = dataList.size

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                holder.itemView.tvContent.text = dataList[position]
            }
        }
    }

    private val callBack: ItemTouchHelper.Callback by lazy {
        object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
                var swipFlag = 0
                var dragflag: Int = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                return makeMovementFlags(dragflag, swipFlag)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                adapter.notifyItemMoved(viewHolder.adapterPosition, target.adapterPosition)
                Collections.swap(dataList, viewHolder.adapterPosition, target.adapterPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, p1: Int) {

            }

            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)
            }

            override fun canDropOver(
                recyclerView: RecyclerView,
                current: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun isLongPressDragEnabled(): Boolean {
                return true
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerview_drag)

        initData()

        mRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mRecyclerView.addItemDecoration(SpaceItemDecoration(10, 10, 10))
        mRecyclerView.adapter = adapter

        val itemTouchHelper = ItemTouchHelper(callBack)
        itemTouchHelper.attachToRecyclerView(mRecyclerView)
    }

    private fun initData() {
        for (i in 0 until 10) {
            dataList.add("item->$i")
        }
    }
}