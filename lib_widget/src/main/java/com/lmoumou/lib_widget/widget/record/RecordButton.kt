package com.lmoumou.lib_widget.widget.record

import android.content.Context
import android.media.AudioFormat
import android.os.Environment
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
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
 * @date : 2020/3/16 10:09
 */
class RecordButton : TextView {

    companion object {
        private const val TAG = "RecordButton"
        //状态
        private const val STATE_NORMAL = 1
        private const val STATE_RECORDING = 2
        private const val STATE_WANT_TO_CANCEL = 3

        //手指滑动 距离
        private const val DISTANCE_Y_CANCEL = 50
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        mDialogManager = DialogManager(context)

        initRecord(context)

    }

    private var mTime: Float = 0F

    //当前状态
    private var mCurState = STATE_NORMAL
    //已经开始录音
    private var isRecording = false

    private var mDialogManager: DialogManager? = null
    private val recordManager by lazy { RecordManager.getInstance() }

     var mListener: OnRecordListener? = null

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
            Log.e(TAG, "path->${it.absolutePath}")
            when (mCurState) {
                STATE_WANT_TO_CANCEL -> {//取消
                    mListener?.onCancel()
                }
                STATE_RECORDING -> {//完成并发送
                    mListener?.onFinish(it.absolutePath)
                }
                STATE_NORMAL -> {//不应该有次情况
                    mListener?.onFinish(it.absolutePath)
                }
            }
        }

        recordManager.setRecordSoundSizeListener { soundSize ->
            mDialogManager?.updateVoiceLevel(
                soundSize % 6
            )
        }

        recordManager.setRecordStateListener(object : RecordStateListener {
            override fun onStateChange(state: RecordHelper.RecordState?) {
                when (state) {
                    RecordHelper.RecordState.PAUSE -> {

                    }
                    RecordHelper.RecordState.IDLE -> {

                    }
                    RecordHelper.RecordState.RECORDING -> {

                    }
                    RecordHelper.RecordState.STOP -> {

                    }
                    RecordHelper.RecordState.FINISH -> {

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

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) return super.onTouchEvent(event)

        val action = event.action
        val x = event.x.toInt()
        val y = event.y.toInt()

        when (action) {
            MotionEvent.ACTION_DOWN -> {//0
                isRecording = true
                mDialogManager?.showRecordingDialog()
                recordManager.start()
                changeState(STATE_RECORDING)
            }
            MotionEvent.ACTION_MOVE -> {//2

                if (isRecording) {
                    if (wantToCancel(x, y)) {
                        changeState(STATE_WANT_TO_CANCEL)
                    } else {
                        changeState(STATE_RECORDING)
                    }
                }
            }
            MotionEvent.ACTION_UP -> {//1
                recordManager.stop()
                mDialogManager!!.dimissDialog()
                reset()
            }
        }
        return true
    }

    private fun reset() {
        isRecording = false
        changeState(STATE_NORMAL)
    }

    private fun wantToCancel(x: Int, y: Int): Boolean {
        if (x < 0 || x > width) return true
        if (y < -DISTANCE_Y_CANCEL || y > height + DISTANCE_Y_CANCEL) return true
        return false
    }


    private fun changeState(state: Int) {
        if (mCurState != state) {
            mCurState = state
            when (state) {
                STATE_NORMAL -> {
                    setBackgroundResource(R.drawable.btn_recorder_normal)
                    text = "按住说话"
                }
                STATE_RECORDING -> {
                    setBackgroundResource(R.drawable.btn_recording)
                    text = "松开结束"
                }
                STATE_WANT_TO_CANCEL -> {
                    setBackgroundResource(R.drawable.btn_recording)
                    text = "松开手指，取消发送"
                }
            }
        }
    }

    interface OnRecordListener {
        fun onFinish(path: String)
        fun onCancel()
    }

}