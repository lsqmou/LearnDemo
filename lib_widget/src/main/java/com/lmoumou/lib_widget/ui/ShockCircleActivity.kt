package com.lmoumou.lib_widget.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.lmoumou.lib_widget.R
import kotlinx.android.synthetic.main.activity_shock_circle.*

/**
 * @author:Lmoumou
 * @date:2019/11/13
 * 描述:
 **/
class ShockCircleActivity : AppCompatActivity() {

    companion object {
        fun startThis(context: Context) {
            context.startActivity(Intent(context.applicationContext, ShockCircleActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shock_circle)

        mShockCircleView.startAnimation()

        mShockEllipseView.startAnimator()
    }
}