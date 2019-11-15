package com.lmoumou.learndemo

import android.content.Context
import android.text.Spannable
import android.text.util.Linkify
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.TextView

/**
 * @author Lmoumou
 * @date : 2019/11/14 10:50
 */
class LinkifyTextView : TextView {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.autoLinkMask = Linkify.ALL
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) super.onTouchEvent(event)
        val widget: TextView = this
        val text = widget.text
        if (text is Spannable) {
            val buffer = text
            val action = event!!.action
            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
                var x = event.x.toInt()
                var y = event.y.toInt()

                x-=widget.totalPaddingLeft
                y-=widget.totalPaddingTop

                x+=widget.scaleX.toInt()
                y+=widget.scaleY.toInt()

            }
        }
        return false
    }
}