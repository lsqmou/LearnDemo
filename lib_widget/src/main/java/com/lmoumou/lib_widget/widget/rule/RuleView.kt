package com.lmoumou.lib_widget.widget.rule

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import com.lmoumou.lib_widget.R

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
        curIndex = dataList.indexOfFirst { it.isCurrent() }

        attrs?.let {
            val ta = context.obtainStyledAttributes(it, R.styleable.RuleView)
            for (i in 0 until ta.indexCount) {
                val attr = ta.getIndex(i)
                when (attr) {
                    R.styleable.RuleView_rv_space_line_color -> {
                        linePaint.color = ta.getColor(attr, Color.parseColor("#E4E7F4"))
                    }
                    R.styleable.RuleView_rv_space_line_size -> {
                        linePaint.strokeWidth = ta.getDimension(attr, 3F)
                    }
                    R.styleable.RuleView_rv_space_line_space -> {
                        lineSpace = ta.getDimensionPixelSize(attr, 150)
                    }
                    R.styleable.RuleView_rv_max_score -> {
                        maxScore = ta.getDimensionPixelSize(attr, 30)
                    }
                    R.styleable.RuleView_rv_text_color_normal -> {
                        textColorNormal = ta.getColor(attr, Color.parseColor("#999999"))
                    }
                    R.styleable.RuleView_rv_text_color_select -> {
                        textColorSelect = ta.getColor(attr, Color.parseColor("#FFA200"))
                    }
                    R.styleable.RuleView_rv_text_size_normal -> {
                        textSizeNormal = ta.getDimension(attr, 36F)
                    }
                    R.styleable.RuleView_rv_text_size_select -> {
                        textSizeSelect = ta.getDimension(attr, 45F)
                    }
                    R.styleable.RuleView_rv_fold_line_color -> {
                        foldPaint.color = ta.getColor(attr, Color.parseColor("#999999"))
                    }
                    R.styleable.RuleView_rv_fold_line_size -> {
                        foldPaint.strokeWidth = ta.getDimension(attr, 3F)
                    }
                    R.styleable.RuleView_rv_mark_color_select -> {
                        pointerPaint.color = ta.getColor(attr, Color.parseColor("#1AFFA200"))
                    }
                    R.styleable.RuleView_rv_point_color_select -> {
                        pointColorSelect = ta.getColor(attr, Color.parseColor("#FFA200"))
                    }
                    R.styleable.RuleView_rv_point_color_normal -> {
                        pointColorNormal = ta.getColor(attr, Color.parseColor("#999999"))
                    }
                    R.styleable.RuleView_rv_point_radius -> {
                        pointRadius = ta.getDimension(attr, 10F)
                    }
                    R.styleable.RuleView_rv_point_unit -> {
                        bottomUnit = ta.getString(attr) ?: ""
                    }
                }
            }
            ta.recycle()
        }


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
            /* RuleBeen("1", 0, false),
             RuleBeen("2", 2, false),
             RuleBeen("3", 3, false),
             RuleBeen("4", 4, false),
             RuleBeen("5", 5, false),
             RuleBeen("6", 6, false),
             RuleBeen("7", 7, false),
             RuleBeen("8", 8, true),
             RuleBeen("9", 9, false),
             RuleBeen("10", 10, false),
             RuleBeen("11", 11, false),
             RuleBeen("12", 12, false),
             RuleBeen("13", 13, false),
             RuleBeen("14", 14, false),
             RuleBeen("15", 30, false)*/
        )
    }
    //当前选中的位置
    private var curIndex: Int = 0

    //滚动距离
    private var scrollOff: Int = 0

    //相邻两条线的间距
    private var lineSpace: Int = 150

    //内容矩阵
    private val contentRectF by lazy { RectF() }
    //中间折线与直线绘制的矩阵
    private val mRectF by lazy { RectF() }

    //显示的最大分数值
    private var maxScore: Int = 30

    private var singleScore: Float = 0F

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w == 0 || h == 0) return
        contentRectF.set(
            paddingLeft.toFloat(),
            paddingTop.toFloat(),
            (w - paddingRight).toFloat(),
            (h - paddingBottom).toFloat()
        )
        mRectF.set(
            contentRectF.left,
            contentRectF.top + textRect.height() + 10,
            contentRectF.right,
            contentRectF.bottom - textRect.height() - 10
        )
        singleScore = mRectF.height() / maxScore
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
            color = Color.parseColor("#E4E7F4")
            strokeWidth = 3F
        }
    }

    //文字矩阵
    private val textRect by lazy { Rect() }
    //文字大小
    private var textSizeNormal = 36F
    private var textSizeSelect = 45F
    //文字颜色
    private var textColorNormal = Color.parseColor("#999999")
    private var textColorSelect = Color.parseColor("#FFA200")
    //单位
    private var bottomUnit: String = "分"
    //文本画笔
    private val textPaint = TextPaint().apply {
        isAntiAlias = true
        color = textColorNormal
        textAlign = Paint.Align.CENTER
        textSize = textSizeSelect
        typeface = Typeface.DEFAULT_BOLD
        getTextBounds("11.22分", 0, "11.22分".length, textRect)
    }

    //中间标记画笔
    private val pointerPaint by lazy {
        Paint().apply {
            color = Color.parseColor("#1AFFA200")
            isAntiAlias = true
            style = Paint.Style.FILL_AND_STROKE
        }
    }

    //区域
    private val regionPath by lazy { Path() }
    private val regionRectF by lazy { RectF() }

    //折线Path
    private val foldPath by lazy { Path() }
    //折线画笔
    private val foldPaint by lazy {
        Paint().apply {
            color = Color.parseColor("#999999")
            strokeWidth = 3F
            isAntiAlias = true
            style = Paint.Style.STROKE
        }
    }

    //折线点颜色
    private var pointColorSelect = Color.parseColor("#FFA200")
    private var pointColorNormal = Color.parseColor("#999999")
    //折线点半径
    private var pointRadius = 10F
    private val foldPointPaint by lazy {
        Paint().apply {
            color = pointColorNormal
            isAntiAlias = true
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (curIndex < 0) return

        canvas?.let {
            //中间向两边画
            //右边
            for (i in curIndex + 1 until dataList.size) {
                regionPath.reset()
                val data = dataList[i]
                val score = data.getRuleScore()
                val x = mRectF.centerX() + (i - curIndex - 1) * lineSpace + lineSpace / 2 + scrollOff

                regionPath.moveTo(x, contentRectF.top)
                regionPath.lineTo(x, contentRectF.bottom)
                regionPath.lineTo(x + lineSpace, contentRectF.bottom)
                regionPath.lineTo(x + lineSpace, contentRectF.top)
                regionPath.close()
                regionPath.computeBounds(regionRectF, true)

                data.getRegion().setPath(
                    regionPath, Region(
                        regionRectF.left.toInt(),
                        regionRectF.top.toInt(),
                        regionRectF.right.toInt(),
                        regionRectF.bottom.toInt()
                    )
                )


                it.drawLine(x, mRectF.top, x, mRectF.bottom, linePaint)

                textPaint.getTextBounds(data.getTopStr(), 0, data.getTopStr().length, textRect)
                it.drawText(data.getTopStr(), x + lineSpace / 2, contentRectF.top + textRect.height(), textPaint)
                textPaint.getTextBounds("$score$bottomUnit", 0, "$score$bottomUnit".length, textRect)
                it.drawText("$score$bottomUnit", x + lineSpace / 2, contentRectF.bottom, textPaint)

                val pointY = mRectF.bottom - score * singleScore
                data.getPoint().set((x + lineSpace / 2), pointY)
            }

            //左边
            for (i in curIndex downTo 0) {
                regionPath.reset()
                val data = dataList[i]
                val score = data.getRuleScore()
                val x = mRectF.centerX() - (curIndex - i) * lineSpace - lineSpace / 2 + scrollOff

                regionPath.moveTo(x, contentRectF.top)
                regionPath.lineTo(x, contentRectF.bottom)
                regionPath.lineTo(x + lineSpace, contentRectF.bottom)
                regionPath.lineTo(x + lineSpace, contentRectF.top)
                regionPath.close()
                regionPath.computeBounds(regionRectF, true)

                data.getRegion().setPath(
                    regionPath, Region(
                        regionRectF.left.toInt(),
                        regionRectF.top.toInt(),
                        regionRectF.right.toInt(),
                        regionRectF.bottom.toInt()
                    )
                )

                it.drawLine(x, mRectF.top, x, mRectF.bottom, linePaint)

                val pointY = mRectF.bottom - score * singleScore
                data.getPoint().set((x + lineSpace / 2), pointY)

                if (data.isCurrent()) {
                    textPaint.typeface = Typeface.DEFAULT_BOLD
                    textPaint.textSize = textSizeSelect
                    textPaint.color = textColorSelect
                } else {
                    textPaint.typeface = Typeface.DEFAULT
                    textPaint.textSize = textSizeNormal
                    textPaint.color = textColorNormal
                }

                textPaint.getTextBounds(data.getTopStr(), 0, data.getTopStr().length, textRect)
                it.drawText(data.getTopStr(), x + lineSpace / 2, contentRectF.top + textRect.height(), textPaint)
                textPaint.getTextBounds("$score$bottomUnit", 0, "$score$bottomUnit".length, textRect)
                it.drawText("$score$bottomUnit", x + lineSpace / 2, contentRectF.bottom, textPaint)
            }

            //画折线
            foldPath.reset()
            dataList.forEachIndexed { index, ruleBeenIm ->
                if (index == 0) {
                    foldPath.moveTo(ruleBeenIm.getPoint().x, ruleBeenIm.getPoint().y)
                } else {
                    foldPath.lineTo(ruleBeenIm.getPoint().x, ruleBeenIm.getPoint().y)
                }
                foldPointPaint.color = if (ruleBeenIm.isCurrent()) pointColorSelect else pointColorNormal
                it.drawCircle(ruleBeenIm.getPoint().x, ruleBeenIm.getPoint().y, pointRadius, foldPointPaint)
            }

            it.drawPath(foldPath, foldPaint)

            //画中间标记
            it.drawRect(
                contentRectF.centerX() - lineSpace / 2,
                0F,
                contentRectF.centerX() + lineSpace / 2,
                height.toFloat(),
                pointerPaint
            )
            foldPointPaint.color = pointColorSelect
            it.drawCircle(dataList[curIndex].getPoint().x, dataList[curIndex].getPoint().y, pointRadius, foldPointPaint)
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
                var distanceCopy = distance
                if (curIndex <= 0 && distanceCopy > 0) {
                    distanceCopy = 0
                }
                if (curIndex >= dataList.size - 1 && distanceCopy < 0) {
                    distanceCopy = 0
                }

                scrollOff += distanceCopy
                val offsetCount: Int = (scrollOff / lineSpace)
                if (offsetCount != 0) {
                    curIndex -= offsetCount
                    scrollOff -= offsetCount * lineSpace

                    dataList.forEach {
                        it.setCurrent(false)
                    }

                    if (curIndex < 0) {
                        curIndex = 0
                    }
                    if (curIndex > dataList.size - 1) {
                        curIndex = dataList.size - 1
                    }

                    if (curIndex in 0 until dataList.size)
                        dataList[curIndex].setCurrent(true)
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

            override fun onClick(x: Float, y: Float) {
                dataList.forEachIndexed { _, ruleBeenIm ->
                    ruleBeenIm.setCurrent(ruleBeenIm.judgeRegion(x.toInt(), y.toInt()))
                }

                var index = dataList.indexOfFirst { it.judgeRegion(x.toInt(), y.toInt()) }

                if (index == -1) {
                    index = curIndex
                    dataList[index].setCurrent(true)
                }

                var distance = (curIndex - index) * lineSpace

                if (curIndex <= 0 && distance > 0) {
                    distance = 0
                }
                if (curIndex >= dataList.size - 1 && distance < 0) {
                    distance = 0
                }
                scrollOff += distance
                val offsetCount: Int = (scrollOff / lineSpace)
                if (offsetCount != 0) {
                    curIndex -= offsetCount
                    scrollOff -= offsetCount * lineSpace
                }
                invalidate()
            }
        })
    }
    private var animator: ValueAnimator? = null
    private fun initAnimation(startValue: Int, endValue: Int) {
        animator = ValueAnimator.ofInt(startValue, endValue)
        animator?.duration = 5000
        animator?.interpolator = LinearInterpolator()
        animator?.addUpdateListener {
            val value: Int = it.animatedValue as Int
            scrollOff += value
            invalidate()
        }
        animator?.start()
    }

    fun setData(datas: MutableList<out RuleBeenIm>) {
        dataList.clear()
        dataList.addAll(datas)
        curIndex = dataList.indexOfFirst { it.isCurrent() }
        invalidate()
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