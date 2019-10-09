package com.lmoumou.lib_calendar.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * 作者：Lmoumou
 * 时间：2019/4/26 17:13
 *
 * 单选，多选操作实体类
 */

open class BaseSelectBean(var isSelect: Boolean = false) : Parcelable {
    constructor(source: Parcel) : this(
        1 == source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt((if (isSelect) 1 else 0))
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<BaseSelectBean> = object : Parcelable.Creator<BaseSelectBean> {
            override fun createFromParcel(source: Parcel): BaseSelectBean = BaseSelectBean(source)
            override fun newArray(size: Int): Array<BaseSelectBean?> = arrayOfNulls(size)
        }
    }
}