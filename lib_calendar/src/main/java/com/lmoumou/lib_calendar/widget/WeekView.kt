package com.lmoumou.lib_calendar.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import com.lmoumou.lib_calendar.R
import com.lmoumou.lib_calendar.utils.CalendarUtil

/**
 * @author Lmoumou
 * @date : 2019/10/9 9:55
 */
class WeekView : View {
    companion object {
        const val TAG = "WeekView"
    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context, attrs, defStyleAttr)
    }

    private val weekArray = arrayOf("日", "一", "二", "三", "四", "五", "六")
    /**
     * 文字
     * */
    private var weekSize: Int = 12//文字尺寸
    private var weekColor = Color.BLACK//文字颜色

    private val mPaint: Paint by lazy {
        Paint().apply {
            color = weekColor
            isAntiAlias = true
            textSize = weekSize.toFloat()
        }
    }
    private lateinit var mContext: Context
    private fun initView(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) {
        this.mContext = context
        setBackgroundColor(Color.WHITE)
        attrs?.let {
            var weekStr: String? = null
            val ta: TypedArray = context.obtainStyledAttributes(it, R.styleable.WeekView)
            for (i in 0 until ta.indexCount) {
                val attr = ta.getIndex(i)
                when (attr) {
                    R.styleable.WeekView_week_color -> {
                        weekColor = ta.getColor(R.styleable.WeekView_week_color, weekColor)
                    }
                    R.styleable.WeekView_week_size -> {
                        weekSize = ta.getInt(R.styleable.WeekView_week_size, weekSize)
                    }
                    R.styleable.WeekView_week_str -> {
                        weekStr = ta.getString(R.styleable.WeekView_week_str)
                    }
                }
            }
            ta.recycle()

            if (!TextUtils.isEmpty(weekStr)) {
                val weeks = weekStr!!.split("\\.")
                if (weeks.size != 7) return
                System.arraycopy(weeks, 0, weekArray, 0, 7)
            }
            weekSize = CalendarUtil.getTextSize1(context, weekSize)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        var heightSize = View.MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        if (heightMode == View.MeasureSpec.AT_MOST) {
            heightSize = CalendarUtil.getPxSize(context, 35)
        }
        if (widthMode == View.MeasureSpec.AT_MOST) {
            widthSize = CalendarUtil.getPxSize(context, 300)
        }
        setMeasuredDimension(widthSize, heightSize)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val width = width
        val height = height
        val itemWidth = width / 7

        for (i in weekArray.indices) {
            val text = weekArray[i]
            val textWidth = mPaint.measureText(text).toInt()
            val startX = itemWidth * i + (itemWidth - textWidth) / 2
            val startY = (height / 2 - (mPaint.ascent() + mPaint.descent()) / 2).toInt()
            canvas?.drawText(text, startX.toFloat(), startY.toFloat(), mPaint)
        }
    }

}