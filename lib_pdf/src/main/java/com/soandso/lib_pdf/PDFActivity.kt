package com.soandso.lib_pdf

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_pdf.*

/**
 * @author Lmoumou
 * @date : 2019/11/19 9:50
 */
class PDFActivity : AppCompatActivity() {

    companion object {
        fun startThis(context: Context) {
            context.startActivity(Intent(context.applicationContext, PDFActivity::class.java))
        }
    }

    private val docPath = "https://puboss.chengdumaixun.com/article/20ce83b3-a790-b1c0-e484-ef075434c2c0.pdf"
    private val testPath="https://www.jianshu.com/p/dade5a944584"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf)

        mWebView.init()
        mWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                view?.loadUrl(request?.url.toString())
                return true
            }
        }

        mWebView.webChromeClient = object : WebChromeClient() {}
//file:///android_asset/pdfjs_compatibility/web/viewer.html?file=
        mWebView.loadUrl("file:///android_asset/pdfjs_compatibility/web/viewer.html?file=$docPath")
    }
}