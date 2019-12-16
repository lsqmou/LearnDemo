package com.lmoumou.learndemo.dialog

import android.content.Context

/**
 * @author Lmoumou
 * @date : 2019/12/16 14:08
 */
data class HintParams(
    var context: Context,
    var layoutId: Int,
    var title: String = "",//标题
    var mCancelable: Boolean = true,//点击返回键消失
    var mOutSide: Boolean = true//点击屏幕消失
)