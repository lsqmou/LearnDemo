package com.soandso.lib_pdf

import android.annotation.SuppressLint
import android.webkit.WebView

/**
 * @author Lmoumou
 * @date : 2019/11/19 10:00
 */
@SuppressLint("SetJavaScriptEnabled")
fun WebView.init() {
    val setting=this.settings
    setting.savePassword=false
    setting.javaScriptEnabled=true
    setting.allowFileAccessFromFileURLs=true
    setting.allowUniversalAccessFromFileURLs=true
    setting.builtInZoomControls=true
}