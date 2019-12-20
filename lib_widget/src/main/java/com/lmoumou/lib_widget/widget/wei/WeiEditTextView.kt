package com.lmoumou.lib_widget.widget.wei

import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.EditText

/**
 * @author Lmoumou
 * @date : 2019/12/20 16:03
 */
class WeiEditTextView : EditText {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                refreshEditTextUI("$s")
            }
        })
    }

    private fun refreshEditTextUI(content: String) {
        if (linkList == null || linkList.isEmpty()) return
        if (TextUtils.isEmpty(content)) return
    }

    private val linkList = mutableListOf<WeiLinkBeen>()


}