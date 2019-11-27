package com.lmoumou.lib_widget.widget.rule

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.lmoumou.lib_widget.entity.RuleBeen

/**
 * @author Lmoumou
 * @date : 2019/11/27 16:46
 */
class RuleView : View {

    companion object {
        private const val TAG = "RuleView"
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int?) : super(context, attrs) {
        textPaint
        curIndex = dataList.indexOfFirst { it.isCurrent() }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val viewWith = measureWidth(widthMeasureSpec)
        val viewHeight = measureHeight(heightMeasureSpec)
        setMeasuredDimension(viewWith, viewHeight)
    }

    //数据源
    private val dataList: MutableList<RuleBeenIm> by lazy {
        mutableListOf<RuleBeenIm>(
            RuleBeen(false),
            RuleBeen(false),
            RuleBeen(false),
            RuleBeen(false),
            RuleBeen(false),
            RuleBeen(false),
            RuleBeen(false),
            RuleBeen(true),
            RuleBeen(false),
            RuleBeen(false),
            RuleBeen(false),
            RuleBeen(false),
            RuleBeen(false),
            RuleBeen(false),
            RuleBeen(false)
        )
    }
    //当前选中的位置
    private var curIndex: Int = 0

    //滚动距离
    private var scrollOff: Int = 0

    //相邻两条线的间距
    private var lineSpace: Int = 150
    //左间距
    private var leftSpace = 0F
    //上间距
    private var topSpace = 39F
    //右间距
    private var rightSpace = 0F
    //下间距
    private var bottomSpace = 39F

    //中间折线与直线绘制的矩阵
    private val mRectF by lazy { RectF() }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w == 0 || h == 0) return
        mRectF.set(leftSpace, topSpace, w - rightSpace, height - bottomSpace)
    }

    /**
     * 测量宽
     *
     * @param widthMeasureSpec
     * @return view的宽
     * */
    private fun measureWidth(widthMeasureSpec: Int): Int {
        val spaceSize = MeasureSpec.getSize(widthMeasureSpec)
        val spaceModel = MeasureSpec.getMode(widthMeasureSpec)
        return when (spaceModel) {
            MeasureSpec.EXACTLY -> spaceSize
            else -> getScreenWith(context)
        }
//        return dataList.size * lineSpace
    }

    /**
     * 测量高
     *
     * @param heightMeasureSpec
     * @return view的高
     * */
    private fun measureHeight(heightMeasureSpec: Int): Int {
        val spaceSize = MeasureSpec.getSize(heightMeasureSpec)
        val spaceModel = MeasureSpec.getMode(heightMeasureSpec)
        return when (spaceModel) {
            MeasureSpec.EXACTLY -> spaceSize
            else -> 750
        }
    }

    /**---------------------------------------------以上是测量部分-------------------------------------------------------------*/

    //垂直分割线画笔
    private val linePaint by lazy {
        Paint().apply {
            isAntiAlias = true
            color = Color.RED
            strokeWidth = 3F
        }
    }

    //文字矩阵
    private val textRect by lazy { Rect() }
    //文本画笔
    private val textPaint by lazy {
        TextPaint().apply {
            isAntiAlias = true
            color = Color.YELLOW
            textSize = 45F
            getTextBounds("11.22", 0, "11.22".length, textRect)
        }
    }

    //中间标记画笔
    private val pointerPaint by lazy {
        Paint().apply {
            color = Color.GREEN
            isAntiAlias = true
            style = Paint.Style.FILL_AND_STROKE
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {

            //中间向两边画
            //右边
            linePaint.color = Color.BLACK
            for (i in curIndex + 1 until dataList.size) {
                val x = mRectF.centerX() + (i - curIndex - 1) * lineSpace + lineSpace / 2 + scrollOff
                it.drawLine(x, mRectF.top, x, mRectF.bottom, linePaint)
            }

            //左边
            linePaint.color = Color.RED
            for (i in curIndex downTo 0) {
                val x = mRectF.centerX() - (curIndex - i) * lineSpace - lineSpace / 2 + scrollOff
                it.drawLine(x, mRectF.top, x, mRectF.bottom, linePaint)
            }
            it.drawCircle(mRectF.centerX(), mRectF.centerY(), 50F, pointerPaint)
        }
    }

    private var mDownFocusX: Float = 0F
    private var mDownFocusY: Float = 0F
    private var isDisallowIntercept: Boolean = false
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) return super.onTouchEvent(event)

        if (!isEnabled) return true
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mDownFocusX = event.x
                mDownFocusY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                if (!isDisallowIntercept && Math.abs(event.y - mDownFocusY) < Math.abs(event.x - mDownFocusX)) {
                    isDisallowIntercept = true
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true)
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                if (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(false)
                }
                isDisallowIntercept = false
            }
            MotionEvent.ACTION_CANCEL -> {
                if (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(false)
                }
                isDisallowIntercept = false
            }
        }
        return scroller.onTouchEvent(event)
    }

    // 是否执行滚动
    private var isScrollingPerformed: Boolean = false
    private val scroller: RuleScroller by lazy {
        RuleScroller(context, object : RuleScroller.ScrollingListener {
            override fun onScroll(distance: Int) {
                scrollOff += distance
                val offsetCount: Int = (scrollOff / lineSpace).toInt()
                if (offsetCount != 0) {
                    curIndex -= offsetCount
                    scrollOff -= offsetCount * lineSpace
                }

                invalidate()
            }

            override fun onStarted() {
                isScrollingPerformed = true
            }

            override fun onJustify() {
                when {
                    scrollOff <= -lineSpace / 2 -> {
                        scroller.scroll((lineSpace + scrollOff), 0)
                    }
                    scrollOff > lineSpace / 2 -> {
                        scroller.scroll((scrollOff - lineSpace), 0)
                    }
                    else -> {
                        scroller.scroll(scrollOff, 0)
                    }
                }
            }

            override fun onFinished() {
                if (isScrollingPerformed) {
                    isScrollingPerformed = false
                }
                scrollOff = 0
                invalidate()
            }
        })
    }


    /**
     * 获取屏幕宽
     *
     * @param context
     * */
    private fun getScreenWith(context: Context): Int = context.resources.displayMetrics.widthPixels

    /**
     * 获取屏幕高度
     *
     * @param context
     * */
    private fun getScreenHeight(context: Context): Int = context.resources.displayMetrics.heightPixels
}