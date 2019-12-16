package com.lmoumou.learndemo.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lmoumou.learndemo.R
import kotlinx.android.synthetic.main.dialog_hint.view.*

/**
 * @author Lmoumou
 * @date : 2019/12/16 11:11
 */
@SuppressLint("ValidFragment")
class HintDialog(private val params: HintParams) : DialogFragment() {

    companion object {
        private const val TAG = "HintDialog"
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            val window = it.window

            //弹窗宽高
            val dm = DisplayMetrics()
            activity?.windowManager?.defaultDisplay?.getMetrics(dm)
            window.setLayout((dm.widthPixels * 0.7).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)

            //背景
            window.setBackgroundDrawable(ColorDrawable(0))
            //动画
            window.setWindowAnimations(android.R.style.Animation_Dialog)

            val params = window.attributes
            //居屏幕中间显示
            params.gravity = Gravity.CENTER
            //背景透明度
            params.dimAmount = 0.5F
            window.attributes = params
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //样式
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)

        isCancelable=params.mCancelable
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(params.layoutId, container)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        //点击屏幕消失
        dialog.setCanceledOnTouchOutside(params.mOutSide)
        //点击返回键消失
        dialog.setCancelable(params.mCancelable)
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.tvTitle.text = params.title

    }

    class Builder(context: Context, layoutId: Int = R.layout.dialog_hint) {
        private val params by lazy { HintParams(context = context, layoutId = layoutId) }

        /**
         * 设置标题
         * */
        fun setTitle(title: String): HintDialog.Builder {
            params.title = title
            return this
        }

        /**
         * 点击返回键是否消失
         * */
        fun setCancelable(cancelable: Boolean): HintDialog.Builder {
            params.mCancelable = cancelable
            return this
        }

        /**
         * 点击屏幕是否消失
         * */
        fun setOutSide(outSide:Boolean):HintDialog.Builder{
            params.mOutSide=outSide
            return this
        }

        fun create(): HintDialog {
            val dialog = HintDialog(params)
            return dialog
        }

        fun show(supportFragmentManager: FragmentManager): HintDialog {
            val dialog = create()
            dialog.show(supportFragmentManager, HintDialog::class.java.simpleName)
            return dialog
        }
    }
}