package com.lmoumou.lib_widget.widget

import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator


/**
 * @author:Lmoumou
 * @date:2019/11/13
 * 描述:
 **/
class ShockCircleView : View {

    companion object {
        private const val TAG = "ShockCircleView"
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int?) : super(context, attrs)

    private val mPaint by lazy {
        Paint().apply {
            color = Color.BLACK
            isAntiAlias = true
            style = Paint.Style.FILL_AND_STROKE
        }
    }

    private var mAmplitude = 50

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            if (mPoint == null) return@let
            it.drawCircle(
                mPoint!!.x.toFloat(),
                mPoint!!.y.toFloat(),
                (width / 2 - mAmplitude).toFloat(),
                mPaint
            )

            if (mPoint2 == null) return@let
            it.drawCircle(
                mPoint2!!.x.toFloat(),
                mPoint2!!.y.toFloat(),
                (width / 2 - mAmplitude).toFloat(),
                mPaint
            )

            if (mPoint3 == null) return@let
            it.drawCircle(
                mPoint3!!.x.toFloat(),
                mPoint3!!.y.toFloat(),
                (width / 2 - mAmplitude).toFloat(),
                mPaint
            )
        }
    }

    private var mPoint: Point? = null
    private var animator: ValueAnimator? = null
    private fun initAnimation() {
        post {
            animator = ValueAnimator.ofObject(
                PointEvaluator(),
                Point(width / 2, height / 2),
                Point(width / 2, height / 2 - mAmplitude)
            )
            animator?.duration = 2000
            animator?.repeatCount = ValueAnimator.INFINITE
            animator?.repeatMode = ValueAnimator.REVERSE
            animator?.interpolator = LinearInterpolator()
            animator?.addUpdateListener {
                mPoint = it.animatedValue as Point

                invalidate()
            }
            animator?.start()
        }
    }

    private var mPoint2: Point? = null
    private var animator2: ValueAnimator? = null
    private fun initAnimation2() {
        post {
            animator2 = ValueAnimator.ofObject(
                PointEvaluator(),
                Point(width / 2, height / 2),
                Point((width / 2 - mAmplitude * Math.cos(Math.toRadians(30.0))).toInt(), (height / 2 + mAmplitude / 2))
            )

            animator2?.duration = 2000
            animator2?.repeatCount = ValueAnimator.INFINITE
            animator2?.repeatMode = ValueAnimator.REVERSE
            animator2?.interpolator = LinearInterpolator()
            animator2?.addUpdateListener {
                mPoint2 = it.animatedValue as Point
                invalidate()
            }
            animator2?.start()
        }
    }

    private var mPoint3: Point? = null
    private var animator3: ValueAnimator? = null
    private fun initAnimation3() {
        post {
            animator3 = ValueAnimator.ofObject(
                PointEvaluator(),
                Point(width / 2, height / 2),
                Point((width / 2 + mAmplitude * Math.cos(Math.toRadians(30.0))).toInt(), (height / 2 + mAmplitude / 2))
            )
            animator3?.duration = 2000
            animator3?.repeatCount = ValueAnimator.INFINITE
            animator3?.repeatMode = ValueAnimator.REVERSE
            animator3?.interpolator = LinearInterpolator()
            animator3?.addUpdateListener {
                mPoint3 = it.animatedValue as Point
                invalidate()
            }
            animator3?.start()
        }
    }

    fun startAnimation() {
        initAnimation()
        initAnimation2()
        initAnimation3()
    }

    inner class PointEvaluator : TypeEvaluator<Point> {
        override fun evaluate(fraction: Float, startValue: Point?, endValue: Point?): Point {
            if (startValue == null || endValue == null) {
                return Point(0, 0)
            }
            val x = (startValue.x + fraction * (endValue.x - startValue.x)).toInt()
            val y = (startValue.y + fraction * (endValue.y - startValue.y)).toInt()
            return Point(x, y)
        }
    }
}

