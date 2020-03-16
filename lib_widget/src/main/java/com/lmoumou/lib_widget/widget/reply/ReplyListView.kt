package com.lmoumou.lib_widget.widget.reply

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.LinearLayout
import android.widget.TextView

/**
 * @author Lmoumou
 * @date : 2019/12/23 11:12
 */
class ReplyListView<T : ReplyIm> : LinearLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        orientation = LinearLayout.VERTICAL

        val lp =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        for (i in 0 until (maxCount + 1)) {
            val textView = TextView(context)
            textView.layoutParams = lp
            textView.setTextColor(textColor)
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
            addView(textView)
        }
    }

    // 数据源
    private val dataList: ArrayList<T> by lazy { arrayListOf<T>() }
    //最大展示数量
    private var maxCount = 2
    //字体颜色
    private var textColor = Color.parseColor("#41484D")
    //字体大小
    private var textSize: Float = 13F

    private fun upDateView() {
        post {
            val dataSize = dataList.size


        }
    }
}