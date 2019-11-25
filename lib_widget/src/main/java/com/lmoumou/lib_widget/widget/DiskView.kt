package com.lmoumou.lib_widget.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.lmoumou.lib_widget.R
import com.lmoumou.lib_widget.entity.DiaskBeen
import com.lmoumou.lib_widget.entity.DiskIm

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
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int?) : super(context, attrs) {
        averageAngle = sweepAngle / (dataList.size)

        attrs?.let {
            val ta = context.obtainStyledAttributes(it, R.styleable.DiskView)
            for (i in 0 until ta.indexCount) {
                val attr = ta.getIndex(i)
                when (attr) {
                    R.styleable.DiskView_dv_pointer_color -> {
                        pointPaint.color = ta.getColor(attr, Color.WHITE)
                    }
                    R.styleable.DiskView_dv_scale_color -> {
                        scaleColor = ta.getColor(attr, Color.BLUE)
                    }
                    R.styleable.DiskView_dv_text_size_normal -> {
                        textSizeNormal = ta.getDimension(attr, 13F)
                    }
                    R.styleable.DiskView_dv_text_size_select -> {
                        textSizeSelect = ta.getDimension(attr, 13F)
                    }
                }
            }
        }

    }

    //外刻度盘
    private val arcRectF by lazy { RectF() }

    //内指针
    private val pointerRectF by lazy { RectF() }

    //指针路径
    private val pointerPath by lazy { Path() }

    private val dataList: MutableList<DiskIm> by lazy {
        mutableListOf<DiskIm>(
            DiaskBeen("01", isSelect = true),
            DiaskBeen("02"),
            DiaskBeen("03"),
            DiaskBeen("04"),
            DiaskBeen("05"),
            DiaskBeen("06"),
            DiaskBeen("07"),
            DiaskBeen("08"),
            DiaskBeen("09"),
            DiaskBeen("10"),
            DiaskBeen("11"),
            DiaskBeen("12"),
            DiaskBeen("13"),
            DiaskBeen("14"),
            DiaskBeen("15"),
            DiaskBeen("16"),
            DiaskBeen("17"),
            DiaskBeen("18"),
            DiaskBeen("19"),
            DiaskBeen("20"),
            DiaskBeen("21"),
            DiaskBeen("22"),
            DiaskBeen("23"),
            DiaskBeen("24"),
            DiaskBeen("25"),
            DiaskBeen("26"),
            DiaskBeen("27"),
            DiaskBeen("28"),
            DiaskBeen("29"),
            DiaskBeen("30")
        )
    }

    private var averageAngle = 0F

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val with = measureWidth(widthMeasureSpec)

        val height = measureHeight(heightMeasureSpec)

        val size = Math.min(with, height)
        setMeasuredDimension(size, size)

    }

    private fun measureWidth(measureSpec: Int): Int {
        val result: Int
        val specModel = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        result = when (specModel) {
            MeasureSpec.EXACTLY -> specSize
            else -> Int.MAX_VALUE
        }
        return result
    }

    private fun measureHeight(measureSpec: Int): Int {
        val result: Int
        val specModel = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        result = when (specModel) {
            MeasureSpec.EXACTLY -> specSize
            else -> Int.MAX_VALUE
        }
        return result
    }

    private val innerArcRectF by lazy { RectF() }
    private val outerArcRectF by lazy { RectF() }
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        outerArcRectF.set(0F + 20, 0F + 20, width.toFloat() - 20, height.toFloat() - 20)
        arcRectF.set(
            0F + arcPaint.strokeWidth / 2 + 20,
            0F + arcPaint.strokeWidth / 2 + 20,
            width.toFloat() - arcPaint.strokeWidth / 2 - 20,
            height.toFloat() - arcPaint.strokeWidth / 2 - 20
        )

        innerArcRectF.set(
            arcPaint.strokeWidth + 20,
            arcPaint.strokeWidth + 20,
            width - arcPaint.strokeWidth - 20,
            height - arcPaint.strokeWidth - 20
        )

        val dfValue = arcPaint.strokeWidth + 20 + 20
        pointerRectF.set(0 + dfValue, 0 + dfValue, width - dfValue, height - dfValue)

        val x = sideLength / 2
        val y = pointerRectF.width() / 2 - sideLength * Math.cos(Math.toRadians(30.0))

        radius = Math.sqrt(Math.pow(x.toDouble(), 2.0) + Math.pow(y, 2.0)).toFloat()

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            //画外圈刻度盘
            drawDial(it)

            //画内带三角的圆
            drawPointer(it)
        }
    }

    private var scaleColor = Color.WHITE
    private val arcPaint by lazy {
        Paint().apply {
            color = scaleColor
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = width / 8F
            strokeCap = Paint.Cap.ROUND
        }
    }

    private val arcPaint2 by lazy {
        Paint().apply {
            color = Color.GRAY
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = width / 8F
            strokeCap = Paint.Cap.ROUND
            maskFilter = BlurMaskFilter(20F, BlurMaskFilter.Blur.SOLID)
        }
    }

    private val mPaint by lazy {
        Paint().apply {
            color = Color.BLUE
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = width / 8F
            strokeCap = Paint.Cap.BUTT
        }
    }

    //画笔
    private val pointPaint by lazy {
        Paint().apply {
            color = Color.BLUE
            isAntiAlias = true
            style = Paint.Style.FILL
            strokeWidth = 1F
            strokeCap = Paint.Cap.SQUARE
//            maskFilter = BlurMaskFilter(20F, BlurMaskFilter.Blur.SOLID)
        }
    }

    private val pointPaint2 by lazy {
        Paint().apply {
            color = Color.GRAY
            isAntiAlias = true
            style = Paint.Style.FILL
            strokeWidth = 1F
            strokeCap = Paint.Cap.SQUARE
            maskFilter = BlurMaskFilter(20F, BlurMaskFilter.Blur.SOLID)
        }
    }

    private val startAngle = 290F
    private val sweepAngle = 320F

    private var textSizeNormal = 45F
    private var textSizeSelect = 45F

    private val textPaint by lazy {
        TextPaint().apply {
            color = Color.BLACK
            textSize = textSizeNormal
            style = Paint.Style.FILL
            textAlign = Paint.Align.CENTER
        }
    }

    private val testPaint by lazy {
        Paint().apply {
            color = Color.GREEN
            style = Paint.Style.STROKE
            isAntiAlias = true
            strokeWidth = 3F
        }
    }

    private val regions by lazy { mutableListOf<Region>() }

    private val regionRectF by lazy { RectF() }

    private val areaPath by lazy { Path() }

    private val arcPath by lazy { Path() }
    private val textRect by lazy { Rect() }
    private fun drawDial(canvas: Canvas) {
        arcPath.reset()
        arcPath.addArc(arcRectF, startAngle, sweepAngle)
        canvas.drawPath(arcPath, arcPaint2)
        canvas.drawPath(arcPath, arcPaint)

        regions.clear()
        for (i in 0 until dataList.size) {
            val diskIm = dataList[i]

            val currentDegree = 110 + averageAngle * i + averageAngle / 2
            if (diskIm.isCurrent()) {
                degree = 110 + averageAngle * i + averageAngle / 2
                arcPath.reset()
                arcPath.addArc(arcRectF, degree + 180 - averageAngle / 2, averageAngle)
                mPaint.color = diskIm.getColor()
                mPaint.alpha = 80
                canvas.drawPath(arcPath, mPaint)
            }

            val textRadius = arcRectF.width() / 2
            val x = arcRectF.centerX() - textRadius * Math.cos(Math.toRadians(currentDegree.toDouble()))
            val y = arcRectF.centerY() - textRadius * Math.sin(Math.toRadians(currentDegree.toDouble()))

            val outerRadius = outerArcRectF.width() / 2
            val degree1 = currentDegree - averageAngle / 2
            val degree2 = currentDegree + averageAngle / 2
            val aX: Float = (arcRectF.centerX() - outerRadius * Math.cos(Math.toRadians(degree1.toDouble()))).toFloat()
            val aY: Float = (arcRectF.centerY() - outerRadius * Math.sin(Math.toRadians(degree1.toDouble()))).toFloat()

//            val bX: Float = (arcRectF.centerX() - outerRadius * Math.cos(Math.toRadians(degree2.toDouble()))).toFloat()
//            val bY: Float = (arcRectF.centerY() - outerRadius * Math.sin(Math.toRadians(degree2.toDouble()))).toFloat()

            val innerRadius = innerArcRectF.width() / 2
            val cX: Float = (arcRectF.centerX() - innerRadius * Math.cos(Math.toRadians(degree2.toDouble()))).toFloat()
            val cY: Float = (arcRectF.centerY() - innerRadius * Math.sin(Math.toRadians(degree2.toDouble()))).toFloat()

//            val dX: Float = (arcRectF.centerX() - innerRadius * Math.cos(Math.toRadians(degree1.toDouble()))).toFloat()
//            val dY: Float = (arcRectF.centerY() - innerRadius * Math.sin(Math.toRadians(degree1.toDouble()))).toFloat()

            areaPath.reset()
            areaPath.moveTo(aX, aY)
            areaPath.addArc(outerArcRectF, degree1 + 180, averageAngle)
            areaPath.lineTo(cX, cY)
            areaPath.addArc(innerArcRectF, degree2 + 180, -averageAngle)
            areaPath.lineTo(aX, aY)
            areaPath.computeBounds(regionRectF, true)


            val region = Region()
            region.setPath(
                areaPath, Region(
                    regionRectF.left.toInt(),
                    regionRectF.top.toInt(),
                    regionRectF.right.toInt(),
                    regionRectF.bottom.toInt()
                )
            )
            regions.add(region)
//            canvas.drawPath(areaPath, testPaint)

            textPaint.textSize = if (diskIm.isCurrent()) textSizeSelect else textSizeNormal
            textPaint.color = diskIm.getColor()

            textPaint.getTextBounds(diskIm.getContent(), 0, diskIm.getContent().length, textRect)

            canvas.drawText(
                diskIm.getContent(),
                x.toFloat(),
                y.toFloat() + textRect.height() / 2,
                textPaint
            )

        }
    }

    //角度
    private var degree = 110.0F

    //圆半径
    private var radius = 200F

    //三角形边长
    private var sideLength = 50

    private fun drawPointer(canvas: Canvas) {
        pointerPath.reset()

        val t = Math.toDegrees(Math.asin((sideLength / 2 / radius).toDouble()))

        val angle1 = degree - t
        val angle2 = degree + t

        val point1X: Float = (arcRectF.centerX() - radius * Math.cos(Math.toRadians(angle1))).toFloat()
        val point1Y: Float = (arcRectF.centerY() - radius * Math.sin(Math.toRadians(angle1))).toFloat()

        val point2X: Float = (arcRectF.centerX() - radius * Math.cos(Math.toRadians(angle2))).toFloat()
        val point2Y: Float = (arcRectF.centerY() - radius * Math.sin(Math.toRadians(angle2))).toFloat()


        val point3X: Float =
            (arcRectF.centerX() - pointerRectF.width() / 2 * Math.cos(Math.toRadians(degree.toDouble()))).toFloat()
        val point3Y: Float =
            (arcRectF.centerX() - pointerRectF.width() / 2 * Math.sin(Math.toRadians(degree.toDouble()))).toFloat()

        pointerPath.moveTo(point1X, point1Y)
        pointerPath.lineTo(point2X, point2Y)
        pointerPath.lineTo(point3X, point3Y)
        pointerPath.close()

        canvas.drawCircle(arcRectF.centerX(), arcRectF.centerY(), radius, pointPaint2)
        canvas.drawPath(pointerPath, pointPaint2)
        canvas.drawCircle(arcRectF.centerX(), arcRectF.centerY(), radius, pointPaint)
        canvas.drawPath(pointerPath, pointPaint)
    }

    private var downX = 0f
    private var downY = 0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) return super.onTouchEvent(event)
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x
                downY = event.y
                pointToPosition(downX, downY)
                super.onTouchEvent(event)
            }
            MotionEvent.ACTION_UP -> {
                super.onTouchEvent(event)
            }
            else -> {
                super.onTouchEvent(event)
            }
        }
    }

    private var lastIndex = 0
    /**
     * 根据点获取对应的位置
     *
     * @param x
     * @param y
     * */
    private fun pointToPosition(x: Float, y: Float) {
        regions.forEachIndexed { index, region ->
            if (dataList[index].isCurrent()) {
                lastIndex = index
            }
            dataList[index].setCurrent(false)
            if (region.contains(x.toInt(), y.toInt())) {
                dataList[index].setCurrent(true)
            }
        }

        if (dataList.isNotEmpty() && dataList.none { it.isCurrent() }) {
            dataList[lastIndex].setCurrent(true)
        }

        invalidate()
    }

    /**
     * 设置数据
     * */
    fun setData(data: MutableList<out DiskIm>) {
        dataList.clear()
        dataList.addAll(data)
        averageAngle = sweepAngle / (dataList.size)
        invalidate()
    }

}