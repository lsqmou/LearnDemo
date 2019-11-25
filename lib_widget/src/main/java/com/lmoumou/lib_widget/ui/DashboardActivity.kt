package com.lmoumou.lib_widget.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.lmoumou.lib_widget.R
import com.lmoumou.lib_widget.entity.DiaskBeen
import kotlinx.android.synthetic.main.activity_dashbora.*

/**
 * @author Lmoumou
 * @date : 2019/11/21 10:03
 */
class DashboardActivity : AppCompatActivity() {

    companion object {
        fun startThis(context: Context) {
            context.startActivity(Intent(context.applicationContext, DashboardActivity::class.java))
        }
    }

    private val testData by lazy { mutableListOf<DiaskBeen>() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashbora)
        for (i in 1..30) {
            testData.add(DiaskBeen("$i", isSelect = i == 15))
        }
        mDiskView.setData(testData)
    }
}