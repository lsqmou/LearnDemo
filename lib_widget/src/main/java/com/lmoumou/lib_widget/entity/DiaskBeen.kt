package com.lmoumou.lib_widget.entity

import android.graphics.Path
import android.graphics.RectF
import android.graphics.Region

/**
 * @author Lmoumou
 * @date : 2019/11/22 13:43
 */
data class DiaskBeen(
    var contentStr: String = "",
    var isSelect: Boolean = false,
    var isShowContentStr: Boolean = true
) {

    private val rectF by lazy { RectF() }
    private val region: Region = Region()

    fun setRegin(path: Path) {
        path.computeBounds(rectF, true)
        region.setPath(
            path, Region(
                rectF.left.toInt(),
                rectF.top.toInt(),
                rectF.right.toInt(),
                rectF.bottom.toInt()
            )
        )
    }

    fun isRegion(x: Int, y: Int): Boolean {
        return region.contains(x, y)
    }

}