package com.lmoumou.lib_widget.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View


/**
 * @author Lmoumou
 * @date : 2019/10/24 15:17
 */
class SlideView : View {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        loadPicture()
    }

    private var x0: Float = 0f
    private var y0: Float = 0f
    var initialX: Float = 0F
    var initialY: Float = 0F
    var downX: Float = 0F
    var downY: Float = 0F
    private var pic: Bitmap? = null

    /**
     * 转载图片
     * */
    private fun loadPicture() {
        val opts: BitmapFactory.Options = BitmapFactory.Options()
        opts.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, com.lmoumou.lib_widget.R.drawable.pic, opts)
        opts.inSampleSize = computeSampleSize(opts, -1, 800 * 800)
        opts.inJustDecodeBounds = false
        pic = BitmapFactory.decodeResource(resources, com.lmoumou.lib_widget.R.drawable.pic, opts)
    }

    private fun computeSampleSize(opts: BitmapFactory.Options, minSideLength: Int, maxNumOfPixels: Int): Int {
        val initialSize = computeInitialSampleSize(opts, minSideLength, maxNumOfPixels)
        var roundedSize: Int
        if (initialSize <= 8) {
            roundedSize = 1
            while (roundedSize < initialSize) {
                roundedSize = roundedSize shl 1
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8
        }

        return roundedSize
    }

    private fun computeInitialSampleSize(options: BitmapFactory.Options, minSideLength: Int, maxNumOfPixels: Int): Int {
        val w = options.outWidth
        val h = options.outHeight

        val lowerBound = if (maxNumOfPixels == -1)
            1
        else
            Math.ceil(Math.sqrt((w * h / maxNumOfPixels).toDouble())).toInt()
        val upperBound = if (minSideLength == -1)
            128
        else
            Math.min(
                Math.floor((w / minSideLength).toDouble()),
                Math.floor((h / minSideLength).toDouble())
            ).toInt()

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound
        }

        return if (maxNumOfPixels == -1 && minSideLength == -1) {
            1
        } else if (minSideLength == -1) {
            lowerBound
        } else {
            upperBound
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            val displayWidth = width
            val displayHeight = height
            val left = x.toInt()
            val top = y.toInt()
            //图片显示区域
            val src = Rect(left, top, left + displayWidth, top + displayHeight)
            //屏幕显示区域
            val dst = RectF(0f, 0f, width.toFloat(), height.toFloat())

            canvas.drawBitmap(pic, src, dst, null)
            invalidate()
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) return super.onTouchEvent(event)
        val action = event.action
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                initialX = x0
                initialY = y0
                downX = event.x
                downY = event.y
            }
            MotionEvent.ACTION_MOVE,
            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL -> {
                x0 = initialX + event.x - downX
                y0 = initialY + event.y - downY
               this.scrollTo(-x0.toInt(),-y0.toInt())
            }
        }
        return true
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(2080, 2050)
    }

    override fun scrollTo(x: Int, y: Int) {
        Log.e("scrollTo", "SlideView")
        super.scrollTo(x, y)
    }

}