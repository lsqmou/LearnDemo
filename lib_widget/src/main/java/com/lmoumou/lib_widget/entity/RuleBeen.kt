package com.lmoumou.lib_widget.entity

import android.graphics.PointF
import android.graphics.Region
import com.lmoumou.lib_widget.widget.rule.RuleBeenIm

/**
 * @author Lmoumou
 * @date : 2019/11/27 17:44
 */
class RuleBeen(
    var dateStr: String = "",
    var score: Int = 0,
    var isSelect: Boolean = false,
    private val reg: Region = Region(),
    private val po: PointF = PointF()
) : RuleBeenIm {
    override fun getPoint(): PointF = po

    override fun getRuleScore(): Int = score

    override fun setCurrent(b: Boolean) {
        isSelect = b
    }

    override fun getRegion(): Region = reg

    override fun getTopStr(): String = dateStr


    override fun isCurrent(): Boolean = isSelect
}