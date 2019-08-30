package com.lmoumou.lib_widget.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.lmoumou.lib_widget.R
import kotlinx.android.synthetic.main.activity_double_wave.*

/**
 * @author Lmoumou
 * @date : 2019/8/30 15:10
 */
class DoubleWaveActivity : AppCompatActivity() {

    companion object {
        fun startThis(content: Context) {
            content.startActivity(Intent(content.applicationContext, DoubleWaveActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_double_wave)

        btStart.setOnClickListener {
         mDoubleWaveView.startAnimation()
        }
    }
}