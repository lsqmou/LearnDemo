package com.lmoumou.lib_widget.widget

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * @author Lmoumou
 * @date : 2019/9/2 10:36
 */
class SpaceItemDecoration(
    private val leftSpace: Int,
    private val rightSpace: Int,
    private val topSpace: Int,
    private val spaceColor: Int = Color.GRAY,
    private val isBottom: Boolean = true
) : RecyclerView.ItemDecoration() {

    private var mSpanCount: Int = 0
    private var mMaxSpanGroupIndex: Int = 0

    private val mPaint: Paint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = spaceColor
            style = Paint.Style.FILL
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        //获取位置
        val position = parent.getChildAdapterPosition(view)
        view.tag = position

        val layoutManager = parent.layoutManager

        if (layoutManager is GridLayoutManager) {
            val spanSizeLookup = layoutManager.spanSizeLookup
            mSpanCount = layoutManager.spanCount
            mMaxSpanGroupIndex = spanSizeLookup.getSpanGroupIndex(parent.adapter?.itemCount ?: 0 - 1, mSpanCount)
            val spanSize = spanSizeLookup.getSpanSize(position)
            val spanIndex = spanSizeLookup.getSpanIndex(position, mSpanCount)
            val spanGroupIndex = spanSizeLookup.getSpanGroupIndex(position, mSpanCount)
            if (spanSize < mSpanCount && spanIndex != 0) {
                // 左边需要偏移
                outRect.left = leftSpace
            }
            if (spanGroupIndex != 0) {
                //上偏移
                outRect.top = topSpace
            }
        } else if (layoutManager is LinearLayoutManager) {
            if (position != 0) {
                if (layoutManager.orientation == LinearLayoutManager.HORIZONTAL) {
                    outRect.left = leftSpace
                } else {
                    outRect.top = topSpace
                }
            }
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            drawSpace(c, layoutManager, parent)
        } else if (layoutManager is LinearLayoutManager) {

        }
    }

    private fun drawSpace(canvas: Canvas, layoutManager: GridLayoutManager, parent: RecyclerView) {
        val spanCount = layoutManager.spanCount
        val spanSizeLookup = layoutManager.spanSizeLookup
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val position = child.tag as Int
            val layoutParams = child.layoutParams as RecyclerView.LayoutParams
            val spanGroupIndex = spanSizeLookup.getSpanGroupIndex(position, spanCount)
            val spanSize = spanSizeLookup.getSpanSize(position)
            val spanIndex = spanSizeLookup.getSpanIndex(position, spanCount)
            // 画bottom分割线,如果没到达底部，绘制bottom
            if (spanGroupIndex < mMaxSpanGroupIndex) {
                val top = child.bottom + layoutParams.bottomMargin
                val bottom = top + topSpace
                val left = child.left - layoutParams.leftMargin // 不需要外边缘
                val right = child.right + layoutParams.rightMargin + rightSpace
                canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mPaint)
            }
            // 画left分割线
            if (spanSize != mSpanCount && spanIndex != 0) {
                // 左边需要分割线，开始绘制
                val top = child.top - layoutParams.topMargin
                val bottom = child.bottom + layoutParams.bottomMargin
                val left = child.left - layoutParams.leftMargin - leftSpace
                val right = left + rightSpace
                canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mPaint)
            }
        }
    }

}