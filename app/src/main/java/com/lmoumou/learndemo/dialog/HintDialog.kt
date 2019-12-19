package com.lmoumou.learndemo.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.annotation.ColorRes
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lmoumou.learndemo.R
import kotlinx.android.synthetic.main.dialog_hint.view.*

/**
 * @author Lmoumou
 * @date : 2019/12/16 11:11
 *
 * 仿照AlertDialog自定义提示弹窗
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
            if (params.withPercentage <= 0.0F) {
                window.setLayout(params.with, ViewGroup.LayoutParams.WRAP_CONTENT)
            } else {
                val dm = DisplayMetrics()
                activity?.windowManager?.defaultDisplay?.getMetrics(dm)
                window.setLayout((dm.widthPixels * params.withPercentage).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
            }

            //背景
            window.setBackgroundDrawable(ColorDrawable(0))
            //动画
            window.setWindowAnimations(android.R.style.Animation_Dialog)

            val windowParams = window.attributes
            //居屏幕中间显示
            windowParams.gravity = Gravity.CENTER
            //背景透明度
            windowParams.dimAmount = 0.5F
            window.attributes = windowParams
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //样式
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
        //点击返回键是否消失
        isCancelable = params.mCancelable
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

        view.tvLeft.text = params.leftText
        view.tvLeft.setOnClickListener {
            params.onLeftClickListener?.onClick(it)
        }
        view.tvRight.text = params.rightText
        view.tvRight.setOnClickListener {
            params.onRightClickListener?.onClick(it)
        }

        view.lineVertical.setBackgroundColor(params.lineColor)
        view.lineHorizontal.setBackgroundColor(params.lineColor)
    }

    class Builder(context: Context, layoutId: Int = R.layout.dialog_hint) {
        private val params by lazy { HintParams(context = context, layoutId = layoutId) }

        /**
         * 设置标题
         * */
        fun setTitle(title: CharSequence): HintDialog.Builder {
            params.title = title
            return this
        }

        /**
         * 弹窗宽
         * */
        fun setWith(value: Int = ViewGroup.LayoutParams.WRAP_CONTENT): HintDialog.Builder {
            params.with = value
            return this
        }

        fun setWithPercentage(value: Float): HintDialog.Builder {
            params.withPercentage = value
            return this
        }

        /**
         * 设置左边按钮文本和点击事件
         * */
        fun setLeftButton(text: CharSequence, listener: OnClickListener? = null): HintDialog.Builder {
            params.leftText = text
            params.onLeftClickListener = listener
            return this
        }

        /**
         * 设置右边按钮文本和点击事件
         * */
        fun setRightButton(text: CharSequence, listener: OnClickListener? = null): HintDialog.Builder {
            params.rightText = text
            params.onRightClickListener = listener
            return this
        }

        /**
         * 分割线颜色
         * */
        fun setLineColor(color: Int): HintDialog.Builder {
            params.lineColor = color
            return this
        }

        fun setLineColorRes(@ColorRes color: Int): HintDialog.Builder {
            params.lineColor = ContextCompat.getColor(params.context, color)
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
        fun setOutSide(outSide: Boolean): HintDialog.Builder {
            params.mOutSide = outSide
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


    interface OnClickListener {
        fun onClick(view: View)
    }
}