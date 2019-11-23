package com.lmoumou.lib_widget.widget

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
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
    }

    private val mRectF by lazy { RectF() }

    //外刻度盘
    private val arcRectF by lazy { RectF() }

    //内指针
    private val pointerRectF by lazy { RectF() }

    //指针路径
    private val pointerPath by lazy { Path() }

    private val dataList: MutableList<DiaskBeen> by lazy {
        mutableListOf<DiaskBeen>(
            DiaskBeen("01", true),
            DiaskBeen("02"),
            DiaskBeen("03"),
            DiaskBeen("04"),
            DiaskBeen("05"),
            DiaskBeen("06", isShowContentStr = false),
            DiaskBeen("07", isShowContentStr = false),
            DiaskBeen("08", isShowContentStr = false),
            DiaskBeen("09", isShowContentStr = false),
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
            DiaskBeen("20", isShowContentStr = false),
            DiaskBeen("21", isShowContentStr = false),
            DiaskBeen("22", isShowContentStr = false),
            DiaskBeen("23", isShowContentStr = false),
            DiaskBeen("24", isShowContentStr = false),
            DiaskBeen("25", isShowContentStr = false),
            DiaskBeen("26", isShowContentStr = false),
            DiaskBeen("27", isShowContentStr = false),
            DiaskBeen("28", isShowContentStr = false),
            DiaskBeen("29", isShowContentStr = false),
            DiaskBeen("30", isShowContentStr = false)
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
        mRectF.set(0F, 0F, width.toFloat(), height.toFloat())
        outerArcRectF.set(0F, 0F, width.toFloat(), height.toFloat())
        arcRectF.set(
            0F + arcPaint.strokeWidth / 2,
            0F + arcPaint.strokeWidth / 2,
            width.toFloat() - arcPaint.strokeWidth / 2,
            height.toFloat() - arcPaint.strokeWidth / 2
        )

        innerArcRectF.set(
            arcPaint.strokeWidth,
            arcPaint.strokeWidth,
            width - arcPaint.strokeWidth,
            height - arcPaint.strokeWidth
        )

        val dfValue = arcPaint.strokeWidth + 20
        pointerRectF.set(0 + dfValue, 0 + dfValue, width - dfValue, height - dfValue)

        val x = sideLength / 2
        val y = pointerRectF.width() / 2 - sideLength * Math.cos(Math.toRadians(30.0))

        radius = Math.sqrt(Math.pow(x.toDouble(), 2.0) + Math.pow(y, 2.0)).toFloat()

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            canvasCopy = it
            //画外圈刻度盘
            drawDial(it)

            //画内带三角的圆
            drawPointer(it)
        }
    }

    private val arcPaint by lazy {
        Paint().apply {
            color = Color.RED
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = width / 8F
            strokeCap = Paint.Cap.ROUND
        }
    }

    private val mPaint by lazy {
        Paint().apply {
            color = Color.parseColor("#80FFFFFF")
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
        }
    }


    private val startAngle = 290F
    private val sweepAngle = 320F

    private val textPaint by lazy {
        TextPaint().apply {
            color = Color.YELLOW
            textSize = 45F
            style = Paint.Style.FILL
            textAlign = Paint.Align.CENTER
        }
    }

    private val testPath by lazy {
        Paint().apply {
            color = Color.GREEN
            style = Paint.Style.STROKE
            isAntiAlias = true
            strokeWidth = 3F
        }
    }

    private val regions by lazy { mutableListOf<Region>() }

    private val testRectF by lazy { RectF() }

    private val areaPath by lazy { Path() }

    private val innerRegion by lazy { Region() } //外圆弧区域

    private val arcRegion by lazy { Region() } //外圆弧区域
    private val arcPath by lazy { Path() }
    private val textRect by lazy { Rect() }
    private fun drawDial(canvas: Canvas) {
        arcPath.reset()
        arcRegion.setEmpty()
        arcPath.addArc(arcRectF, startAngle, sweepAngle)

        canvas.drawPath(arcPath, arcPaint)

        //获取外圆弧区域
        arcPath.reset()
        arcPath.addArc(mRectF, startAngle, sweepAngle)
        arcPath.computeBounds(mRectF, true)
        arcRegion.setPath(
            arcPath, Region(
                mRectF.left.toInt(), mRectF.top.toInt(), mRectF.right.toInt(),
                mRectF.bottom.toInt()
            )
        )

        //获取内圆弧区域
        arcPath.reset()
        arcPath.addArc(innerArcRectF, startAngle, sweepAngle)
        arcPath.computeBounds(mRectF, true)
        innerRegion.setPath(
            arcPath,
            Region(
                innerArcRectF.left.toInt(),
                innerArcRectF.top.toInt(),
                innerArcRectF.right.toInt(),
                innerArcRectF.bottom.toInt()
            )
        )

        arcRegion.op(innerRegion, Region.Op.XOR)
        regions.clear()
        for (i in 0 until dataList.size) {
            val diskIm = dataList[i]

            val currentDegree = 110 + averageAngle * i + averageAngle / 2
            if (diskIm.isSelect) {
                degree = 110 + averageAngle * i + averageAngle / 2
                arcPath.reset()
                arcPath.addArc(arcRectF, degree + 180 - averageAngle / 2, averageAngle)
                canvas.drawPath(arcPath, mPaint)
            }

            val textRadius = arcRectF.width() / 2
            val x = arcRectF.centerX() - textRadius * Math.cos(Math.toRadians(currentDegree.toDouble()))
            val y = arcRectF.centerY() - textRadius * Math.sin(Math.toRadians(currentDegree.toDouble()))

            val outerRadius = width / 2
            val degree1 = currentDegree - averageAngle / 2
            val degree2 = currentDegree + averageAngle / 2
            val aX: Float = (arcRectF.centerX() - outerRadius * Math.cos(Math.toRadians(degree1.toDouble()))).toFloat()
            val aY: Float = (arcRectF.centerY() - outerRadius * Math.sin(Math.toRadians(degree1.toDouble()))).toFloat()

            val bX: Float = (arcRectF.centerX() - outerRadius * Math.cos(Math.toRadians(degree2.toDouble()))).toFloat()
            val bY: Float = (arcRectF.centerY() - outerRadius * Math.sin(Math.toRadians(degree2.toDouble()))).toFloat()

            val innerRadius = width / 2 - arcPaint.strokeWidth
            val cX: Float = (arcRectF.centerX() - innerRadius * Math.cos(Math.toRadians(degree2.toDouble()))).toFloat()
            val cY: Float = (arcRectF.centerY() - innerRadius * Math.sin(Math.toRadians(degree2.toDouble()))).toFloat()

            val dX: Float = (arcRectF.centerX() - innerRadius * Math.cos(Math.toRadians(degree1.toDouble()))).toFloat()
            val dY: Float = (arcRectF.centerY() - innerRadius * Math.sin(Math.toRadians(degree1.toDouble()))).toFloat()

            areaPath.reset()
            areaPath.moveTo(aX, aY)
            areaPath.addArc(outerArcRectF, degree1 + 180, averageAngle)
            areaPath.lineTo(cX, cY)
            areaPath.moveTo(aX, aY)
            areaPath.lineTo(dX, dY)
            areaPath.addArc(innerArcRectF, degree1 + 180, averageAngle)
            areaPath.close()
            areaPath.computeBounds(testRectF, true)
            val region = Region()
            region.setPath(
                areaPath, Region(
                    testRectF.left.toInt(),
                    testRectF.top.toInt(),
                    testRectF.right.toInt(),
                    testRectF.bottom.toInt()
                )
            )
            regions.add(region)
            canvas.drawPath(areaPath, testPath)
//            canvas.drawRect(region.bounds, testPath)

            if (diskIm.isShowContentStr||diskIm.isSelect) {
                textPaint.getTextBounds(diskIm.contentStr, 0, diskIm.contentStr.length, textRect)
                canvas.drawText(
                    diskIm.contentStr,
                    x.toFloat(),
                    y.toFloat() + textRect.height() / 2,
                    textPaint
                )
            } else {
                canvas.drawCircle(x.toFloat(), y.toFloat(), 15F, pointPaint)
            }
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


        canvas.drawCircle(arcRectF.centerX(), arcRectF.centerY(), radius, pointPaint)

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
        canvas.drawPath(pointerPath, pointPaint)
    }

    private var downX = 0f
    private var downY = 0f

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) return super.onTouchEvent(event)
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x
                downY = event.y
                judgeArea(downX, downY)
            }
            MotionEvent.ACTION_UP -> {
                val upX = event.x
                val upY = event.y

                pointToPosition(downX, downY)

                judgeArea(upX, upY)
            }
            else -> {
                super.onTouchEvent(event)
            }
        }
    }

    /**
     * 判断点击点是否在外圆弧区域
     *
     * @param x
     * @param y
     * */
    private fun judgeArea(x: Float, y: Float): Boolean {
        var result = false
        result = arcRegion.contains(x.toInt(), y.toInt())
//        Log.e(TAG, "result->${result}")
        return result
    }

    private lateinit var canvasCopy: Canvas
    /**
     * 根据点获取对应的位置
     *
     * @param x
     * @param y
     * */
    private fun pointToPosition(x: Float, y: Float) {
        regions.forEachIndexed { index, region ->
            dataList[index].isSelect=false
            if (region.contains(x.toInt(), y.toInt())) {
                Log.e(TAG,"index->$index")
                dataList[index].isSelect=true
            }
        }

        invalidate()
    }


}