package com.lmoumou.lib_widget.widget

import android.content.Context
import android.graphics.*
import android.media.AudioFormat
import android.os.Environment
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.lmoumou.lib_widget.BuildConfig
import com.lmoumou.lib_widget.R
import com.zlw.main.recorderlib.RecordManager
import com.zlw.main.recorderlib.recorder.RecordConfig
import com.zlw.main.recorderlib.recorder.RecordHelper
import com.zlw.main.recorderlib.recorder.listener.RecordStateListener
import java.io.File
import java.util.*

/**
 * @author Lmoumou
 * @date : 2019/12/10 16:00
 *
 * 录音控件
 */
class RecordVoiceView : View {

    companion object {
        private const val TAG = "RecordVoiceView"
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        mBitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.record_voice2, null)
        radius = mBitmap.height / 2 + 30F

        initRecord(context)
    }

    //位图
    private var mBitmap: Bitmap
    //图片上间距
    private var imgSpaceTop = 0
    //图片下间距
    private var imgSpaceBottom = 0
    //图片矩阵
    private val imgRectF by lazy { RectF() }

    //圆形背景画笔
    private val bgCirclePaint by lazy {
        Paint().apply {
            isAntiAlias = true
            color = Color.parseColor("#75A1F3")
            style = Paint.Style.FILL
        }
    }
    //半径
    private var radius = 0F

    //底部文本
    private var bottomText = "长按开始"
    //底部文本画笔
    private val bottomTextPaint = Paint().apply {
        color = Color.parseColor("#333333")
//        color = Color.WHITE
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
        textSize = 45F
    }

    //顶部文本
    private var topText = 0
    //顶部画笔
    private val topTextPaint = Paint().apply {
        color = Color.parseColor("#75A1F3")
//        color = Color.WHITE
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
        textSize = 36F
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSize = measureWidth(widthMeasureSpec)
        val heightSize = measureHeight(heightMeasureSpec)
        setMeasuredDimension(widthSize, heightSize)
    }

    /**
     * 测量宽度
     * */
    private fun measureWidth(widthMeasureSpec: Int): Int {
        val spaceSize = MeasureSpec.getSize(widthMeasureSpec)
        val spaceModel = MeasureSpec.getMode(widthMeasureSpec)
        return when (spaceModel) {
            MeasureSpec.EXACTLY -> spaceSize
            else -> {
                Math.max(
                    mBitmap.width,
                    bottomTextPaint.measureText(bottomText).toInt()
                ) + paddingStart + paddingEnd + paddingLeft + paddingRight
            }
        }
    }

    /**
     * 测量高度
     * */
    private fun measureHeight(heightMeasureSpec: Int): Int {
        val spaceSize = MeasureSpec.getSize(heightMeasureSpec)
        val spaceModel = MeasureSpec.getMode(heightMeasureSpec)
        return when (spaceModel) {
            MeasureSpec.EXACTLY -> spaceSize
            else -> {
                val topFontMetrics = topTextPaint.fontMetrics
                val topTextHeight = Math.abs(topFontMetrics.descent - topFontMetrics.ascent).toInt()

                val bottomFontMetrics = bottomTextPaint.fontMetrics
                val bottomTextHeight = Math.abs(bottomFontMetrics.descent - bottomFontMetrics.ascent).toInt()
                (topTextHeight + imgSpaceTop + radius * 2 + imgSpaceBottom + bottomTextHeight + paddingBottom + paddingTop).toInt()
            }
        }
    }

    //画布矩阵
    private val mRectF by lazy { RectF() }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w == 0 || h == 0) return
        mRectF.set(0F, 0F, w.toFloat(), h.toFloat())
        imgRectF.set(
            mRectF.centerX() - radius,
            mRectF.centerY() - radius,
            mRectF.centerX() + radius,
            mRectF.centerY() + radius
        )
    }

    private lateinit var mCanvas: Canvas
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            mCanvas = it
            drawableTopText()
            drawableBitmap(it)
            drawableBottomText(it)
        }
    }

    var recordListener: RecordVoiceListener? = null

    private val recordManager by lazy { RecordManager.getInstance() }
    private fun initRecord(context: Context) {
        recordManager.init(context.applicationContext, BuildConfig.DEBUG)
        recordManager.changeFormat(RecordConfig.RecordFormat.MP3)
        recordManager.changeRecordConfig(recordManager.recordConfig.setSampleRate(44100))
        recordManager.changeRecordConfig(recordManager.recordConfig.setEncodingConfig(AudioFormat.ENCODING_PCM_16BIT))
        val recordDir = String.format(
            Locale.getDefault(), "%s/Record/Learn/",
            Environment.getExternalStorageDirectory().absolutePath
        )
        val file = File(recordDir)
        if (file.exists()) {
            file.delete()
        }
        recordManager.changeRecordDir(recordDir)

        recordManager.setRecordResultListener {
            recordListener?.onVoicePath(it.absolutePath)
        }

        recordManager.setRecordStateListener(object : RecordStateListener {
            override fun onStateChange(state: RecordHelper.RecordState?) {
                when (state) {
                    RecordHelper.RecordState.PAUSE -> {
                        Log.e(TAG, "onStateChange->暂停")
                    }
                    RecordHelper.RecordState.IDLE -> {
                        Log.e(TAG, "onStateChange->空闲")
                    }
                    RecordHelper.RecordState.RECORDING -> {
                        Log.e(TAG, "onStateChange->录音中")
                    }
                    RecordHelper.RecordState.STOP -> {
                        Log.e(TAG, "onStateChange->停止")
                    }
                    RecordHelper.RecordState.FINISH -> {
                        Log.e(TAG, "onStateChange->录音结束")
                        recordListener?.onFinish()
                        handler.removeCallbacksAndMessages(null)
                        topText = 0
                        bottomText = "长按开始"
                        invalidate()
                    }
                    else -> {

                    }
                }
            }

            override fun onError(error: String?) {
                Log.e(TAG, "onError->$error")
            }
        })
    }

    private val mHandler by lazy { Handler() }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mHandler.removeCallbacksAndMessages(null)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) return super.onTouchEvent(event)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {//开始录制
                if (imgRectF.contains(event.x, event.y)) {
                    recordManager.start()
                    recordListener?.onStart()
                    bottomText = "松开完成"
                    doTime()
                }
                return true
            }
            MotionEvent.ACTION_UP -> {//结束录制
                recordManager.stop()
                return true
            }
        }


        return super.onTouchEvent(event)
    }

    private fun doTime() {
        topText++
        Log.e(TAG, "topText->$topText")
        invalidate()
        handler.postDelayed({ doTime() }, 1000)
    }

    /**
     * 绘制上文本
     * */
    private fun drawableTopText() {
        val fontMetrics = topTextPaint.fontMetrics
        val startX = mRectF.centerX()
        val startY =
            mRectF.centerY() - radius - imgSpaceTop - Math.abs(fontMetrics.descent - fontMetrics.ascent) / 2
        mCanvas.drawText("$topText", startX, startY, topTextPaint)
    }

    /**
     * 绘制Bitmap
     * */
    private fun drawableBitmap(canvas: Canvas) {
        //背景圆
        canvas.drawCircle(mRectF.centerX(), mRectF.centerY(), radius, bgCirclePaint)
        val startX = mRectF.centerX() - mBitmap.width / 2
        val startY = mRectF.centerY() - mBitmap.height / 2
        canvas.drawBitmap(mBitmap, startX, startY, null)
    }

    /**
     * 绘制底部文字
     * */
    private fun drawableBottomText(canvas: Canvas) {
        val fontMetrics = bottomTextPaint.fontMetrics
        val startX = mRectF.centerX()
        val startY =
            mRectF.centerY() + radius + imgSpaceBottom + Math.abs(fontMetrics.descent - fontMetrics.ascent)
        canvas.drawText(bottomText, startX, startY, bottomTextPaint)
    }

    interface RecordVoiceListener {
        fun onStart()
        fun onFinish()
        fun onError(msg: String?)
        fun onVoicePath(path: String)
    }

}