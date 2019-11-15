package com.lmoumou.learndemo

import android.text.Selection
import android.text.Spannable
import android.text.method.BaseMovementMethod
import android.text.style.ClickableSpan
import android.view.MotionEvent
import android.widget.TextView

/**
 * @author Lmoumou
 * @date : 2019/11/14 10:31
 */
class ClickableMovementMethod : BaseMovementMethod() {

    companion object {
        @Volatile
        private var instance: ClickableMovementMethod? = null

        fun getInstance(): ClickableMovementMethod {
            return instance ?: synchronized(this) {
                instance ?: ClickableMovementMethod()
            }
        }
    }

    override fun onTouchEvent(widget: TextView, buffer: Spannable, event: MotionEvent): Boolean {
        val action = event.actionMasked
        var x = event.x.toInt()
        var y = event.y.toInt()
        x -= widget.totalPaddingLeft
        y -= widget.totalPaddingTop
        x += widget.scaleX.toInt()
        y += widget.scaleY.toInt()

        val layout = widget.layout
        val line = layout.getLineForVertical(y)
        val off = layout.getOffsetForHorizontal(line, x.toFloat())

        val link = buffer.getSpans(off, off, ClickableSpan::class.java)
        if (link.isNotEmpty()) {
            if (action == MotionEvent.ACTION_UP) {
                link[0].onClick(widget)
            } else {
                Selection.setSelection(buffer, buffer.getSpanStart(link[0]), buffer.getSpanEnd(link[0]))
            }
            return true
        } else {
            Selection.removeSelection(buffer)
        }
        return false

    }
}