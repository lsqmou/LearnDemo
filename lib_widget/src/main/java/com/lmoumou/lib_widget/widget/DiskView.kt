package com.lmoumou.lib_widget.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View

/**
 * @author Lmoumou
 * @date : 2019/11/21 9:26
 */
class DiskView : View {

    companion object {
        private const val TAG = "DiskView"
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int?) : super(context, attrs)


    private val arcRectF by lazy { RectF() }

    //指针路径
    private val pointerPath by lazy { Path() }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        arcRectF.set(0F, 0F, width.toFloat(), height.toFloat())

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            //画内带三角的圆
            drawPointer(it)
        }
    }

    //角度
    private var degree = 90.0

    //圆半径
    private var radius = 200F

    //三角形边长
    private var sideLength = 50

    //画笔
    private val pointPaint by lazy {
        Paint().apply {
            color = Color.BLUE
            isAntiAlias = true
            style = Paint.Style.FILL
            strokeWidth = 1F
            strokeCap = Paint.Cap.SQUARE
        }
    }

    private fun drawPointer(canvas: Canvas) {
        pointerPath.reset()

        canvas.drawCircle(arcRectF.centerX(), arcRectF.centerY(), radius, pointPaint)

        val t = Math.toDegrees(Math.asin((sideLength / 2 / radius).toDouble()))

        val angle1 = degree - t
        val angle2 = degree + t

        val point1X: Float = (arcRectF.centerX() - radius * Math.cos(Math.toRadians(angle1))).toFloat()
        val point1Y: Float = (arcRectF.centerY() - radius * Math.sin(Math.toRadians(angle1))).toFloat()

        val point2X: Float = (arcRectF.centerX() - radius * Math.cos(Math.toRadians(angle2))).toFloat()
        val point2Y: Float = (arcRectF.centerY() - radius * Math.sin(Math.toRadians(angle2))).toFloat()


        val length = radius * Math.cos(Math.toRadians(t)) + sideLength * Math.cos(Math.toRadians(30.0))
        val point3X: Float = (arcRectF.centerX() - length * Math.cos(Math.toRadians(degree))).toFloat()
        val point3Y: Float = (arcRectF.centerX() - length * Math.sin(Math.toRadians(degree))).toFloat()

        pointerPath.moveTo(point1X,point1Y)
        pointerPath.lineTo(point2X,point2Y)
        pointerPath.lineTo(point3X,point3Y)
        pointerPath.close()
        canvas.drawPath(pointerPath,pointPaint)



    }

}