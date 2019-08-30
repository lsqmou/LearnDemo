package com.lmoumou.lib_widget.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import com.lmoumou.lib_widget.R

/**
 * @author Lmoumou
 * @date : 2019/8/30 16:39
 *
 * RecyclerView 水平分割线
 *
 * @param context
 * @param insetLeft 左边距
 * @param insetRight 有边距
 * @param dividerResId 分割线样式
 * @param isShowBootomDicider 是否展示底部分割线
 */
class HorizontalItemDecoration(
    private val context: Context,
    private val insetLeft: Int = 0,
    private val insetRight: Int = 0,
    private val dividerResId: Int = R.drawable.default_line,
    private val isShowBootomDicider: Boolean = true
) : RecyclerView.ItemDecoration() {

    private val mDivider: Drawable by lazy { ContextCompat.getDrawable(context, dividerResId)!! }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        outRect.set(0, 0, 0, mDivider.intrinsicHeight)
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        val left: Int = parent.paddingLeft
        val right: Int = parent.width - parent.paddingRight
        val childCount: Int = if (isShowBootomDicider) {
            parent.childCount
        } else {
            parent.childCount - 1
        }
        for (i in 0 until childCount) {


            val child = parent.getChildAt(i)

            val lp: RecyclerView.LayoutParams = child.layoutParams as RecyclerView.LayoutParams

            val top = child.bottom + lp.bottomMargin
            val bottom = top + mDivider.intrinsicHeight

            mDivider.setBounds(left + insetLeft, top, right - insetRight, bottom)
            mDivider.draw(c)
        }
    }

}