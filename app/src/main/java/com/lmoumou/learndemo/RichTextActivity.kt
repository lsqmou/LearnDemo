package com.lmoumou.learndemo

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.util.Linkify
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_rich_text.*

/**
 * @author Lmoumou
 * @date : 2019/11/14 9:35
 */
class RichTextActivity : AppCompatActivity() {

    companion object {
        private const val content =
            "11月13日消息，相信不少人对微信朋友圈的广告已经屡见不鲜了，不过今天朋友圈广告却闹了乌龙。据网友视频爆料，今天早上奥迪Q8在微信朋友圈的广告视频内容播放的却是英菲尼迪汽车内容。"

        private const val TAG = "RichTextActivity"
        fun startThis(context: Context) {
            context.startActivity(Intent(context.applicationContext, RichTextActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rich_text)


        val sp = SpannableString("我是前缀：")
        sp.setSpan(ForegroundColorSpan(Color.BLUE), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        sp.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                Log.e(TAG, "富文本")
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isAntiAlias = true
                ds.isUnderlineText = false
            }
        }, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        mTextView.movementMethod = ClickableMovementMethod.getInstance()
        mTextView.text = sp
        mTextView.append(content)

        mLinearLayout.setOnClickListener {
            Log.e(TAG, "LinearLayout")
        }

//        mTextView.setOnClickListener {
//            Log.e(TAG, "TextView")
//        }
    }
}