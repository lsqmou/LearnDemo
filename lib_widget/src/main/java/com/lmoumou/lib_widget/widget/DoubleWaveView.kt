package com.lmoumou.lib_widget.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator


/**
 * @author Lmoumou
 * @date : 2019/8/30 10:51
 */
class DoubleWaveView : View {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int?) : super(context, attrs)

    private val mPaint: Paint by lazy {
        Paint().apply {
            style = Paint.Style.FILL_AND_STROKE
        }
    }
    private val mPath: Path by lazy { Path() }

    private val widths: Int by lazy { width }
    private val heights: Int by lazy { height }
    private var dx: Int = 0

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mPath.reset()
        mPaint.color = Color.parseColor("#1affffff")

        mPath.moveTo((-widths + dx).toFloat(), (heights / 5 * 2).toFloat())
        for (i in 0 until 3) {
            mPath.rQuadTo((widths / 4).toFloat(), -50F, (widths / 2).toFloat(), 0F)
            mPath.rQuadTo((widths / 4).toFloat(), 50F, (widths / 2).toFloat(), 0F)
        }
        mPath.lineTo(widths.toFloat(), heights.toFloat())
        mPath.lineTo(0F, heights.toFloat())
        mPath.close()
        canvas.drawPath(mPath, mPaint)

        mPaint.reset()
        mPaint.color = Color.parseColor("#1affffff")
        mPath.moveTo((-widths + dx).toFloat(), (heights / 5 * 2).toFloat())
        for (i in 0 until 3) {
            mPath.rQuadTo((widths / 4).toFloat(), 50F, (widths / 2).toFloat(), 0F)
            mPath.rQuadTo((widths / 4).toFloat(), -50F, (widths / 2).toFloat(), 0F)
        }
        mPath.lineTo(widths.toFloat(), heights.toFloat())
        mPath.lineTo(0F, heights.toFloat())
        mPath.close()
        canvas.drawPath(mPath, mPaint)
    }

    fun startAnimation() {
        post {
            ValueAnimator.ofInt(0, widths).apply {
                duration = 3000
                repeatCount = ValueAnimator.INFINITE
                interpolator = LinearInterpolator()
                addUpdateListener {
                    dx = it.animatedValue as Int
                    invalidate()
                }
                start()
            }
        }

    }

}