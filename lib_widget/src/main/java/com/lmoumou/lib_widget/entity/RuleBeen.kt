package com.lmoumou.lib_widget.entity

import com.lmoumou.lib_widget.widget.rule.RuleBeenIm

/**
 * @author Lmoumou
 * @date : 2019/11/27 17:44
 */
class RuleBeen(var isSelect: Boolean = false) : RuleBeenIm {
    override fun isCurrent(): Boolean = isSelect
}