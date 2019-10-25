package com.lmoumou.lib_widget.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Scroller

/**
 * @author Lmoumou
 * @date : 2019/10/24 15:59
 */
class TemperatureView : View {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        mScroller = Scroller(context)
    }


    private var mScroller: Scroller

    //单元格宽高
    private var cellX = 120
    private var cellY = 60
    //单元格画笔
    private val cellPaint: Paint by lazy {
        Paint().apply {
            color = Color.parseColor("#979797")
            strokeWidth = cellLine
        }
    }
    //单元格线宽
    private var cellLine: Float = 4F

    //行列
    private var row = 16
    private var line = 10

    //控件宽高
    private var viewW = 0
    private var viewH = 0

    //折现路径
    private val path: Path = Path()
    //连接点的半径
    private var radius: Int = 12
    //折线连接点画笔
    private val cPaint by lazy {
        Paint().apply {
            style = Paint.Style.FILL
            color = Color.parseColor("#FE7E6D")
            strokeWidth = 5F
        }
    }

    //折线画笔
    private val tPaint by lazy {
        Paint().apply {
            style = Paint.Style.STROKE
            color = Color.parseColor("#FE7E6D")
            strokeWidth = 5F
        }
    }

    private var rowPoints: MutableList<Float> = mutableListOf()
    private val linePoints: MutableList<Float> = mutableListOf()
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        viewW = cellX * line
        viewH = cellY * row

        mTempScale = viewW / cellX / 2

        rowPoints.add(0F)
        rowPoints.add(0F + cellLine / 2)
        rowPoints.add(viewW.toFloat())
        rowPoints.add(0F + cellLine / 2)

        for (i in 1..(row - 1)) {
            rowPoints.add(0F)
            rowPoints.add(i * cellY.toFloat())
            rowPoints.add(viewW.toFloat())
            rowPoints.add(i * cellY.toFloat())
        }

        rowPoints.add(0F)
        rowPoints.add(row * cellY + cellLine / 2)
        rowPoints.add(viewW.toFloat())
        rowPoints.add(row * cellY + cellLine / 2)



        for (i in 0..line) {
            linePoints.add(i * cellX.toFloat())
            linePoints.add(0F)
            linePoints.add(i * cellX.toFloat())
            linePoints.add(viewH.toFloat())
        }


        setMeasuredDimension((viewW + cellLine).toInt(), (viewH + cellLine).toInt())
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            //画横线
            it.drawLines(rowPoints.toFloatArray(), cellPaint)
            //画竖线
            it.drawLines(linePoints.toFloatArray(), cellPaint)
            //画折线和连接点
            it.drawCircle(0F, cellY.toFloat(), radius.toFloat(), cPaint)
            it.drawCircle(cellX.toFloat(), cellY * 3.toFloat(), radius.toFloat(), cPaint)
//            tPaint.style=Paint.Style.STROKE
//            tPaint.strokeWidth=3F
            path.moveTo(0F, cellY.toFloat())
            path.lineTo(cellX.toFloat(), cellY * 3.toFloat())
            it.drawPath(path, tPaint)
        }
    }

    private var mScrollLastX = 0
    private var mCountScale: Int = line + 1 //滑动的总刻度
    private var mTempScale: Int = 0 // 用于判断滑动方向
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) return super.onTouchEvent(event)
        val x = event.x.toInt()
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (!mScroller.isFinished) {
                    mScroller.abortAnimation()
                }
                mScrollLastX = x
                true
            }
            MotionEvent.ACTION_MOVE -> {
                val dataX = mScrollLastX - x
                if (mCountScale - mTempScale < 0) {//向右滑动

                } else if (mCountScale - mTempScale < 0) {//向左滑动

                }
//                mScroller.startScroll(mScroller.finalX, mScroller.finalY, dataX, 0)
//                postInvalidate()
                this.scrollTo(dataX,0)
                true
            }
            else -> {
                super.onTouchEvent(event)
            }
        }
    }

    override fun computeScroll() {
        super.computeScroll()
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.currX, mScroller.currY)
//            invalidate()
        }
    }

}