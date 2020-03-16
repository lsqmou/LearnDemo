package com.lmoumou.lib_widget.widget.record

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.lmoumou.lib_widget.R
import kotlinx.android.synthetic.main.dialog_recorder.view.*

/**
 * @author Lmoumou
 * @date : 2020/3/16 11:06
 */
class DialogManager(private val mContext: Context) {

    private var mDialog: Dialog? = null
    private val mInflater by lazy { LayoutInflater.from(mContext) }
    private lateinit var view: View

    fun showRecordingDialog() {
        if (mDialog == null) {
            mDialog = Dialog(mContext, R.style.Theme_AudioDialog)
            view = mInflater.inflate(R.layout.dialog_recorder, null)
            mDialog!!.setContentView(view)
        }

        mDialog!!.show()
    }

    //正在播放时的状态
    fun recording() {
        if (mDialog != null && mDialog!!.isShowing) {
            view.mIcon.visibility = View.VISIBLE
            view.mVoice.visibility = View.VISIBLE
            view.mLable.visibility = View.VISIBLE

            view.mIcon.setImageResource(R.mipmap.recorder)
            view.mLable.text = "手指上划，取消发送"
        }
    }

    //想要取消
    fun wantToCancel() {
        if (mDialog != null && mDialog!!.isShowing) {
            view.mIcon.visibility = View.VISIBLE
            view.mVoice.visibility = View.GONE
            view.mLable.visibility = View.VISIBLE

            view.mIcon.setImageResource(R.mipmap.cancel)
            view.mLable.text = "松开手指，取消发送"
        }
    }

    //录音时间太短
    fun tooShort() {
        if (mDialog != null && mDialog!!.isShowing) {
            view.mIcon.visibility = View.VISIBLE
            view.mVoice.visibility = View.GONE
            view.mLable.visibility = View.VISIBLE

            view.mIcon.setImageResource(R.mipmap.voice_to_short)
            view.mLable.text = "录音时间过短"
        }
    }

    //关闭dialog
    fun dimissDialog() {
        if (mDialog != null && mDialog!!.isShowing) {
            mDialog!!.dismiss()
            mDialog = null
        }
    }

    /**
     * 通过level更新voice上的图片
     *
     * @param level
     */
    fun updateVoiceLevel(level: Int) {
        if (mDialog != null && mDialog!!.isShowing) {
            val resId = mContext.resources.getIdentifier("v$level", "mipmap", mContext.packageName)
            view.mVoice.setImageResource(resId)
        }
    }

}