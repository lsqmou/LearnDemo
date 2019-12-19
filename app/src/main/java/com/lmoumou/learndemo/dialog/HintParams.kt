package com.lmoumou.learndemo.dialog

import android.content.Context
import android.graphics.Color

/**
 * @author Lmoumou
 * @date : 2019/12/16 14:08
 */
data class HintParams(
    var context: Context,
    var layoutId: Int,
    var title: CharSequence = "",//标题
    var with: Int = 0,//窗口宽
    var withPercentage: Float = 0.7F,//占宿主Activity的窗口的比例
    var leftText: CharSequence = "取消",//左边按钮文本
    var onLeftClickListener: HintDialog.OnClickListener? = null,
    var rightText: CharSequence = "确定",//右边按钮文本
    var onRightClickListener: HintDialog.OnClickListener? = null,
    var lineColor:Int=Color.parseColor("#F1F2F2"),
    var mCancelable: Boolean = true,//点击返回键消失
    var mOutSide: Boolean = true//点击屏幕消失
)