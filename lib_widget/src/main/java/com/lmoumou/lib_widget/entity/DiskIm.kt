package com.lmoumou.lib_widget.entity

/**
 * @author Lmoumou
 * @date : 2019/11/22 13:41
 */
interface DiskIm {

    fun isCurrent(): Boolean

    fun getContent(): String

    fun getSatus():Int

    fun getColor():Int

    fun setCurrent(b:Boolean)

}