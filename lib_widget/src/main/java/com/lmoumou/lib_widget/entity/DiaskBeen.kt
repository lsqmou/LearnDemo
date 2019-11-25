package com.lmoumou.lib_widget.entity

import android.graphics.Color

/**
 * @author Lmoumou
 * @date : 2019/11/22 13:43
 */
data class DiaskBeen(
    var contentStr: String = "",
    var stage: Int = 0,//1->安全期，2->月经期，3->预测月经期，4->排卵期
    var isSelect: Boolean = false
) : DiskIm {
    override fun isCurrent(): Boolean = isSelect

    override fun setCurrent(b: Boolean) {
        this.isSelect = b
    }

    override fun getContent(): String = contentStr

    override fun getSatus(): Int = stage

    override fun getColor(): Int {
        return when (stage) {
            2 -> Color.parseColor("#FE7E6D")
            4 -> Color.parseColor("#A4A8FF")
            else -> Color.parseColor("#59E8D8")
        }
    }
}