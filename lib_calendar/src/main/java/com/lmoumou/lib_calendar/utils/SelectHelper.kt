package com.lmoumou.lib_calendar.utils

import com.lmoumou.lib_calendar.bean.BaseSelectBean

/**
 * 作者：Lmoumou
 * 时间：2019/4/26 17:15
 *
 * 单选多选操作辅助类
 */
object SelectHelper {

    /**
     * 全选、撤销全选选择实体列表
     *
     * @param selectBeans 要操作的集合
     * @param select true->全选，false->全不选
     * */
    fun selectAllSelectBeans(selectBeans: List<out BaseSelectBean>, select: Boolean) {
        if (selectBeans.isEmpty()) {
            return
        }
        selectBeans.forEach {
            it.isSelect = select
        }
    }

    /**
     * 全选、撤销全选选择实体列表
     * @param selectBeans 要操作的数组
     * @param select true->全选，false->全不选
     * */
    fun selectAllSelectBeans(selectBeans: Array<out BaseSelectBean>, select: Boolean) {
        if (selectBeans.isEmpty()) {
            return
        }
        selectBeans.forEach {
            it.isSelect = select
        }
    }

    /**
     * 检查选择实体列表是否已经全选
     * */
    fun isSelectAll(selectBeans: List<out BaseSelectBean>): Boolean {
        if (selectBeans.isEmpty()) {
            return false
        }
        selectBeans.forEach {
            if (!it.isSelect) {
                return false
            }
        }

        return true
    }

    /**
     * 检查选择实体列表是否都没选
     * */
    fun isNotSelectAll(selectBeans: List<out BaseSelectBean>): Boolean {
        if (selectBeans.isEmpty()) {
            return false
        }
        selectBeans.forEach {
            if (it.isSelect) {
                return false
            }
        }

        return true
    }

    /**
     * 获取已被选择的选择实体（单选）
     * */
    fun <T : BaseSelectBean> getSelectBean(selectBeans: List<T>): T? {
        if (selectBeans.isEmpty()) {
            return null
        }

        selectBeans.forEach {
            if (it.isSelect) {
                return it
            }
        }
        return null
    }

    /**
     * 获取已被选择的选择实体（多选）
     * */
    fun <T : BaseSelectBean> getSelectBeans(selectBeans: List<T>): List<T> {
        val ts = mutableListOf<T>()
        if (selectBeans.isNotEmpty()) {
            selectBeans.forEach {
                if (it.isSelect) {
                    ts.add(it)
                }
            }
        }
        return ts
    }


}