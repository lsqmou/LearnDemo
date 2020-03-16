package com.lmoumou.lib_widget.widget.reply

import android.text.SpannableStringBuilder

/**
 * @author Lmoumou
 * @date : 2019/12/23 11:17
 */
interface ReplyIm {
    fun getCommentContent(): SpannableStringBuilder
    fun getReplayContent(): SpannableStringBuilder
}