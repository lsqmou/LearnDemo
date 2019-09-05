package com.lmoumou.lib_widget.widget

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View

/**
 * @author Lmoumou
 * @date : 2019/9/2 10:36
 *
 * @param leftSpace 左间距（GridLayoutManager时无效）
 * @param rightSpace 右间距
 * @param topSpace 上间距
 * @param spaceColor 间距间填充色
 * @param isShowBottom 是否展示底部间距
 */
class SpaceItemDecoration(
    private val leftSpace: Int,
    private val rightSpace: Int,
    private val topSpace: Int,
    private val spaceColor: Int = Color.GRAY,
    private val isShowBottom: Boolean = true
) : RecyclerView.ItemDecoration() {

    private var mSpanCount: Int = 0
    private val mPaint: Paint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = spaceColor
            style = Paint.Style.FILL
        }
    }
    private var mMaxSpanGroupIndex: Int = 0

    /**
     * 获取Item的偏移量
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        // 获取位置
        val position = parent.getChildAdapterPosition(view)
        view.tag = position

        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            val gridLayoutManager = layoutManager as GridLayoutManager?
            val spanSizeLookup = gridLayoutManager!!.spanSizeLookup
            mSpanCount = gridLayoutManager.spanCount
            mMaxSpanGroupIndex = spanSizeLookup.getSpanGroupIndex(parent.adapter!!.itemCount - 1, mSpanCount)
            val spanSize = spanSizeLookup.getSpanSize(position)
            val spanIndex = spanSizeLookup.getSpanIndex(position, mSpanCount)
            val spanGroupIndex = spanSizeLookup.getSpanGroupIndex(position, mSpanCount)
            Log.d(TAG, "getItemOffsets: spanIndex:$spanIndex")
//            if (spanSize < mSpanCount && spanIndex != 0) {
//                // 左边需要偏移
//                outRect.left = leftSpace
//            }

            if ((spanIndex + 1) != mSpanCount && spanIndex != 0) {
                outRect.right = rightSpace / 2
                outRect.left = rightSpace / 2
            }

            if (spanGroupIndex != 0) {
                outRect.top = topSpace
            }
        } else if (layoutManager is LinearLayoutManager) {
            val linearLayoutManager = layoutManager as LinearLayoutManager?
//            if (position != 0) {
            if (linearLayoutManager!!.orientation == LinearLayoutManager.HORIZONTAL) {
                outRect.left = leftSpace
            } else {
                if (position != 0) {
                    outRect.bottom = topSpace
                }
                outRect.left = leftSpace
                outRect.right = rightSpace
            }
//            }
        }
    }

    /**
     * 绘制分割线
     * @param c
     * @param parent
     * @param state
     */
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            drawSpace(c, parent)
        } else if (layoutManager is LinearLayoutManager) {
            val linearLayoutManager = layoutManager as LinearLayoutManager?
            if (linearLayoutManager!!.orientation == LinearLayoutManager.HORIZONTAL) {
                // 画竖直分割线
                drawVertical(c, parent)
            } else {
                // 画横向分割线
                drawHorizontal(c, parent)
            }
        }
    }

    private fun drawHorizontal(c: Canvas, parent: RecyclerView) {
        // 画横向分割线
        val linearLayoutManager = parent.layoutManager as LinearLayoutManager?
        var top: Int
        var bottom: Int
        var left: Int
        var right: Int
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val position = child.tag as Int
            // 判断是否位于边缘
            if (position == linearLayoutManager!!.itemCount - 1 && !isShowBottom) continue
            val layoutParams = child.layoutParams as RecyclerView.LayoutParams

            top = child.bottom + layoutParams.bottomMargin
            bottom = top + topSpace
            left = child.left - layoutParams.leftMargin
            right = child.right + layoutParams.rightMargin
            c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mPaint)
        }
    }

    private fun drawVertical(c: Canvas, parent: RecyclerView) {
        // 画竖直分割线
        val linearLayoutManager = parent.layoutManager as LinearLayoutManager?
        var top: Int
        var bottom: Int
        var left: Int
        var right: Int
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val position = child.tag as Int
            // 判断是否位于边缘
            if (position == 0) continue
            val layoutParams = child.layoutParams as RecyclerView.LayoutParams
            top = child.top - layoutParams.topMargin
            bottom = child.bottom + layoutParams.bottomMargin
            left = child.left - layoutParams.leftMargin - leftSpace
            right = left + leftSpace
            c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mPaint)
        }
    }

    /**
     * 绘制分割线
     * @param canvas
     * @param parent
     */
    private fun drawSpace(canvas: Canvas, parent: RecyclerView) {
        val gridLayoutManager = parent.layoutManager as GridLayoutManager?
        val spanCount = gridLayoutManager!!.spanCount
        val spanSizeLookup = gridLayoutManager.spanSizeLookup
        val childCount = parent.childCount
        var top: Int
        var bottom: Int
        var left: Int
        var right: Int
        for (i in 0 until childCount) {
            // 绘制思路，以绘制bottom和left为主，top和right不绘制，需要判断出当前的item是否位于边缘，位于边缘的item不绘制bottom和left，你懂得
            val child = parent.getChildAt(i)
            val position = child.tag as Int
            val layoutParams = child.layoutParams as RecyclerView.LayoutParams

            val spanGroupIndex = spanSizeLookup.getSpanGroupIndex(position, spanCount)
            val spanSize = spanSizeLookup.getSpanSize(position)
            val spanIndex = spanSizeLookup.getSpanIndex(position, spanCount)
            Log.e(TAG, "postion->$position,spanIndex->$spanIndex,mSpanCount->$mSpanCount,spanSize->$spanSize")

            if (isShowBottom) {
                top = child.bottom + layoutParams.bottomMargin
                bottom = top + topSpace
                left = child.left - layoutParams.leftMargin // 不需要外边缘
                right = child.right + layoutParams.rightMargin + rightSpace
                canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mPaint)
            } else {
                // 画bottom分割线,如果没到达底部，绘制bottom
                if (spanGroupIndex < mMaxSpanGroupIndex) {
                    top = child.bottom + layoutParams.bottomMargin
                    bottom = top + topSpace
                    left = child.left - layoutParams.leftMargin // 不需要外边缘
                    right = child.right + layoutParams.rightMargin + rightSpace
                    canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mPaint)
                }
            }


            //画right分割线
            if ((spanIndex + 1) != spanCount) {//不在最右边的都要画
                top = child.top - layoutParams.topMargin
                bottom = child.bottom + layoutParams.bottomMargin
                right = child.right + layoutParams.rightMargin + rightSpace
                left = right - rightSpace
                canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mPaint)
            }


            // 画left分割线
//            if (spanSize != mSpanCount && spanIndex != 0) {
//                // 左边需要分割线，开始绘制
//                top = child.top - layoutParams.topMargin
//                bottom = child.bottom + layoutParams.bottomMargin
//                left = child.left - layoutParams.leftMargin - leftSpace
//                right = left + leftSpace
//                canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mPaint)
//            }
        }
    }

    companion object {

        private const val TAG = "SpaceItemDecoration1"
    }

}