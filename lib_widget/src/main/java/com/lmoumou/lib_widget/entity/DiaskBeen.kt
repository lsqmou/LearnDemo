package com.lmoumou.lib_widget.entity

/**
 * @author Lmoumou
 * @date : 2019/11/22 13:43
 */
data class DiaskBeen(var contentStr: String = "", var isSelect: Boolean = false, var isShowContentStr: Boolean = true) :
    DiskIm {
    override fun isShowContent(): Boolean = isShowContentStr

    override fun isCurrent(): Boolean = isSelect

    override fun getContent(): String = contentStr
}