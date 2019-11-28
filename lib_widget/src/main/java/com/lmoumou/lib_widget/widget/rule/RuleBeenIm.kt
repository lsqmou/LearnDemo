package com.lmoumou.lib_widget.widget.rule

import android.graphics.Point
import android.graphics.PointF
import android.graphics.Region

/**
 * @author Lmoumou
 * @date : 2019/11/27 17:40
 */
interface RuleBeenIm {

    /**
     * 是否是当前选中
     * */
    fun isCurrent(): Boolean

    fun setCurrent(b: Boolean)

    /**
     * 顶部展示的文本内容
     * */
    fun getTopStr(): String

    /**
     * 底部展示的文本内容
     * */
    fun getRuleScore(): Int

    /**
     * 区域Region
     * */
    fun getRegion(): Region

    fun getPoint(): PointF

    /**
     * 判断点击点是否在区域内
     * */
    fun judgeRegion(x: Int, y: Int): Boolean {
        return getRegion().contains(x, y)
    }
}