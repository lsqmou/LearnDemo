package com.lmoumou.lib_widget.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.lmoumou.lib_widget.R
import com.lmoumou.lib_widget.widget.RecordVoiceView
import com.lmoumou.lib_widget.widget.record.RecordButton
import kotlinx.android.synthetic.main.activity_record_voice.*

/**
 * @author Lmoumou
 * @date : 2019/12/11 9:54
 */
class RecordVoiceActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "RecordVoiceActivity"
        fun startThis(context: Context) {
            context.startActivity(
                Intent(
                    context.applicationContext,
                    RecordVoiceActivity::class.java
                )
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_voice)

        mRecordButton.mListener = object : RecordButton.OnRecordListener {
            override fun onFinish(path: String) {
                Log.e(TAG, "path->${path}")
            }

            override fun onCancel() {
                Log.e(TAG, "onCancel")
            }
        }

//        mRecordVoiceView.recordListener = object : RecordVoiceView.RecordVoiceListener {
//            override fun onStart() {
//                Log.e(TAG, "onStart")
//            }
//
//            override fun onFinish() {
//                Log.e(TAG, "onFinish")
//            }
//
//            override fun onError(msg: String?) {
//                Log.e(TAG, "onError->$msg")
//            }
//
//            override fun onVoicePath(path: String) {
//                Log.e(TAG, "onVoicePath->$path")
//            }
//        }
    }
}