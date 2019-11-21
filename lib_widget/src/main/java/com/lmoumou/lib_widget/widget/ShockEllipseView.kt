package com.lmoumou.lib_widget.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation

/**
 * @author Lmoumou
 * @date : 2019/11/20 10:03
 */
class ShockEllipseView : View {
    companion object {
        private const val TAG = "ShockEllipseView"
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int?) : super(context, attrs)

    private val mPaint by lazy {
        Paint().apply {
            color = Color.BLACK
            isAntiAlias = true
            style = Paint.Style.FILL_AND_STROKE
            strokeWidth = 3F
        }
    }
    private val ovalF by lazy {
        RectF(
            (width / 2 - width/4).toFloat(),
            0F,
            (width / 2 + width/4).toFloat(),
            height.toFloat()
        )
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            it.drawOval(ovalF, mPaint)

            it.rotate(45F, (width / 2).toFloat(), (height / 2).toFloat())
            it.drawOval(ovalF, mPaint)

            it.rotate(90F, (width / 2).toFloat(), (height / 2).toFloat())
            it.drawOval(ovalF, mPaint)

            it.rotate(135F, (width / 2).toFloat(), (height / 2).toFloat())
            it.drawOval(ovalF, mPaint)

        }
    }

    fun startAnimator() {
        post {
            val anim = RotateAnimation(0F, 360F, Animation.RELATIVE_TO_SELF, 0.5F, Animation.RELATIVE_TO_SELF, 0.5F)
            anim.fillAfter = true
            anim.duration = 15000
            anim.repeatCount = ValueAnimator.INFINITE
            anim.interpolator = LinearInterpolator()
            startAnimation(anim)
        }
    }


}