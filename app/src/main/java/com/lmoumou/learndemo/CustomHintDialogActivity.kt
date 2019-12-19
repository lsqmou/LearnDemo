package com.lmoumou.learndemo

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import com.lmoumou.learndemo.dialog.HintDialog
import kotlinx.android.synthetic.main.activity_custom_hint_dialog.*

/**
 * @author Lmoumou
 * @date : 2019/12/16 11:00
 */
class CustomHintDialogActivity : AppCompatActivity() {
    companion object {

        private const val TAG = "CustomHintDialogActivity"

        fun startThis(context: Context) {
            context.startActivity(Intent(context.applicationContext, CustomHintDialogActivity::class.java))
        }
    }

    @SuppressLint("LongLogTag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_hint_dialog)

        bt1.setOnClickListener {
            AlertDialog.Builder(this).setTitle("系统弹窗")
                .setPositiveButton("右边", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        Log.e(TAG, "点击右边按钮")
                    }
                }).setNegativeButton("左边", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        Log.e(TAG, "点击左边按钮")
                    }
                }).show()
        }

        bt2.setOnClickListener {
            HintDialog.Builder(this)
                .setTitle(SpannableString("自定义弹窗").apply {
                    setSpan(ForegroundColorSpan(Color.BLUE), 0, "自定义弹窗".length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                })
                .setCancelable(true)
                .setOutSide(true)
                .setLeftButton("左边", object : HintDialog.OnClickListener {
                    override fun onClick(view: View) {
                        Log.e(TAG, "点击左边按钮")
                    }
                })
                .setRightButton("右边", object : HintDialog.OnClickListener {
                    override fun onClick(view: View) {
                        Log.e(TAG, "点击右边按钮")
                    }
                })
                .show(supportFragmentManager)
        }
    }
}